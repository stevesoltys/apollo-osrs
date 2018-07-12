package org.apollo.game.release.r83;

import org.apollo.game.message.impl.UpdateItemsMessage;
import org.apollo.game.model.Item;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

public class UpdateItemsMessageEncoder extends MessageEncoder<UpdateItemsMessage> {

	@Override
	public GamePacket encode(UpdateItemsMessage event) {
		GamePacketBuilder bldr = new GamePacketBuilder(33, PacketType.VARIABLE_SHORT);
		bldr.put(DataType.INT, event.getInterfaceId() << 16 | event.getChildId());
		bldr.put(DataType.SHORT, event.getType());
		Item[] items = event.getItems();
		bldr.put(DataType.SHORT, items.length);
        for (Item item : items) {
            if (item != null) {
                int count = item.getAmount();

				bldr.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, item.getId() + 1);

                if (count > 254) {
                    bldr.put(DataType.BYTE, DataTransformation.ADD, 255);
                    bldr.put(DataType.INT, count);
                } else {
                    bldr.put(DataType.BYTE, DataTransformation.ADD, count);
                }
            } else {
				bldr.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, 0);
                bldr.put(DataType.BYTE, DataTransformation.ADD, 0);
            }
        }
		return bldr.toGamePacket();
	}
}
