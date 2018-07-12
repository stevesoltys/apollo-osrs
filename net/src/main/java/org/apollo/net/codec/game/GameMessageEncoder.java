package org.apollo.net.codec.game;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apollo.net.message.Message;
import org.apollo.net.release.MessageEncoder;
import org.apollo.net.release.Release;

import java.util.List;
import java.util.logging.Logger;

/**
 * A {@link MessageToMessageEncoder} which encodes {@link Message}s into {@link GamePacket}s.
 *
 * @author Graham
 */
public final class GameMessageEncoder extends MessageToMessageEncoder<Message> {

	private static final Logger logger = Logger.getLogger(GameMessageEncoder.class.getName());

	/**
	 * The current release.
	 */
	private final Release release;

	/**
	 * Creates the game message encoder with the specified release.
	 *
	 * @param release The release.
	 */
	public GameMessageEncoder(Release release) {
		this.release = release;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, List<Object> out) {
		MessageEncoder<Message> encoder = (MessageEncoder<Message>) release.getMessageEncoder(message.getClass());
//		logger.info(String.format("Encoding message \"%s\"", message.getClass().getSimpleName()));

		if (encoder != null) {
			out.add(encoder.encode(message));
		}
	}

}