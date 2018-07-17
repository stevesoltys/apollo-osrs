package org.apollo.game.release.r83;

import org.apollo.game.message.impl.SendObjectMessage;
import org.apollo.game.message.impl.SendTileItemMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SendObjectMessage}.
 *
 * @author Major
 */
public final class SendPublicTileItemMessageEncoder extends MessageEncoder<SendTileItemMessage> {

	@Override
	public GamePacket encode(SendTileItemMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(205);
		builder.put(DataType.BYTE, message.getPositionOffset());
		builder.put(DataType.SHORT, message.getAmount());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getId());
		return builder.toGamePacket();
	}

}