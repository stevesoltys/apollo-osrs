package org.apollo.game.model.inv;

import org.apollo.game.message.impl.UpdateItemsMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;

/**
 * An {@link InventoryListener} which synchronizes the state of the server's inventory with the client's.
 *
 * @author Graham
 */
public final class SynchronizationInventoryListener extends InventoryAdapter {

	/**
	 * The inventory interface id.
	 */
	public static final int INVENTORY_ID = 149;

	/**
	 * The inventory interface child id.
	 */
	public static final int INVENTORY_CHILD_ID = 0;

	/**
	 * The inventory interface type id.
	 */
	public static final int INVENTORY_TYPE_ID = 93;

	/**
	 * The equipment interface id.
	 */
	public static final int EQUIPMENT_ID = -1;

	/**
	 * The equipment interface child id.
	 */
	public static final int EQUIPMENT_CHILD_ID = 64208;

	/**
	 * The equipment interface type id.
	 */
	public static final int EQUIPMENT_TYPE_ID = 94;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The child id.
	 */
	private final int childId;

	/**
	 * The inventory type.
	 */
	private final int type;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the synchronization inventory listener.
	 *
	 * @param player      The player.
	 * @param interfaceId The interface id.
	 * @param childId     The child interface id.
	 * @param type        The inventory type.
	 */
	public SynchronizationInventoryListener(Player player, int interfaceId, int childId, int type) {
		this.player = player;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.type = type;
	}

	/**
	 * Creates the synchronization inventory listener. This defaults 'type' and 'slot' to values of -1.
	 *
	 * @param player      The player.
	 * @param interfaceId The interface id.
	 */
	public SynchronizationInventoryListener(Player player, int interfaceId) {
		this(player, interfaceId, -1, -1);
	}

	@Override
	public void itemsUpdated(Inventory inventory) {
		player.send(new UpdateItemsMessage(interfaceId, childId, type, inventory.getItems()));
	}


	@Override
	public void itemUpdated(Inventory inventory, int slot, Item item) {
		player.send(new UpdateItemsMessage(interfaceId, childId, type, inventory.getItems()));
//		player.send(new UpdateSlottedItemMessage(interfaceId, childId, type, new SlottedItem(slot, item)));
		// TODO: Write slotted item message encoder.
	}

}