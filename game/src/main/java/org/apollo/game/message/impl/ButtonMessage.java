package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player clicks a button.
 *
 * @author Graham
 */
public final class ButtonMessage extends Message {

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
	 * Creates the button message.
	 *
	 * @param interfaceId The interface id.
	 */
	public ButtonMessage(int interfaceId, int button, int childButton) {
		this.interfaceId = interfaceId;
		this.button = button;
		this.childButton = childButton;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	public int getButton() {
		return button;
	}

	public int getChildButton() {
		return childButton;
	}
}