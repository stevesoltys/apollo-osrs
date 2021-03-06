package org.apollo.net.codec.game;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.apollo.net.meta.PacketMetaData;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.Release;
import org.apollo.util.StatefulFrameDecoder;
import org.apollo.util.security.IsaacRandom;

import java.util.List;

/**
 * A {@link StatefulFrameDecoder} that decodes {@link GamePacket}s.
 *
 * @author Graham
 */
public final class GamePacketDecoder extends StatefulFrameDecoder<GameDecoderState> {

	/**
	 * The current length.
	 */
	private int length;

	/**
	 * The current opcode.
	 */
	private int opcode;

	/**
	 * The random number generator.
	 */
	private final IsaacRandom random;

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * The packet type.
	 */
	private PacketType type;

	/**
	 * Creates the {@link GamePacketDecoder}.
	 *
	 * @param random The random number generator.
	 * @param release The current release.
	 */
	public GamePacketDecoder(IsaacRandom random, Release release) {
		super(GameDecoderState.GAME_OPCODE);
		this.random = random;
		this.release = release;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, GameDecoderState state) {
		switch (state) {
			case GAME_OPCODE:
				decodeOpcode(in, out);
				break;
			case GAME_LENGTH_BYTE:
				decodeLengthByte(in);
				break;
			case GAME_LENGTH_SHORT:
				decodeLengthShort(in);
				break;
			case GAME_PAYLOAD:
				decodePayload(in, out);
				break;
			case GAME_REMAINING:
				decodeRemaining(in);
				break;
			default:
				throw new IllegalStateException("Invalid game decoder state.");
		}
	}

	/**
	 * Decodes the length state.
	 *
	 * @param buffer The buffer.
	 */
	private void decodeLengthByte(ByteBuf buffer) {
		if (buffer.isReadable()) {
			length = buffer.readUnsignedByte();
			if (length != 0) {
				setState(GameDecoderState.GAME_PAYLOAD);
			}
		}
	}

	/**
	 * Decodes the length state.
	 *
	 * @param buffer The buffer.
	 */
	private void decodeLengthShort(ByteBuf buffer) {
		if (buffer.isReadable()) {
			length = buffer.readUnsignedShort();
			if (length != 0) {
				setState(GameDecoderState.GAME_PAYLOAD);
			}
		}
	}

	/**
	 * Decodes the length of the packet by looking at the remaining bytes in the buffer.
	 *
	 * @param buffer The buffer.
	 */
	private void decodeRemaining(ByteBuf buffer) {
		if (buffer.isReadable()) {
			length = buffer.readableBytes();
			setState(GameDecoderState.GAME_PAYLOAD);
		}
	}

	/**
	 * Decodes the opcode state.
	 *
	 * @param buffer The buffer.
	 * @param out The {@link List} of objects to be passed along the pipeline.
	 */
	private void decodeOpcode(ByteBuf buffer, List<Object> out) {
		if (buffer.isReadable()) {
			int encryptedOpcode = buffer.readUnsignedByte();
			opcode = encryptedOpcode;// - random.nextInt() & 0xFF; TODO: Fix Isaac random for incoming packets.

			PacketMetaData metaData = release.getIncomingPacketMetaData(opcode);
			Preconditions.checkNotNull(metaData, "Illegal opcode: " + opcode + ".");

			type = metaData.getType();
			switch (type) {
				case FIXED:
					length = metaData.getLength();
					if (length == 0) {
						setState(GameDecoderState.GAME_OPCODE);
						out.add(new GamePacket(opcode, type, Unpooled.EMPTY_BUFFER));
					} else {
						setState(GameDecoderState.GAME_PAYLOAD);
					}
					break;
				case VARIABLE_BYTE:
					setState(GameDecoderState.GAME_LENGTH_BYTE);
					break;
				case VARIABLE_SHORT:
					setState(GameDecoderState.GAME_LENGTH_SHORT);
					break;
				case REMAINING:
					setState(GameDecoderState.GAME_REMAINING);
					break;
				default:
					throw new IllegalStateException("Illegal packet type: " + type + ".");
			}
		}
	}

	/**
	 * Decodes the payload state.
	 *
	 * @param buffer The buffer.
	 * @param out The {@link List} of objects to be passed along the pipeline.
	 */
	private void decodePayload(ByteBuf buffer, List<Object> out) {
		if (buffer.readableBytes() >= length) {
			ByteBuf payload = buffer.readBytes(length);
			setState(GameDecoderState.GAME_OPCODE);
			out.add(new GamePacket(opcode, type, payload));
		}
	}

}