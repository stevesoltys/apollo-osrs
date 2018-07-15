package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.EquipmentConstants;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.InterfaceConstants;
import org.apollo.game.model.inv.Inventory;

/**
 * A {@link MessageHandler} that removes equipped items.
 *
 * @author Graham
 * @author Major
 */
public final class RemoveEquippedItemHandler extends MessageHandler<ButtonMessage> {

	/**
	 * Creates the RemoveEquippedItemHandler.
	 *
	 * @param world The {@link World} the {@link ButtonMessage} occurred in.
	 */
	public RemoveEquippedItemHandler(World world) {
		super(world);
	}

	/**
	 * Converts a button to a slot.
	 *
	 * @param button The button.
	 * @return The slot.
	 * @throws IllegalArgumentException If the option is invalid.
	 */
	private static int buttonToSlot(int button) {

		switch (button) {
			case 6:
				return EquipmentConstants.HAT;
			case 7:
				return EquipmentConstants.CAPE;
			case 8:
				return EquipmentConstants.AMULET;
			case 9:
				return EquipmentConstants.WEAPON;
			case 10:
				return EquipmentConstants.CHEST;
			case 11:
				return EquipmentConstants.SHIELD;
			case 12:
				return EquipmentConstants.LEGS;
			case 13:
				return EquipmentConstants.HANDS;
			case 14:
				return EquipmentConstants.FEET;
			case 15:
				return EquipmentConstants.RING;
			case 16:
				return EquipmentConstants.ARROWS;
		}

		return -1;
	}

	@Override
	public void handle(Player player, ButtonMessage message) {
		if (message.getIndex() == 1 && message.getInterfaceId() == InterfaceConstants.EQUIPMENT_ID) {
			Inventory inventory = player.getInventory();
			Inventory equipment = player.getEquipment();

			int slot = buttonToSlot(message.getButton());

			if (slot == -1) {
				message.terminate();
				return;
			}

			Item item = equipment.get(slot);

			if (item == null) {
				message.terminate();
				return;
			}

			if (inventory.freeSlots() == 0 && !item.getDefinition().isStackable()) {
				inventory.forceCapacityExceeded();
				message.terminate();
				return;
			}

			int id = item.getId();
			boolean removed;

			inventory.stopFiringEvents();
			equipment.stopFiringEvents();

			try {
				int remaining = inventory.add(id, item.getAmount());
				removed = remaining == 0;
				equipment.set(slot, removed ? null : new Item(id, remaining));
			} finally {
				inventory.startFiringEvents();
				equipment.startFiringEvents();
			}

			if (removed) {
				inventory.forceRefresh();
				equipment.forceRefresh(slot);
			} else {
				inventory.forceCapacityExceeded();
			}
		}
	}

}