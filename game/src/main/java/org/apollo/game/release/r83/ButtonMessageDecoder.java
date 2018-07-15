package org.apollo.game.release.r83;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

import java.util.Set;

/**
 * @author Steve Soltys
 */
public class ButtonMessageDecoder extends MessageDecoder<ButtonMessage> {

	private static final ImmutableMap<Set<Integer>, Integer> INDICES = ImmutableMap.<Set<Integer>, Integer>builder()
		.put(ImmutableSet.of(255), 1)
		.put(ImmutableSet.of(149), 2)
		.put(ImmutableSet.of(194), 3)
		.put(ImmutableSet.of(159), 4)
		.put(ImmutableSet.of(148), 5)
		.put(ImmutableSet.of(0), 6)
		.put(ImmutableSet.of(245), 7)
		.put(ImmutableSet.of(77), 8)
		.put(ImmutableSet.of(153), 9)
		.put(ImmutableSet.of(46), 10)
		.build();

    @Override
    public ButtonMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getSigned(DataType.SHORT);
		int button = (int) reader.getSigned(DataType.SHORT);
		int childButton = (int) reader.getSigned(DataType.SHORT);
		int itemId = (int) reader.getSigned(DataType.SHORT);

		int index = INDICES.entrySet().stream()
			.filter(entry -> entry.getKey().contains(packet.getOpcode()))
			.findAny().orElseThrow(() -> new IllegalArgumentException("No item action index specified for opcode"))
			.getValue();

        return new ButtonMessage(index, interfaceId, button, childButton, itemId);
    }
}
