package org.apollo.game.release.r83;

import org.apollo.game.message.impl.EnterAmountMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class EnterAmountMessageEncoder extends MessageEncoder<EnterAmountMessage> {

    @Override
    public GamePacket encode(EnterAmountMessage message) {
        GamePacketBuilder builder = new GamePacketBuilder(167, PacketType.VARIABLE_SHORT);
		builder.putString("s");
		builder.putString("Enter amount");
		builder.put(DataType.INT, 108);
        return builder.toGamePacket();
    }
}
