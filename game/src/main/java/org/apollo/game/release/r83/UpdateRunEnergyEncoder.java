package org.apollo.game.release.r83;

import org.apollo.game.message.impl.UpdateRunEnergyMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Steve Soltys
 */
public class UpdateRunEnergyEncoder extends MessageEncoder<UpdateRunEnergyMessage> {

    @Override
    public GamePacket encode(UpdateRunEnergyMessage message) {
        GamePacketBuilder builder = new GamePacketBuilder(140);
        builder.put(DataType.BYTE, message.getEnergy());
        return builder.toGamePacket();
    }
}
