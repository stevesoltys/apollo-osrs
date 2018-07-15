package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that changes the state of a widget component.
 *
 * @author Steve Soltys
 */
public class SetWidgetVisibilityMessage extends Message {

	private final int interfaceId;

	private final int id;

	private final boolean enabled;

	public SetWidgetVisibilityMessage(int interfaceId, int id, boolean enabled) {
		this.interfaceId = interfaceId;
		this.id = id;
		this.enabled = enabled;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public int getId() {
		return id;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
