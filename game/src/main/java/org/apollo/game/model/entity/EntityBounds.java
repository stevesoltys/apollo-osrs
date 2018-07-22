package org.apollo.game.model.entity;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * The bounds of an {@link Entity}.
 *
 * @author Steve Soltys
 */
public class EntityBounds {

	/**
	 * The {@link Entity}.
	 */
	private final Entity entity;

	/**
	 * The last known Position of the Entity.
	 */
	private Position position = new Position(0, 0);

	/**
	 * The last known width of the Entity.
	 */
	private int width = 0;

	/**
	 * The last known height of the Entity.
	 */
	private int height = 0;

	/**
	 * The set of positions that the Entity occupies.
	 */
	private Set<Position> positions = new HashSet<>();

	/**
	 * Creates an EntityBounds.
	 *
	 * @param entity The entity.
	 */
	EntityBounds(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Checks whether the given position is within the Entity's bounds.
	 *
	 * @param position The position.
	 * @return A flag indicating whether or not the position exists within the Entity's bounds.
	 */
	public boolean contains(Position position) {
		Position entityPosition = entity.getPosition();
		int width = entity.getWidth();
		int height = entity.getHeight();

		return position.getX() >= entityPosition.getX() && position.getX() < entityPosition.getX() + width &&
			position.getY() >= entityPosition.getY() && position.getY() < entityPosition.getY() + height &&
			position.getHeight() == entityPosition.getHeight();
	}

	/**
	 * Gets the set of positions that the Entity occupies.
	 * <p>
	 * If the width, height, or Position of the entity has changed since the last call, it will calculate the new set
	 * of positions.
	 *
	 * @return The set of positions.
	 */
	public Set<Position> getPositions() {

		if (entity.getPosition() == null) {
			positions.clear();
			return positions;
		}

		if (!position.equals(entity.getPosition()) || width != entity.getWidth() || height != entity.getHeight()) {
			position = entity.getPosition();
			width = entity.getWidth();
			height = entity.getHeight();

			positions.clear();

			for (int deltaX = 0; deltaX < width; deltaX++) {
				for (int deltaY = 0; deltaY < height; deltaY++) {
					positions.add(position.step(deltaX, Direction.EAST).step(deltaY, Direction.NORTH));
				}
			}
		}

		return positions;
	}
}
