package org.apollo.game.action;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
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
	 * A flag indicating if the distance has been reached yet.
	 */
	private boolean reached;

	/**
	 * Creates a new EntityDistancedAction.
	 *
	 * @param delay     The delay between executions once the distance threshold is reached.
	 * @param immediate Whether or not this action fires immediately after the distance threshold is reached.
	 * @param mob       The mob.
	 */
	public EntityDistancedAction(int delay, boolean immediate, T mob) {
		super(0, true, mob);
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
	 * Gets the set of positions that will trigger this action.
	 *
	 * @return The set of trigger positions.
	 */
	protected abstract Set<Position> getTriggerPositions();

	/**
	 * Checks whether or not the mob is within distance of the Entity.
	 *
	 * @return A flag indicating whether or not the mob is within distance.
	 */
	private boolean withinDistance() {
		return getTriggerPositions().stream().anyMatch(position -> mob.getPosition().equals(position));
	}
}
