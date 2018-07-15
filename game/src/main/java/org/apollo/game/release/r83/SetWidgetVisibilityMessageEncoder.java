package org.apollo.game.release.r83;

import org.apollo.game.message.impl.SetWidgetVisibilityMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class SetWidgetVisibilityMessageEncoder extends MessageEncoder<SetWidgetVisibilityMessage> {

	@Override
	public GamePacket encode(SetWidgetVisibilityMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(246);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.isEnabled() ? 0 : 1);
		builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getInterfaceId() << 16 | message.getId());
		return builder.toGamePacket();
	}
}
