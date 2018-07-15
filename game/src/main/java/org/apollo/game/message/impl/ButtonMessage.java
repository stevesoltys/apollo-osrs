package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player clicks a button.
 *
 * @author Graham
 */
public final class ButtonMessage extends Message {

	/**
	 * The button menu index.
	 */
	private final int index;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The button id.
	 */
	private final int button;

	/**
	 * The child button id.
	 */
	private final int childButton;

	/**
	 * The item id.
	 */
	private final int itemId;

	/**
	 * Creates the button message.
	 *
	 * @param index       The index for thsi button message.
	 * @param interfaceId The interface id.
	 * @param button      The button id.
	 * @param itemId      The item id.
	 */
	public ButtonMessage(int index, int interfaceId, int button, int childButton, int itemId) {
		this.index = index;
		this.interfaceId = interfaceId;
		this.button = button;
		this.childButton = childButton;
		this.itemId = itemId;
	}

	public int getIndex() {
		return index;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getButton() {
		return button;
	}

	public int getChildButton() {
		return childButton;
	}

	public int getItemId() {
		return itemId;
	}
}