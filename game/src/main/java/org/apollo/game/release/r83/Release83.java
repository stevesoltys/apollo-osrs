package org.apollo.game.release.r83;

import org.apollo.game.message.impl.PlayerSynchronizationMessage;
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
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 0
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 10
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 20
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 30
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 40
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 50
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 60
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 70
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 80
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 90
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 100
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 110
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 120
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 130
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 140
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 150
		-3, -1, -3, -3, -3, -3, -3, -3, -3, -3, // 160
		-3, -3, -3, -3, -3, -3, -3, -1, -3, -3, // 170
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 180
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 190
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 200
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 210
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 220
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 230
		-3, -3, -3, -3, -3, -3, -3, -3, -3, -3, // 240
		-3, -3, -3, -3, -3, -3 // 250
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
		register(PlayerSynchronizationMessage.class, new PlayerSynchronizationMessageEncoder());
		register(SetWidgetPaneMessage.class, new SetWidgetPaneMessageEncoder());

		WalkMessageDecoder walkingDecoder = new WalkMessageDecoder();
		register(177, walkingDecoder);
		register(161, walkingDecoder);
	}
}
