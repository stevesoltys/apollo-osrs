package org.apollo.game.message.impl;

import java.util.OptionalInt;

/**
 * @author Steve Soltys
 */
public class EquipItemMessage extends InventoryItemMessage {

	/**
	 * Creates the InventoryItemMessage.
	 *
	 * @param interfaceId The interface id.
	 * @param id          The id.
	 * @param slot        The slot.
	 */
	public EquipItemMessage(int interfaceId, int id, int slot) {
		super(OptionalInt.empty(), interfaceId, id, slot);
	}
}
