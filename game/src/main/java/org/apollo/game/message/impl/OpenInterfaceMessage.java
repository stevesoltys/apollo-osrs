package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that opens an interface.
 *
 * @author Graham
 */
public final class OpenInterfaceMessage extends Message {

	private int layerId;
	private int windowId;
	private int targetInterfaceId;
	private boolean walkable;

	/**
	 * Creates the event with the specified interface id.
	 *
	 * @param layerId The layer.
	 * @param windowId The window id.
	 * @param targetInterfaceId The interface id.
	 * @param walkable Whether or not the player can walk with this interface
	 * open.
	 */
	public OpenInterfaceMessage(int layerId, int windowId, int targetInterfaceId, boolean walkable) {
		this.layerId = layerId;
		this.windowId = windowId;
		this.targetInterfaceId = targetInterfaceId;
		this.walkable = walkable;
	}

	/**
	 * Creates the event with the specified interface id.
	 *
	 * @param layerId The layer.
	 * @param windowId The window id.
	 * @param targetInterfaceId The interface id.
	 */
	public OpenInterfaceMessage(int layerId, int windowId, int targetInterfaceId) {
		this.layerId = layerId;
		this.windowId = windowId;
		this.targetInterfaceId = targetInterfaceId;
		this.walkable = false;
	}

	public int getLayerId() {
		return layerId;
	}

	public int getWindowId() {
		return windowId;
	}

	public int getTargetInterfaceId() {
		return targetInterfaceId;
	}

	public boolean isWalkable() {
		return walkable; //XXX: fix
	}
}