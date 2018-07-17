package org.apollo.game.message.impl;

import org.apollo.game.model.entity.GroundItem;
import org.apollo.game.model.entity.Player;
import org.apollo.net.message.Message;

import java.util.Objects;

/**
 * A {@link Message} sent to the client to display an item on a tile for every player.
 *
 * @author Major
 */
public final class SendTileItemMessage extends RegionUpdateMessage {

	/**
	 * The ground item.
	 */
	private final GroundItem groundItem;

	/**
	 * The position offset
	 */
	private final int positionOffset;

	/**
	 * Creates the SendPublicTileItemMessage.
	 *
	 * @param item           The item to add to the tile.
	 * @param owner          The player who dropped the item.
	 * @param global         A flag indicating whether or not the item is global.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public SendTileItemMessage(GroundItem groundItem, int positionOffset) {
		this.groundItem = groundItem;
		this.positionOffset = positionOffset;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SendTileItemMessage that = (SendTileItemMessage) o;
		return positionOffset == that.positionOffset &&
			Objects.equals(groundItem, that.groundItem);
	}

	@Override
	public int hashCode() {
		return Objects.hash(groundItem, positionOffset);
	}

	/**
	 * Gets the amount of the item.
	 *
	 * @return The amount.
	 */
	public int getAmount() {
		return groundItem.getItem().getAmount();
	}

	/**
	 * Gets the id of the item.
	 *
	 * @return The id.
	 */
	public int getId() {
		return groundItem.getItem().getId();
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
	public int priority() {
		return LOW_PRIORITY;
	}

	@Override
	public boolean visibleTo(Player player) {
		return groundItem.isGlobal() ||
			(groundItem.getOwner() != null && player.getEncodedName() == groundItem.getOwner().getEncodedName());
	}
}