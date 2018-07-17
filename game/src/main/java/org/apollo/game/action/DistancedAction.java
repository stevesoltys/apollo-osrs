package org.apollo.game.action;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.Mob;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.path.AStarPathfindingAlgorithm;
import org.apollo.game.model.entity.path.EuclideanHeuristic;

import java.util.Deque;

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
	protected final boolean immediate;

	/**
	 * The position to distance check with.
	 */
	protected final Position position;

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
		super(0, true, mob);
		this.position = position;
		this.distance = distance;
		this.delay = delay;
		this.immediate = immediate;

		calculatePath();
	}

	@Override
	public final void execute() {
		if (reached) { // Don't check again in case the player has moved away since it was reached
			executeAction();

		} else if (mob.getPosition().isWithinDistance(position, distance) && mob.getWalkingQueue().size() == 0) {
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

	private void calculatePath() {
		AStarPathfindingAlgorithm pathfindingAlgorithm = new AStarPathfindingAlgorithm(
			mob.getWorld().getCollisionManager(), new EuclideanHeuristic());

		if (mob.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) mob;

			Deque<Position> bestPath = pathfindingAlgorithm.find(player.getPosition(), position);
			Position bestPosition = position;

			for (int i = 0; i < distance; i++) {
				for (Direction direction : Direction.NESW) {
					Position currentPosition = position.step(distance, direction);
					Deque<Position> currentPath = pathfindingAlgorithm.find(player.getPosition(), currentPosition);

						if (!currentPath.isEmpty() && currentPath.size() <= bestPath.size()) {

						if(currentPath.size() == bestPath.size() && bestPosition == position) {
							continue;
						}

						bestPath = currentPath;
						bestPosition = currentPosition;
					}
				}
			}

			player.getWalkingQueue().addSteps(bestPath);
			player.getWalkingQueue().setRunning(player.isRunning());
		}

		// TODO: NPC distanced actions
	}
}
