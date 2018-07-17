package org.apollo.game.sync.seg;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.sync.block.SynchronizationBlockSet;

/**
 * A {@link SynchronizationSegment} which adds a player.
 *
 * @author Graham
 */
public final class AddPlayerSegment extends SynchronizationSegment {

	/**
	 * The index.
	 */
	private final int index;

	/**
	 * The position.
	 */
	private final Position position;

	/**
	 * The direction.
	 */
	private final Direction direction;

	/**
	 * Creates the add player segment.
	 *
	 * @param blockSet    The block set.
	 * @param index       The player's index.
	 * @param position    The position.
	 * @param direction   The direction.
	 */
	public AddPlayerSegment(SynchronizationBlockSet blockSet, int index, Position position, Direction direction) {
		super(blockSet);
		this.index = index;
		this.position = position;
		this.direction = direction;
	}

	/**
	 * Gets the player's index.
	 *
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the position.
	 *
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Gets the direction.
	 *
	 * @return The direction.
	 */
	public Direction getDirection() {
		return direction;
	}

	@Override
	public SegmentType getType() {
		return SegmentType.ADD_MOB;
	}
}