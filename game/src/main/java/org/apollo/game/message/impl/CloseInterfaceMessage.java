package org.apollo.game.message.impl;

import org.apollo.game.model.inter.ScreenArea;
import org.apollo.game.model.inter.WindowPane;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that closes the open interface.
 *
 * @author Graham
 */
public final class CloseInterfaceMessage extends Message {

	private WindowPane windowPane;

	private ScreenArea area;

	public CloseInterfaceMessage(WindowPane windowPane, ScreenArea area) {
		this.windowPane = windowPane;
		this.area = area;
	}

	public CloseInterfaceMessage() {
	}

	public WindowPane getWindowPane() {
		return windowPane;
	}

	public ScreenArea getArea() {
		return area;
	}
}