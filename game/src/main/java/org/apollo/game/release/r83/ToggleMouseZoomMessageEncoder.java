package org.apollo.game.release.r83;

import org.apollo.game.message.impl.ToggleMouseZoomMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class ToggleMouseZoomMessageEncoder extends MessageEncoder<ToggleMouseZoomMessage> {

	@Override
	public GamePacket encode(ToggleMouseZoomMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(167, PacketType.VARIABLE_SHORT);
		builder.putString("iiii");
		builder.put(DataType.INT, 17104977);
		builder.put(DataType.INT, 1);
		builder.put(DataType.INT, 17104902);
		builder.put(DataType.INT, 17104900);
		builder.put(DataType.INT, 43);
		return builder.toGamePacket();
	}
}
