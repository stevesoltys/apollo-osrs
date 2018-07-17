package org.apollo.game.release.r83;

import org.apollo.game.message.impl.*;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

/**
 * @author Steve Soltys
 */
public class Release83 extends Release {

	/**
	 * Incoming packet sizes array.
	 */
	public static final int[] PACKET_SIZES = new int[256];

	static {
		for (int i = 0; i < PACKET_SIZES.length; i++)
			PACKET_SIZES[i] = -3;
		PACKET_SIZES[177] = -1;
		PACKET_SIZES[118] = -1; // mystery
		PACKET_SIZES[106] = 1; // some boolean flag
		PACKET_SIZES[132] = 0; // unidentified

		PACKET_SIZES[89] = 6;
		PACKET_SIZES[161] = -1;
		PACKET_SIZES[235] = -1; // command
		PACKET_SIZES[83] = -2;
		PACKET_SIZES[55] = -1;
		PACKET_SIZES[250] = 3;
		PACKET_SIZES[255] = 8;
		PACKET_SIZES[198] = 8;
		PACKET_SIZES[128] = -1;
		PACKET_SIZES[171] = -2;
		PACKET_SIZES[111] = 3;
		PACKET_SIZES[199] = 0;
		PACKET_SIZES[65] = 3; //player option 5
		PACKET_SIZES[232] = 11; //item on npc
		PACKET_SIZES[95] = 0; //idle packet
		PACKET_SIZES[44] = 14; //magic on item
		PACKET_SIZES[74] = 13; //magic on ground item
		PACKET_SIZES[121] = 9; //magic on npc
		PACKET_SIZES[206] = 2; //ge packet
		PACKET_SIZES[3] = 6; //dialogue handler
		PACKET_SIZES[78] = 4; //welcome screen
		PACKET_SIZES[176] = 16; //item on item
		PACKET_SIZES[29] = -1; //clan ranking
		PACKET_SIZES[204] = -3; //clan kick
		PACKET_SIZES[136] = 3; //npc option 1
		PACKET_SIZES[212] = 3; //npc option 2
		PACKET_SIZES[52] = 3; //npc option trade
		PACKET_SIZES[202] = 2; //npc examine
		PACKET_SIZES[233] = 13; //Character Design
		PACKET_SIZES[114] = 8; //shop value
		PACKET_SIZES[158] = 8; //shop option 1
		PACKET_SIZES[122] = 8; //shop option 5
		PACKET_SIZES[215] = 8; //shop option 10
		PACKET_SIZES[238] = 15; // item on object
		PACKET_SIZES[64] = 4; //enter amount
		PACKET_SIZES[166] = 7; //object option 1
		PACKET_SIZES[5] = 7; // pickup item
		PACKET_SIZES[183] = 8; // drop item
		PACKET_SIZES[129] = 5; //resize packet
		PACKET_SIZES[239] = 3;//follow player
		PACKET_SIZES[39] = 3;//challenge player
		PACKET_SIZES[156] = 13; //spell on object
		PACKET_SIZES[188] = 7; //object option 2
		PACKET_SIZES[218] = 7; //object option 3
		PACKET_SIZES[101] = 2; //object examine
		PACKET_SIZES[150] = 9; //magic on player
		PACKET_SIZES[49] = -1; // remove friend
		PACKET_SIZES[179] = -1; // add ignore
		PACKET_SIZES[180] = -1; // remove ignore
		PACKET_SIZES[0] = 8; // idk
		PACKET_SIZES[51] = 3; // Friends list status

		PACKET_SIZES[149] = 8; // item option 1
		PACKET_SIZES[194] = 8; // item option 2
		PACKET_SIZES[159] = 8; // item option 3
		PACKET_SIZES[245] = 8; // item option 4
		PACKET_SIZES[116] = 2; // item option 4
		PACKET_SIZES[46] = 8; // item option 5
		PACKET_SIZES[59] = 16; // move items
		PACKET_SIZES[6] = 9; // move items

		PACKET_SIZES[170] = 11; // item on player

		PACKET_SIZES[157] = -1; // set clan prefix
		PACKET_SIZES[241] = -1; // join clan chat

		PACKET_SIZES[2] = -1; // enter input
	}

