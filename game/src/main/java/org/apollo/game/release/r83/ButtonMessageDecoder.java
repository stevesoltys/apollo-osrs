package org.apollo.game.release.r83;

import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Steve Soltys
 */
public class ButtonMessageDecoder extends MessageDecoder<ButtonMessage> {

    @Override
    public ButtonMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int interfaceId = (int) reader.getSigned(DataType.SHORT);
        int button = (int) reader.getSigned(DataType.SHORT);
        int childButton = 0;
        if (packet.getLength() >= 6) {
            childButton = (int) reader.getSigned(DataType.SHORT);
            if (childButton >= 65535) {
                childButton = 0;
            }
        }
        return new ButtonMessage(interfaceId, button, childButton);
    }
}
