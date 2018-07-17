package org.apollo.game.release.r83;

import org.apollo.game.message.impl.SendObjectMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link SendObjectMessage}.
 *
 * @author Major
 */
public final class SendObjectMessageEncoder extends MessageEncoder<SendObjectMessage> {

	@Override
	public GamePacket encode(SendObjectMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(215);
		builder.put(DataType.SHORT, message.getId());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getPositionOffset());
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getType() << 2 | message.getOrientation());
		return builder.toGamePacket();
	}

}