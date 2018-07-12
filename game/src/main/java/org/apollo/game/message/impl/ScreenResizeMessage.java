package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when the player resizes the screen.
 *
 * @author Steve Soltys
 */
public final class ScreenResizeMessage extends Message {

	private final int mode;

	private final int width;

	private final int height;

	public ScreenResizeMessage(int mode, int width, int height) {
		this.mode = mode;
		this.width = width;
		this.height = height;
	}

	public int getMode() {
		return mode;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}