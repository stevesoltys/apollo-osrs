package org.apollo.game.message.impl;

import org.apollo.game.model.inv.SlottedItem;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that updates a single item in an interface.
 *
 * @author Graham
 */
public final class UpdateSlottedItemMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The child interface id.
	 */
	private final int childId;

	/**
	 * The inventory type.
	 */
	private final int type;

	/**
	 * The slotted item.
	 */
	private final SlottedItem item;

	/**
	 * Creates the update item in interface message.
	 *  @param interfaceId The interface id.
	 * @param childId
	 * @param item       The slotted item.
	 */
	public UpdateSlottedItemMessage(int interfaceId, int childId, int type, SlottedItem item) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.item = item;
		this.type = type;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the child interface id.
	 *
	 * @return The child interface id.
	 */
	public int getChildId() {
		return childId;
	}

	/**
	 * Gets the inventory type.
	 *
	 * @return The inventory type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the slotted item.
	 *
	 * @return The slotted item.
	 */
	public SlottedItem getSlottedItem() {
		return item;
	}
}