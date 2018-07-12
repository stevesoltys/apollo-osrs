package org.apollo.game.release.r83;

import org.apollo.game.message.impl.ServerChatMessage;
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
public class ServerMessageMessageEncoder extends MessageEncoder<ServerChatMessage> {

    @Override
    public GamePacket encode(ServerChatMessage message) {
        GamePacketBuilder builder = new GamePacketBuilder(79, PacketType.VARIABLE_BYTE);
		builder.putSmart(0); // type
		builder.put(DataType.BYTE, 0); // extra
        builder.putString(message.getMessage());
        return builder.toGamePacket();
    }
}
