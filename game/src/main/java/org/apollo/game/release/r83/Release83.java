package org.apollo.game.release.r83;

import org.apollo.game.message.impl.RegionChangeMessage;
import org.apollo.game.message.impl.SetWidgetPaneMessage;
import org.apollo.net.meta.PacketMetaDataGroup;
import org.apollo.net.release.Release;

/**
 * @author Steve Soltys
 */
public class Release83 extends Release {

	/**
	 * The incoming packet lengths array.
	 */
	public static final int[] PACKET_LENGTHS = {
		0, 12, 0, 6, 6, 0, 0, 0, 2, 0, // 0
		0, 0, 0, 2, 0, 0, 0, 0, 0, 4, // 10
		0, 0, 2, 0, 6, 0, 0, 0, -1, 0, // 20
		0, 4, 0, 0, 0, 0, 8, 0, 0, 0, // 30
		0, 0, 2, 0, 0, 2, 0, 0, 0, -1, // 40
		6, 0, 0, 0, 6, 6, -1, 8, 0, 0, // 50
		0, 0, 0, 0, 0, 0, 0, 2, 0, 0, // 60
		0, 6, 0, 0, 0, 4, 0, 6, 4, 2, // 70
		2, 0, 0, 8, 0, 0, 0, 0, 0, 0, // 80
		0, 6, 0, 0, 0, 4, 0, 0, 0, 0, // 90
		6, 0, 0, 0, 4, 0, 0, 0, 4, 0, // 100
		0, 0, 2, 0, 0, 0, 2, 0, 0, 1, // 110
		8, 0, 0, 7, 0, 0, 1, 0, 0, 0, // 120
		0, 0, 0, -2, 0, 0, 6, 0, 0, 0, // 130
		4, 8, 0, 8, 0, 0, 0, 0, 0, 0, // 140
		0, 0, 12, 0, 0, 0, 0, 4, 6, 0, // 150
		8, 6, 0, 13, 0, 1, 0, 0, 0, 0, // 160
		0, -1, 0, 3, 0, 0, 3, 6, 0, 0, // 170
		0, 6, 0, 0, 10, 0, 0, 1, 0, 0, // 180
		0, 0, 0, 0, 2, 0, 0, 4, 0, 0, // 190
		0, 0, 0, 6, 0, 0, 8, 0, 0, 0, // 200
		8, 12, 0, -1, 0, 0, 0, 8, 0, 0, // 210
		0, 0, 3, 0, 0, 0, 2, 9, 6, 0, // 220
		6, 6, 0, 2, 0, 0, 0, 0, 0, 0, // 230
		0, 6, 0, 0, -1, 2, 0, -1, 0, 0, // 240
		0, 0, 0, 0, 0, 0 // 250
	};

	public Release83() {
		super(83, PacketMetaDataGroup.createFromArray(PACKET_LENGTHS));
		init();
	}

	/**
	 * Initialises this release by registering encoders and decoders.
	 */
	private void init() {
		register(RegionChangeMessage.class, new RegionChangeMessageEncoder());
//		register(IdAssignmentMessage.class, new IdAssignmentMessageEncoder());
		register(SetWidgetPaneMessage.class, new SetWidgetPaneMessageEncoder());
	}
}
