package org.apollo.game.model.inter;

/**
 * Contains interface-related constants.
 *
 * @author Major
 */
public class InterfaceConstants {

	public static final int EQUIPMENT_ID = 387;

	/**
	 * The layer and interface ids of the side bar tabs.
	 * Interesting note - the layers are also considered screen areas.
	 */
	public enum UserInterface {

		CHAT_BOX(548, 21, 162),
		ORBS(548, 9, 160),
		ATTACK_STYLE(548, 63, 593),
		SKILLS(548, 64, 320),
		QUESTS(548, 65, 399),
		INVENTORY(548, 66, 149),
		EQUIPMENT(548, 67, EQUIPMENT_ID),
		PRAYER(548, 68, 541),
		SPELLBOOK(548, 69, 218),
		CLAN_CHAT(548, 70, 589),
		FRIENDS(548, 71, 429),
		IGNORES(548, 72, 432),
		LOGOUT(548, 73, 182),
		SETTINGS(548, 74, 261),
		EMOTES(548, 75, 216),
		MUSIC(548, 76, 239),

		CHAT_BOX_RESIZABLE_RESIZABLE(161, 22, 162),
		ORBS_RESIZABLE(161, 15, 160),
		ATTACK_STYLE_RESIZABLE(161, 61, 593),
		SKILLS_RESIZABLE(161, 62, 320),
		QUESTS_RESIZABLE(161, 63, 399),
		INVENTORY_RESIZABLE(161, 64, 149),
		EQUIPMENT_RESIZABLE(161, 65, EQUIPMENT_ID),
		PRAYER_RESIZABLE(161, 66, 541),
		SPELLBOOK_RESIZABLE(161, 67, 218),
		CLAN_CHAT_RESIZABLE(161, 68, 589),
		FRIENDS_RESIZABLE(161, 69, 429),
		IGNORES_RESIZABLE(161, 70, 432),
		LOGOUT_RESIZABLE(161, 71, 182),
		SETTINGS_RESIZABLE(161, 72, 261),
		EMOTES_RESIZABLE(161, 73, 216),
		MUSIC_RESIZABLE(161, 74, 239),

		CHAT_BOX_RESIZABLE_REARRANGED(164, 24, 162),
		ORBS_REARRANGED(164, 21, 160),
		ATTACK_STYLE_REARRANGED(164, 59, 593),
		SKILLS_REARRANGED(164, 60, 320),
		QUESTS_REARRANGED(164, 61, 399),
		INVENTORY_REARRANGED(164, 62, 149),
		EQUIPMENT_REARRANGED(164, 63, EQUIPMENT_ID),
		PRAYER_REARRANGED(164, 64, 541),
		SPELLBOOK_REARRANGED(164, 65, 218),
		CLAN_CHAT_REARRANGED(164, 66, 589),
		FRIENDS_REARRANGED(164, 67, 429),
		IGNORES_REARRANGED(164, 68, 432),
		LOGOUT_REARRANGED(164, 69, 182),
		SETTINGS_REARRANGED(164, 70, 261),
		EMOTES_REARRANGED(164, 71, 216),
		MUSIC_REARRANGED(164, 72, 239);

		private final int windowId;

		private final int layer;

		private final int interfaceId;

		UserInterface(int windowId, int layer, int interfaceId) {
			this.windowId = windowId;
			this.layer = layer;
			this.interfaceId = interfaceId;
		}

		public int getWindowId() {
			return windowId;
		}

		public int getLayer() {
			return layer;
		}

		public int getInterfaceId() {
			return interfaceId;
		}

	}

	/**
	 * Private constructor to prevent instancing.
	 */
	private InterfaceConstants() {
	}
}