package org.apollo.game.release.r168;

import org.apollo.game.message.impl.SendWindowPaneMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class SendWindowPaneEncoder extends MessageEncoder<SendWindowPaneMessage> {

    @Override
    public GamePacket encode(SendWindowPaneMessage message) {
        GamePacketBuilder builder = new GamePacketBuilder(66);
        builder.put(DataType.SHORT, DataTransformation.ADD, message.getWindowPaneId());
        return builder.toGamePacket();
    }
}
