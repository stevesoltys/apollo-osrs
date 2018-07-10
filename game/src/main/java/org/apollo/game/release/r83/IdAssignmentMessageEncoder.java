package org.apollo.game.release.r83;

import org.apollo.game.message.impl.IdAssignmentMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * Created by garyttierney on 12/09/15.
 */
public class IdAssignmentMessageEncoder extends MessageEncoder<IdAssignmentMessage> {
    @Override
    public GamePacket encode(IdAssignmentMessage message) {
        GamePacketBuilder builder = new GamePacketBuilder();
        builder.put(DataType.SHORT, message.getId());
        builder.put(DataType.BYTE, message.isMembers() ? 0 : 1);

        return builder.toGamePacket();
    }
}
