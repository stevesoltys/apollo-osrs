package org.apollo.game.release.r83;

import org.apollo.game.message.impl.ConfigMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class ConfigMessageEncoder extends MessageEncoder<ConfigMessage> {

	@Override
	public GamePacket encode(ConfigMessage message) {

		if (message.getValue() <= -128 || message.getValue() >= 128) {
			GamePacketBuilder builder = new GamePacketBuilder(249);
			builder.put(DataType.INT, DataOrder.MIDDLE, message.getValue());
			builder.put(DataType.SHORT, DataTransformation.ADD, message.getId());
			return builder.toGamePacket();

		} else {
			GamePacketBuilder builder = new GamePacketBuilder(56);
			builder.put(DataType.SHORT, DataOrder.LITTLE, message.getId());
			builder.put(DataType.BYTE, message.getValue());
			return builder.toGamePacket();
		}
	}
}
