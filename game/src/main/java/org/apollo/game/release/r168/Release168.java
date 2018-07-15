package org.apollo.game.release.r168;

import org.apollo.game.message.impl.RegionChangeMessage;
import org.apollo.game.message.impl.SendWindowPaneMessage;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

/**
 * @author Steve Soltys
 */
public class Release168 extends Release {

	/**
	 * The incoming packet lengths array.
	 */
	public static final int[] PACKET_LENGTHS = {
		16, -2, -1, 3, 3, 4, 7, -2, 3, -1, 15,
		7, -1, -1, 8, 14, 7, 8, 8, 4, 8, 3, 6, 11, 0, 1, -1, 0, 10, 2, 15, -1, -1, 8, 9, 8, 13, 3, 8, -1, 9, 8, 3, 8,
		11, 13, 8, 3, 3, 4, -1, -1, 13, 2, 7, 2, 8, 5, 7, 9, 7, 16, 3, 8, 6, 8, 8, -1, 7, 0, 8, 3, 3, 4, 9, 2, 0, 4,
		-1, -1, 8, 3, 16, 7, 7, 8, 7, -1, -1, 3, 8, 3, -1, -1, -2, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
	};

	public Release168() {
		super(168, PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
		init();
	}

	/**
	 * Initialises this release by registering encoders and decoders.
	 */
	private void init() {
		register(RegionChangeMessage.class, new RegionChangeMessageEncoder());
//		register(PlayerSynchronizationMessage.class, new PlayerSynchronizationMessageEncoder());
//		register(NpcSynchronizationMessage.class, new NpcSynchronizationMessageEncoder());
		register(SendWindowPaneMessage.class, new SendWindowPaneEncoder());
//		register(OpenInterfaceMessage.class, new OpenInterfaceMessageEncoder());

//		register(UpdateSkillMessage.class, new UpdateSkillMessageEncoder());
//		register(UpdateItemsMessage.class, new UpdateItemsMessageEncoder());
//		register(UpdateRunEnergyMessage.class, new UpdateRunEnergyEncoder());
//		register(ServerChatMessage.class, new ServerMessageMessageEncoder());

//		WalkMessageDecoder walkingDecoder = new WalkMessageDecoder();
//		register(177, walkingDecoder);
//		register(161, walkingDecoder);
//
//		register(55, new PublicChatMessageDecoder());
//		register(129, new ScreenResizeMessageDecoder());
//		register(235, new CommandMessageDecoder());
//
//		ItemOptionMessageDecoder itemOptionMessageDecoder = new ItemOptionMessageDecoder();
//		register(32, itemOptionMessageDecoder);
//
//		register(255, new ButtonMessageDecoder());
	}
}
