package org.apollo.game.release.r83;

import com.oldscape.tool.util.MapXTEA;
import org.apollo.game.message.impl.RegionChangeMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class RegionChangeMessageEncoder extends MessageEncoder<RegionChangeMessage> {

	@Override
	public GamePacket encode(RegionChangeMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(133, PacketType.VARIABLE_SHORT);
		Position pos = message.getPosition();

		int regionX = pos.getCentralRegionX();
		int regionY = pos.getCentralRegionY();

		boolean forceSend = true;

		if ((((regionX / 8) == 48) || ((regionX / 8) == 49)) && ((regionY / 8) == 48)) {
			forceSend = false;
		}
		if (((regionX / 8) == 48) && ((regionY / 8) == 148)) {
			forceSend = false;
		}

		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, pos.getLocalX());

		for (int xCalc = (regionX - 6) / 8; xCalc <= (regionX + 6) / 8; xCalc++) {
			for (int yCalc = (regionY - 6) / 8; yCalc <= (regionY + 6) / 8; yCalc++) {
				int region = yCalc + (xCalc << 8);
				final int[] mapData = MapXTEA.getKey(region);
				if (forceSend
					|| ((yCalc != 49) && (yCalc != 149) && (yCalc != 147)
					&& (xCalc != 50) && ((xCalc != 49) || (yCalc != 47)))) {
					for (int key : mapData)
						builder.put(DataType.INT, key);
				}
			}
		}

		builder.put(DataType.SHORT, DataOrder.LITTLE, pos.getLocalY());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, pos.getHeight());
		builder.put(DataType.SHORT, DataTransformation.ADD, regionX);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, regionY);

		return builder.toGamePacket();
	}
}
