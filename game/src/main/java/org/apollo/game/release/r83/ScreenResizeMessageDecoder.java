package org.apollo.game.release.r83;

import org.apollo.game.message.impl.ScreenResizeMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

public class ScreenResizeMessageDecoder extends MessageDecoder<ScreenResizeMessage> {

    @Override
    public ScreenResizeMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
		int mode = (int) reader.getUnsigned(DataType.BYTE);
		int width = (int) reader.getUnsigned(DataType.SHORT);
		int height = (int) reader.getUnsigned(DataType.SHORT);

        return new ScreenResizeMessage(mode, width, height);
    }
}
