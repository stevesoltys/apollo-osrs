package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.game.message.impl.InventoryItemMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link MessageHandler} that verifies {@link ButtonMessage}s involving items.
 *
 * @author Chris Fletcher
 * @author Major
 */
public final class ItemButtonVerificationHandler extends MessageHandler<ButtonMessage> {

	/**
	 * A supplier for an {@link Inventory}.
	 *
	 * @author Major
	 */
	@FunctionalInterface
	public interface InventorySupplier {

		/**
		 * Gets the appropriate {@link Inventory}.
		 *
		 * @param player The {@link Player} who prompted the verification call.
		 * @return The inventory, or {@code null} to immediately fail verification.
		 */
		Inventory getInventory(Player player);

	}

	/**
	 * The map of interface ids to inventories.
	 */
	private static final Map<Integer, InventorySupplier> inventories = new HashMap<>();

	static {
		inventories.put(SynchronizationInventoryListener.INVENTORY_ID, Player::getInventory);
		inventories.put(BankConstants.SIDEBAR_ID, Player::getInventory);
		inventories.put(BankConstants.WINDOW_ID, Player::getBank);
	}

	/**
	 * Adds an {@link Inventory} with the specified interface id to the {@link Map} of supported ones,
	 * <strong>iff</strong> the specified id does <strong>not</strong> already have a mapping.
	 *
	 * @param id The id of the interface.
	 * @param supplier The {@link InventorySupplier}.
	 */
	public static void addInventory(int id, InventorySupplier supplier) {
		inventories.putIfAbsent(id, supplier);
	}

	/**
	 * Creates the ItemVerificationHandler.
	 *
	 * @param world The {@link World} the {@link InventoryItemMessage} occurred in.
	 */
	public ItemButtonVerificationHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ButtonMessage message) {
		InventorySupplier supplier = inventories.get(message.getInterfaceId());

		if (supplier == null) {
			return;
		}

		Inventory inventory = supplier.getInventory(player);

		int slot = message.getChildButton();
		if (inventory == null || slot < 0 || slot >= inventory.capacity()) {
			message.terminate();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != message.getItemId()) {
			message.terminate();
		}
	}

}