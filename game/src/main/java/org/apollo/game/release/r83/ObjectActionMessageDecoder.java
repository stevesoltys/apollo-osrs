package org.apollo.game.release.r83;

import org.apollo.game.message.impl.ObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Steve Soltys
 */
public class ObjectActionMessageDecoder extends MessageDecoder<ObjectActionMessage> {

	private static final int OPTION_ONE = 166;

	@Override
	public ObjectActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		if (packet.getOpcode() == OPTION_ONE) {
			int x = (int) reader.getUnsigned(DataType.SHORT);
			int y = (int) reader.getUnsigned(DataType.SHORT);
			int z = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT);
			int objectId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);

			return new ObjectActionMessage(1, objectId, new Position(x, y, z));
		}

		return null;
	}
}
