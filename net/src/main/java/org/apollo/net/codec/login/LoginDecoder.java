package org.apollo.net.codec.login;

import com.google.common.net.InetAddresses;
import com.oldscape.tool.util.XTEA;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apollo.net.NetworkConstants;
import org.apollo.util.BufferUtil;
import org.apollo.util.StatefulFrameDecoder;
import org.apollo.util.security.IsaacRandom;
import org.apollo.util.security.IsaacRandomPair;
import org.apollo.util.security.PlayerCredentials;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.List;

/**
 * A {@link StatefulFrameDecoder} which decodes the login request frames.
 *
 * @author Graham
 */
public final class LoginDecoder extends StatefulFrameDecoder<LoginDecoderState> {

	/**
	 * The secure random number generator.
	 */
	private static final SecureRandom RANDOM = new SecureRandom();

	/**
	 * The login packet length.
	 */
	private int loginLength;

	/**
	 * The reconnecting flag.
	 */
	private boolean reconnecting;

	/**
	 * The server-side session key.
	 */
	private long serverSeed;

	/**
	 * The username hash.
	 */
	private int usernameHash;

	/**
	 * Creates the login decoder with the default initial state.
	 */
	public LoginDecoder() {
		super(LoginDecoderState.LOGIN_HEADER);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, LoginDecoderState state) {
		switch (state) {
			case LOGIN_HANDSHAKE:
				decodeHandshake(ctx, in, out);
				break;
			case LOGIN_HEADER:
				decodeHeader(ctx, in, out);
				break;
			case LOGIN_PAYLOAD:
				decodePayload(ctx, in, out);
				break;
			default:
				throw new IllegalStateException("Invalid login decoder state: " + state);
		}
	}

