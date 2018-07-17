package org.apollo.game.message.impl;

import java.util.OptionalInt;

/**
 * An {@link InventoryItemMessage} sent by the client when an item's drop or destroy option is clicked.
 *
 * @author Chris Fletcher
 */
public final class DropItemMessage extends InventoryItemMessage {

	/**
	 * Creates the ItemOptionMessage.
	 *
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public DropItemMessage(int interfaceId, int id, int slot) {
		super(OptionalInt.empty(), interfaceId, id, slot);
	}

}