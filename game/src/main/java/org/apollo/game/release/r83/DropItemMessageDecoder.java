package org.apollo.game.release.r83;

import org.apollo.game.message.impl.DropItemMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Steve Soltys
 */
public class DropItemMessageDecoder extends MessageDecoder<DropItemMessage> {

	@Override
	public DropItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int interfaceHash = (int) reader.getUnsigned(DataType.INT);
		int interfaceId = interfaceHash >> 16;

		return new DropItemMessage(interfaceId, id, slot);
	}
}
