package org.apollo.game.release.r83;

import org.apollo.game.message.impl.ServerChatMessage;
import org.apollo.game.message.impl.SetPlayerActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link ServerChatMessage}.
 *
 * @author Steve Soltys
 */
public class SetPlayerActionMessageEncoder extends MessageEncoder<SetPlayerActionMessage> {

	@Override
	public GamePacket encode(SetPlayerActionMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(131, PacketType.VARIABLE_BYTE);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.isPrimaryAction() ? 1 : 0);
		builder.putString(message.getText());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getSlot());
		return builder.toGamePacket();
	}
}
