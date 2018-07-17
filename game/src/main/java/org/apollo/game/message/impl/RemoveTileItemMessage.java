package org.apollo.game.message.impl;

import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.net.message.Message;

import java.util.Objects;

/**
 * A {@link Message} sent to the client to remove an item from a tile.
 *
 * @author Major
 */
public final class RemoveTileItemMessage extends RegionUpdateMessage {

	/**
	 * The id of the Item to remove.
	 */
	private final int id;

	/**
	 * The offset from the client's base position.
	 */
	private final int positionOffset;

	private final boolean global;

	private final Player owner;

	/**
	 * Creates the RemoveTileItemMessage.
	 *
	 * @param id The id of the {@link Item} to remove.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public RemoveTileItemMessage(int id, int positionOffset, boolean global, Player owner) {
		this.id = id;
		this.positionOffset = positionOffset;
		this.global = global;
		this.owner = owner;
	}

	/**
	 * Gets the id of the item to remove.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the offset from the 'base' position.
	 *
	 * @return The offset.
	 */
	public int getPositionOffset() {
		return positionOffset;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RemoveTileItemMessage that = (RemoveTileItemMessage) o;
		return id == that.id &&
			positionOffset == that.positionOffset &&
			global == that.global &&
			Objects.equals(owner, that.owner);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, positionOffset, global, owner);
	}

	@Override
	public int priority() {
		return HIGH_PRIORITY;
	}

	@Override
	public boolean visibleTo(Player player) {
		return global || (owner != null && owner.getEncodedName() == player.getEncodedName());
	}

}