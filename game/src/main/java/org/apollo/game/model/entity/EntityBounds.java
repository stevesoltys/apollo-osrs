package org.apollo.game.model.entity;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;

import java.util.Collections;
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
	 * Creates an EntityBounds.
	 *
	 * @param entity The entity.
	 */
	protected EntityBounds(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Gets the set of positions from which this entity can be interacted with.
	 *
	 * @return The set of interaction positions.
	 */
	public Set<Position> getInteractionPositions() {
		return Collections.emptySet();
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
		int height = entity.getLength();

		return position.getX() >= entityPosition.getX() && position.getX() < entityPosition.getX() + width &&
			position.getY() >= entityPosition.getY() && position.getY() < entityPosition.getY() + height &&
			position.getHeight() == entityPosition.getHeight();
	}

	/**
	 * Gets the set of positions that the Entity occupies.
	 * <p>
	 * If the width, height, or Position of the entity has changed since the last call, it will calculate a new
	 * positions set before returning.
	 *
	 * @return The set of positions.
	 */
	public Set<Position> getPositions() {
		Set<Position> positionSet = new HashSet<>();

		for (int deltaX = 0; deltaX < entity.getWidth(); deltaX++) {
			for (int deltaY = 0; deltaY < entity.getLength(); deltaY++) {
				positionSet.add(entity.getPosition().step(deltaX, Direction.EAST).step(deltaY, Direction.NORTH));
			}
		}

		return positionSet;
	}
}
