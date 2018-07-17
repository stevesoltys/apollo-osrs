package org.apollo.game.release.r83;

import org.apollo.game.message.impl.TakeTileItemMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Steve Soltys
 */
public class TakeTileItemMessageDecoder extends MessageDecoder<TakeTileItemMessage> {

	@Override
	public TakeTileItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int unknown = (int) reader.getUnsigned(DataType.BYTE);
		int y = (int) reader.getUnsigned(DataType.SHORT);

		return new TakeTileItemMessage(id, new Position(x, y));
	}
}
