package org.apollo.game.release.r83;

import org.apollo.game.message.impl.WalkMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

public class WalkMessageDecoder extends MessageDecoder<WalkMessage> {

    @Override
    public WalkMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

		final int y = (int) reader.getUnsigned(DataType.SHORT);
        final int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
        final boolean run = reader.getUnsigned(DataType.BYTE, DataTransformation.ADD) == 1;

        return new WalkMessage(new Position(x, y), run);
    }
}
