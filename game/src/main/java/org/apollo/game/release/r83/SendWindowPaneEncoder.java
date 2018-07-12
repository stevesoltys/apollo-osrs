package org.apollo.game.release.r83;

import org.apollo.game.message.impl.SendWindowPaneMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

/**
 * Created by garyttierney on 12/09/15.
 */
public class SendWindowPaneEncoder extends MessageEncoder<SendWindowPaneMessage> {

    @Override
    public GamePacket encode(SendWindowPaneMessage message) {
        GamePacketBuilder builder = new GamePacketBuilder(108);
        builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getWindowPaneId());
        return builder.toGamePacket();
    }
}
