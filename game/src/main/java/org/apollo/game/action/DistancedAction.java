package org.apollo.game.action;

import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Mob;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An @{link Action} which fires when a distance requirement is met.
 *
 * @param <T> The type of {@link Mob}.
 * @author Blake
 * @author Graham
 */
public abstract class DistancedAction<T extends Mob> extends Action<T> {

	/**
	 * The delay once the threshold is reached.
	 */
	protected final int delay;

	/**
	 * The minimum distance before the action fires.
	 */
	protected final int distance;

	/**
	 * A flag indicating if this action fires immediately after the threshold is reached.
	 */
	private final boolean immediate;

	/**
	 * The positions to distance check with.
	 */
	protected final Set<Position> positions;

	/**
	 * A flag indicating if the distance has been reached yet.
	 */
	private boolean reached;

	/**
	 * Creates a new DistancedAction.
	 *
	 * @param delay     The delay between executions once the distance threshold is reached.
	 * @param immediate Whether or not this action fires immediately after the distance threshold is reached.
	 * @param mob       The mob.
	 * @param position  The position.
	 * @param distance  The distance.
	 */
	public DistancedAction(int delay, boolean immediate, T mob, Position position, int distance) {
		this(delay, immediate, mob, Collections.singleton(position), distance);
	}

	/**
	 * Creates a new DistancedAction.
	 *
	 * @param delay     The delay between executions once the distance threshold is reached.
	 * @param immediate Whether or not this action fires immediately after the distance threshold is reached.
	 * @param mob       The mob.
	 * @param positions The positions.
	 * @param distance  The distance.
	 */
	public DistancedAction(int delay, boolean immediate, T mob, Set<Position> positions, int distance) {
		super(0, true, mob);
		this.positions = new HashSet<>(positions);
		this.distance = distance;
		this.delay = delay;
		this.immediate = immediate;
	}

	@Override
	public final void execute() {
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
	 * Checks whether or not the mob is within distance of any of the required positions.
	 *
	 * @return A flag indicating whether or not the mob is within distance.
	 */
	private boolean withinDistance() {
		return positions.stream().anyMatch(position -> mob.getPosition().isWithinDistance(position, distance));
	}
}
