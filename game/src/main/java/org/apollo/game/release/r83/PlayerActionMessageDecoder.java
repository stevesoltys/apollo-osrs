package org.apollo.game.release.r83;

import org.apollo.game.message.impl.PlayerActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Steve Soltys
 */
public class PlayerActionMessageDecoder extends MessageDecoder<PlayerActionMessage> {

	@Override
	public PlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		if (packet.getOpcode() == 111) {
			int index = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
			int unknown = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.NEGATE);
			return new PlayerActionMessage(4, index);
		}

		return null;
	}
}
