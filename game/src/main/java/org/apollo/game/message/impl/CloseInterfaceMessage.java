package org.apollo.game.message.impl;

import org.apollo.game.model.inter.InterfaceConstants;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that closes the open interface.
 *
 * @author Graham
 */
public final class CloseInterfaceMessage extends Message {

	private int windowPane;

	private InterfaceConstants.ScreenArea area;

	public CloseInterfaceMessage(int windowPane, InterfaceConstants.ScreenArea area) {
		this.windowPane = windowPane;
		this.area = area;
	}

	public CloseInterfaceMessage() {
	}

	public int getWindowPane() {
		return windowPane;
	}

	public InterfaceConstants.ScreenArea getArea() {
		return area;
	}
}