package org.apollo.game.release.r83;


import org.apollo.game.message.impl.OpenInterfaceMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageEncoder;

public class OpenInterfaceMessageEncoder extends MessageEncoder<OpenInterfaceMessage> {

    @Override
    public GamePacket encode(OpenInterfaceMessage event) {
        GamePacketBuilder bldr = new GamePacketBuilder(66);
		bldr.put(DataType.SHORT, event.getTargetInterfaceId());
        bldr.put(DataType.SHORT, event.getLayerId());
        bldr.put(DataType.SHORT, event.getWindowId());
		bldr.put(DataType.BYTE, DataTransformation.SUBTRACT, event.isWalkable() ? 1 : 0);
        return bldr.toGamePacket();
    }
}
