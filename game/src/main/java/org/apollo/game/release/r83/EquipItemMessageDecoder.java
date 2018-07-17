package org.apollo.game.release.r83;

import org.apollo.game.message.impl.EquipItemMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Steve Soltys
 */
public class EquipItemMessageDecoder extends MessageDecoder<EquipItemMessage> {

	@Override
	public EquipItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int hash = (int) reader.getSigned(DataType.INT);
		int interfaceId = hash >> 16;
		int itemId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);

		return new EquipItemMessage(interfaceId, itemId, slot);
	}
}
