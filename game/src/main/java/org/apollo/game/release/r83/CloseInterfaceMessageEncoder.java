package org.apollo.game.release.r83;

import org.apollo.game.message.impl.CloseInterfaceMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class CloseInterfaceMessageEncoder extends MessageEncoder<CloseInterfaceMessage> {

	@Override
	public GamePacket encode(CloseInterfaceMessage message) {
		GamePacketBuilder gamePacketBuilder = new GamePacketBuilder(65);
		gamePacketBuilder.put(DataType.SHORT, message.getWindowPane().getId());
		gamePacketBuilder.put(DataType.SHORT, message.getArea().getId(message.getWindowPane()));
		return gamePacketBuilder.toGamePacket();
	}
}
