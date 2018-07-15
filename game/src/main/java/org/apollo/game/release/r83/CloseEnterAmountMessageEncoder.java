package org.apollo.game.release.r83;

import org.apollo.game.message.impl.CloseEnterAmountMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class CloseEnterAmountMessageEncoder extends MessageEncoder<CloseEnterAmountMessage> {

    @Override
    public GamePacket encode(CloseEnterAmountMessage message) {
        return new GamePacketBuilder(138).toGamePacket();
    }
}
