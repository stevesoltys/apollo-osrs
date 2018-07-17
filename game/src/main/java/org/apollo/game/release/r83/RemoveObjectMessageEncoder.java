package org.apollo.game.release.r83;

import org.apollo.game.message.impl.RemoveObjectMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link RemoveObjectMessage}.
 *
 * @author Major
 */
public final class RemoveObjectMessageEncoder extends MessageEncoder<RemoveObjectMessage> {

	@Override
	public GamePacket encode(RemoveObjectMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(241);
		builder.put(DataType.BYTE, message.getPositionOffset());
		builder.put(DataType.BYTE, message.getType() << 2 | message.getOrientation());
		return builder.toGamePacket();
	}

}