package org.apollo.game.action;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.collision.CollisionUpdate;
import org.apollo.game.model.area.collision.CollisionUpdateType;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.Mob;
import org.apollo.game.model.entity.obj.GameObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An @{link Action} which fires when a distance requirement is met.
 *
 * @param <T> The type of {@link Mob}.
 * @author Blake
 * @author Graham
 * @author Steve Soltys
 */
public abstract class EntityDistancedAction<T extends Mob> extends Action<T> {

	/**
	 * The delay once the threshold is reached.
	 */
	protected final int delay;

	/**
	 * A flag indicating if this action fires immediately after the threshold is reached.
	 */
	private final boolean immediate;

	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * The trigger positions.
	 */
	private Set<Position> triggerPositions;

	/**
	 * A flag indicating if the distance has been reached yet.
	 */
	private boolean reached;

	/**
	 * Creates a new EntityDistancedAction.
	 *
	 * @param delay     The delay between executions once the distance threshold is reached.
	 * @param immediate Whether or not this action fires immediately after the distance threshold is reached.
	 * @param mob       The mob.
	 * @param entity    The entity that we are approaching.
	 */
	public EntityDistancedAction(int delay, boolean immediate, T mob, Entity entity) {
		super(0, true, mob);
		this.delay = delay;
		this.immediate = immediate;
		this.entity = entity;
	}

	@Override
	public final void execute() {

		if(triggerPositions == null || entity instanceof Mob) {
			triggerPositions = getTriggerPositions();
		}

		if (reached) { // Don't check again in case the player has moved away since it was reached
			executeAction();
			// TODO checking the walking queue size is a really cheap fix, and relies on the client not
			// being edited... this class needs to be completely re-written.
		} else if (withinDistance() && mob.getWalkingQueue().size() == 0) {
			reached = true;
			setDelay(delay);
			if (immediate) {
				executeAction();
			}
		}
	}

	/**
	 * Executes the actual action. Called when the distance requirement is met.
	 */
	protected abstract void executeAction();

	/**
	 * Checks whether or not the mob is within distance of any of the object.
	 *
	 * @return A flag indicating whether or not the mob is within distance.
	 */
	private boolean withinDistance() {
		return triggerPositions.stream().anyMatch(position -> mob.getPosition().equals(position));
	}

	/**
	 * Gets the set of positions that will trigger this action.
	 *
	 * @return The set of trigger positions.
	 */
	protected Set<Position> getTriggerPositions() {
		if (entity instanceof GameObject) {
			return getObjectTriggerPositions();

		} else if (entity instanceof Mob) {
			return getMobTriggerPositions();
		}

		return Collections.emptySet();
	}

	protected Set<Position> getObjectTriggerPositions() {
		GameObject gameObject = (GameObject) entity;
		Set<Position> positions = new HashSet<>();

		CollisionUpdate.Builder builder = new CollisionUpdate.Builder();
		builder.type(CollisionUpdateType.ADDING);
		builder.object(gameObject);
		builder.build();

		builder.build().getFlags().entries().forEach(entry -> {
			Position position = entry.getKey();
			CollisionUpdate.DirectionFlag flag = entry.getValue();
			Direction direction = flag.getDirection();

			positions.add(position.step(1, direction));

			if(gameObject.getType() == 9) {
				positions.add(position.step(1, direction.clockwise().clockwise().clockwise()));
				positions.add(position.step(1, direction.counterClockwise().counterClockwise().counterClockwise()));

			} else {
				if (traversable(position.step(1, direction.clockwise()), direction.clockwise().opposite())) {
					positions.add(position.step(1, direction.clockwise()));
				}

				if (traversable(position.step(1, direction.clockwise().clockwise()),
					direction.clockwise().clockwise().opposite())) {
					positions.add(position.step(1, direction.clockwise().clockwise()));
				}

				if (traversable(position.step(1, direction.counterClockwise()), direction.counterClockwise().opposite())) {
					positions.add(position.step(1, direction.counterClockwise()));
				}

				if (traversable(position.step(1, direction.counterClockwise().counterClockwise()),
					direction.counterClockwise().counterClockwise().opposite())) {
					positions.add(position.step(1, direction.counterClockwise().counterClockwise()));
				}
			}
		});

		for (Position entityPosition : gameObject.getBounds()) {
			if (!gameObject.getDefinition().isObstructive()) {
				positions.add(entityPosition);
			}
		}

		return positions;
	}

	private Set<Position> getMobTriggerPositions() {
		Set<Position> positions = new HashSet<>();

		entity.getBounds().forEach(position ->
			Arrays.stream(Direction.values())
				.filter(direction -> direction != Direction.NONE)
				.forEach(direction -> positions.add(position.step(1, direction))));

		return positions;
	}

	private boolean traversable(Position position, Direction direction) {
		return mob.getWorld().getCollisionManager().traversable(position, mob.getEntityType(), direction);
	}
}
