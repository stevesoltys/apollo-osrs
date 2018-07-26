package org.apollo.game.message.impl;

import org.apollo.game.model.Position;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to request that the player walks somewhere.
 *
 * @author Graham
 */
public final class WalkMessage extends Message {

	/**
	 * The running flag.
	 */
	private final boolean run;

	/**
	 * The destination.
	 */
	private Position destination;

	/**
	 * Creates the message.
	 *
	 * @param destination The destination.
	 * @param run         The run flag.
	 */
	public WalkMessage(Position destination, boolean run) {
		this.destination = destination;
		this.run = run;
	}

	/**
	 * Gets the destination.
	 *
	 * @return The destination.
	 */
	public Position getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 *
	 * @param destination The destination.
	 */
	public void setDestination(Position destination) {
		this.destination = destination;
	}

	/**
	 * Checks if the steps should be ran (ctrl+click).
	 *
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isRunning() {
		return run;
	}

}