	public Release83() {
		super(83, PacketMetaDataGroup.createFromArray(PACKET_SIZES));
		init();
	}

	/**
	 * Initialises this release by registering encoders and decoders.
	 */
	private void init() {
		register(RegionChangeMessage.class, new RegionChangeMessageEncoder());
		register(PlayerSynchronizationMessage.class, new PlayerSynchronizationMessageEncoder());
		register(NpcSynchronizationMessage.class, new NpcSynchronizationMessageEncoder());
		register(SendWindowPaneMessage.class, new SendWindowPaneEncoder());
		register(OpenInterfaceMessage.class, new OpenInterfaceMessageEncoder());
		register(CloseInterfaceMessage.class, new CloseInterfaceMessageEncoder());
		register(AccessMaskMessage.class, new AccessMaskMessageEncoder());

		register(UpdateSkillMessage.class, new UpdateSkillMessageEncoder());
		register(UpdateItemsMessage.class, new UpdateItemsMessageEncoder());
		register(UpdateRunEnergyMessage.class, new UpdateRunEnergyEncoder());
		register(ServerChatMessage.class, new ServerMessageMessageEncoder());
		register(EnterAmountMessage.class, new EnterAmountMessageEncoder());
		register(CloseEnterAmountMessage.class, new CloseEnterAmountMessageEncoder());
		register(SetWidgetVisibilityMessage.class, new SetWidgetVisibilityMessageEncoder());
		register(ConfigMessage.class, new ConfigMessageEncoder());
		register(ToggleMouseZoomMessage.class, new ToggleMouseZoomMessageEncoder());
		register(SetPlayerActionMessage.class, new SetPlayerActionMessageEncoder());

		register(GroupedRegionUpdateMessage.class, new GroupedRegionUpdateMessageEncoder(this));
		register(SendObjectMessage.class, new SendObjectMessageEncoder());
		register(RemoveObjectMessage.class, new RemoveObjectMessageEncoder());
		register(SendTileItemMessage.class, new SendTileItemMessageEncoder());
		register(RemoveTileItemMessage.class, new RemoveTileItemMessageEncoder());

		WalkMessageDecoder walkingDecoder = new WalkMessageDecoder();
		register(177, walkingDecoder);
		register(161, walkingDecoder);

		register(55, new PublicChatMessageDecoder());
		register(129, new ScreenResizeMessageDecoder());
		register(235, new CommandMessageDecoder());

		ItemOptionMessageDecoder itemOptionMessageDecoder = new ItemOptionMessageDecoder();
		register(198, itemOptionMessageDecoder);

		register(32, new EquipItemMessageDecoder());

		register(183, new DropItemMessageDecoder());
		register(5, new TakeTileItemMessageDecoder());

		EnteredAmountMessageDecoder enteredAmountMessageDecoder = new EnteredAmountMessageDecoder();
		register(64, enteredAmountMessageDecoder);

		ButtonMessageDecoder buttonMessageDecoder = new ButtonMessageDecoder();
		register(149, buttonMessageDecoder);
		register(255, buttonMessageDecoder);
		register(194, buttonMessageDecoder);
		register(159, buttonMessageDecoder);
		register(148, buttonMessageDecoder);
		register(0, buttonMessageDecoder);
		register(245, buttonMessageDecoder);
		register(77, buttonMessageDecoder);
		register(153, buttonMessageDecoder);

		ObjectActionMessageDecoder objectActionMessageDecoder = new ObjectActionMessageDecoder();
		register(166, objectActionMessageDecoder);

		PlayerActionMessageDecoder playerActionMessageDecoder = new PlayerActionMessageDecoder();
		register(111, playerActionMessageDecoder);
	}
}
