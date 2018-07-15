package org.apollo.game.release.r83;

import org.apollo.game.message.impl.AccessMaskMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class AccessMaskMessageEncoder extends MessageEncoder<AccessMaskMessage> {

	@Override
	public GamePacket encode(AccessMaskMessage message) {
		GamePacketBuilder gamePacketBuilder = new GamePacketBuilder(196);
		gamePacketBuilder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getFirst());
		gamePacketBuilder.put(DataType.SHORT, message.getSecond());
		gamePacketBuilder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getThird());
		gamePacketBuilder.put(DataType.INT, DataOrder.MIDDLE, message.getFourth());
		return gamePacketBuilder.toGamePacket();
	}
}
