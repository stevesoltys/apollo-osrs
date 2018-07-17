package org.apollo.game.release.r83;

import org.apollo.game.message.impl.RemoveTileItemMessage;
import org.apollo.game.message.impl.SendObjectMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SendObjectMessage}.
 *
 * @author Major
 */
public final class RemoveTileItemMessageEncoder extends MessageEncoder<RemoveTileItemMessage> {

	@Override
	public GamePacket encode(RemoveTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(83);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getPositionOffset());
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getId());
		return builder.toGamePacket();
	}

}