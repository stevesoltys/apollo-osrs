package org.apollo.game.release.r83;

import org.apollo.game.message.impl.ItemOptionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Steve Soltys
 */
public class ItemOptionMessageDecoder extends MessageDecoder<ItemOptionMessage> {

	@Override
	public ItemOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		if (packet.getOpcode() == 198) {
			int itemId = (int) reader.getSigned(DataType.SHORT, DataTransformation.ADD);
			int interfaceId = (int) reader.getSigned(DataType.INT);
			int slot = (int) reader.getSigned(DataType.SHORT);

			return new ItemOptionMessage(1, interfaceId, itemId, slot);
		}

//        if (packet.getOpcode() == 124) {
//            int slot = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
//            int itemId = (int) reader.getUnsigned(DataType.SHORT);
//            int interfaceId = (int) reader.getSigned(DataType.INT, DataOrder.LITTLE);
//
//            return new ItemOptionMessage(2, interfaceId, itemId, slot);
//        } else if (packet.getOpcode() == 89) {
//            int itemId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
//            int hash = (int) reader.getSigned(DataType.INT);
//            int interfaceId = hash >> 16;
//            int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
//
//            return new ItemOptionMessage(1, interfaceId, itemId, slot);
//        } else if (packet.getOpcode() == 41) {
//            int hash = (int) reader.getUnsigned(DataType.INT, DataOrder.LITTLE);
//            int interfaceId = hash >> 16;
//            int itemId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
//            int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
//
//            return new ItemOptionMessage(3, interfaceId, itemId, slot);
//        }

		return null;
	}
}
