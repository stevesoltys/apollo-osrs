package org.apollo.game.release.r83;

import org.apollo.game.message.impl.PublicChatMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.TextUtil;

public class PublicChatMessageDecoder extends MessageDecoder<PublicChatMessage> {

    @Override
    public PublicChatMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

		int unknown = (int) reader.getUnsigned(DataType.BYTE);
		int colour = (int) reader.getUnsigned(DataType.BYTE);
        int effects = (int) reader.getUnsigned(DataType.BYTE);
        int messageLength = packet.getLength() - 3;

        byte[] message = new byte[messageLength];
        reader.getBytes(message);

        String uncompressed = TextUtil.filterInvalidCharacters(TextUtil.decompress(message, messageLength));
        byte[] recompressed = new byte[messageLength];
        TextUtil.compress(uncompressed, recompressed);

        return new PublicChatMessage(uncompressed, recompressed, colour, effects);
    }
}