	/**
	 * Decodes in the handshake state.
	 *
	 * @param ctx    The channel handler context.
	 * @param buffer The buffer.
	 * @param out    The {@link List} of objects to pass forward through the pipeline.
	 */
	private void decodeHandshake(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.isReadable()) {
			usernameHash = buffer.readUnsignedByte();
			serverSeed = RANDOM.nextLong();

			ByteBuf response = ctx.alloc().buffer(17);
			response.writeByte(LoginConstants.STATUS_EXCHANGE_DATA);
			response.writeLong(0);
			response.writeLong(serverSeed);
			ctx.channel().write(response);

			setState(LoginDecoderState.LOGIN_HEADER);
		}
	}

	/**
	 * Decodes in the header state.
	 *
	 * @param ctx    The channel handler context.
	 * @param buffer The buffer.
	 * @param out    The {@link List} of objects to pass forward through the pipeline.
	 */
	private void decodeHeader(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.readableBytes() >= 2) {
			int type = buffer.readUnsignedByte();

			if (type != LoginConstants.TYPE_STANDARD && type != LoginConstants.TYPE_RECONNECTION) {
				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			reconnecting = type == LoginConstants.TYPE_RECONNECTION;
			loginLength = buffer.readShort();

			setState(LoginDecoderState.LOGIN_PAYLOAD);
		}
	}

	public static String getRS2String(ByteBuf buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.isReadable() && (b = buf.readByte()) != 0) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}


	/**
	 * Decodes in the payload state.
	 *
	 * @param ctx    The channel handler context.
	 * @param buffer The buffer.
	 * @param out    The {@link List} of objects to pass forward through the pipeline.
	 */
	private void decodePayload(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.readableBytes() >= loginLength) {
			ByteBuf payload = buffer.readBytes(loginLength);
			int version = payload.readInt();

			int rsaBlockSize = payload.readUnsignedShort();
			byte[] rsaBlockBytes = new byte[rsaBlockSize];
			payload.readBytes(rsaBlockBytes);
			ByteBuf rBuf = Unpooled.wrappedBuffer(new BigInteger(rsaBlockBytes)
				.modPow(NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS).toByteArray());

			int blockMagic = rBuf.readUnsignedByte();
			if (blockMagic != 1) {
				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			int blockType = rBuf.readUnsignedByte();

			int[] cKeys = new int[4];

			for (int i = 0; i < cKeys.length; i++) {
				cKeys[i] = rBuf.readInt();
			}

			if (blockType == 2) {
				rBuf.readerIndex(rBuf.readerIndex() + 8);

			} else if (blockType == 3 || blockType == 1) {
				rBuf.readUnsignedMedium();
				rBuf.readerIndex(rBuf.readerIndex() + 5);

			} else if (blockType == 0) {
				rBuf.readInt();
				rBuf.readerIndex(rBuf.readerIndex() + 4);
			}

			String password = BufferUtil.readStringNew(rBuf);

			int xteaBlockSize = payload.readableBytes();
			byte[] xteaBlockBytes = new byte[xteaBlockSize];
			payload.readBytes(xteaBlockBytes);
			XTEA.decrypt(xteaBlockBytes, 0, xteaBlockBytes.length, cKeys);
			ByteBuf xBuf = Unpooled.wrappedBuffer(xteaBlockBytes);

			String username = BufferUtil.readStringNew(xBuf);

			InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
			String hostAddress = InetAddresses.toAddrString(socketAddress.getAddress());

			if (password.length() < 6 || password.length() > 20 || username.isEmpty() || username.length() > 12) {
				writeResponseCode(ctx, LoginConstants.STATUS_INVALID_CREDENTIALS);
				return;
			}

			int memoryStatus = xBuf.readUnsignedByte();// low mem
			boolean lowMemory = memoryStatus == 1;

			xBuf.readUnsignedShort();// width
			xBuf.readUnsignedShort();// height

			byte[] random = new byte[24];
			for (int i = 0; i < random.length; i++) {
				random[i] = xBuf.readByte();
			}

			String token = BufferUtil.readStringNew(xBuf);
			xBuf.readInt();// affiliate id

			xBuf.readUnsignedByte();// 6
			xBuf.readUnsignedByte();// OS Type
			xBuf.readUnsignedByte();// 64-Bit OS
			xBuf.readUnsignedByte();// OS Version
			xBuf.readUnsignedByte();// Java Vendor
			xBuf.readUnsignedByte();// Something todo with Java
			xBuf.readUnsignedByte();// Something todo with Java
			xBuf.readUnsignedByte();
			xBuf.readUnsignedByte();// 0
			xBuf.readUnsignedShort();// Max Mem
			xBuf.readUnsignedByte();// Availible Processors
			xBuf.readUnsignedMedium();// 0
			xBuf.readUnsignedShort();// 0
			BufferUtil.readStringNew(xBuf);// usually null
			BufferUtil.readStringNew(xBuf);// usually null
			BufferUtil.readStringNew(xBuf);// usually null
			BufferUtil.readStringNew(xBuf);// usually null
			xBuf.readUnsignedByte();
			xBuf.readUnsignedShort();
			BufferUtil.readStringNew(xBuf);// usually null
			BufferUtil.readStringNew(xBuf);// usually null
			xBuf.readUnsignedByte();
			xBuf.readUnsignedByte();

			int[] var = new int[3];
			for (int i = 0; i < var.length; i++)
				var[i] = xBuf.readInt();

			xBuf.readInt();
			xBuf.readUnsignedByte();

			int[] crc = new int[16];// xBuf.readableBytes() / 4
			for (int i = 0; i < crc.length; i++)
				crc[i] = xBuf.readInt();

			int[] sKeys = new int[cKeys.length];
			for (int i = 0; i < sKeys.length; i++)
				sKeys[i] = cKeys[i] + 50;
//
//			String username =  org.apollo.util.BufferUtil.readStringNew(xBuf);

//			int[] crcs = new int[FileSystemConstants.ARCHIVE_COUNT];
//			for (int index = 0; index < 9; index++) {
//				crcs[index] = payload.readInt();
//			}

//			int length = payload.readUnsignedByte();
//			if (length != loginLength - 41) {
//				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
//				return;
//			}
//
//			ByteBuf secure = payload.readBytes(length);
//
//			BigInteger value = new BigInteger(secure.array());
//			value = value.modPow(NetworkConstants.RSA_EXPONENT, NetworkConstants.RSA_MODULUS);
//			secure = Unpooled.wrappedBuffer(value.toByteArray());
//
//			int id = secure.readUnsignedByte();
//			if (id != 10) {
//				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
//				return;
//			}
//
//			long clientSeed = secure.readLong();
//			long reportedSeed = secure.readLong();
//			if (reportedSeed != serverSeed) {
//				writeResponseCode(ctx, LoginConstants.STATUS_LOGIN_SERVER_REJECTED_SESSION);
//				return;
//			}
//
//			int uid = secure.readInt();
//			String username = BufferUtil.readString(secure);
//			String password = BufferUtil.readString(secure);
//
//			int[] seed = new int[4];
//			seed[0] = (int) (clientSeed >> 32);
//			seed[1] = (int) clientSeed;
//			seed[2] = (int) (serverSeed >> 32);
//			seed[3] = (int) serverSeed;
//
//			IsaacRandom decodingRandom = new IsaacRandom(seed);
//			for (int index = 0; index < seed.length; index++) {
//				seed[index] += 50;
//			}
//
//			IsaacRandom encodingRandom = new IsaacRandom(seed);
//
//
//			IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);

			IsaacRandom decodingRandom = new IsaacRandom(cKeys);
			IsaacRandom encodingRandom = new IsaacRandom(sKeys);

			PlayerCredentials credentials = new PlayerCredentials(username, password, usernameHash, -1, hostAddress);
			IsaacRandomPair randomPair = new IsaacRandomPair(encodingRandom, decodingRandom);

			out.add(new LoginRequest(credentials, randomPair, reconnecting, lowMemory, -1, crc, version));
		}
	}

	/**
	 * Writes a response code to the client and closes the current channel.
	 *
	 * @param ctx      The context of the channel handler.
	 * @param response The response code to write.
	 */
	private void writeResponseCode(ChannelHandlerContext ctx, int response) {
		ByteBuf buffer = ctx.alloc().buffer(Byte.BYTES);
		buffer.writeByte(response);
		ctx.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
	}

}