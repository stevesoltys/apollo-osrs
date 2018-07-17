package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ObjectActionMessage;
import org.apollo.game.message.impl.WalkMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.WalkingQueue;
import org.apollo.game.model.entity.path.AStarPathfindingAlgorithm;
import org.apollo.game.model.entity.path.EuclideanHeuristic;

/**
 * A pathfinding {@link MessageHandler} for the {@link ObjectActionMessage}.
 *
 * @author Steve Soltys
 */
public class ObjectActionPathfindingHandler extends MessageHandler<ObjectActionMessage> {

	/**
	 * The pathfinding algorithm.
	 */
	private final AStarPathfindingAlgorithm pathfindingAlgorithm;

	/**
	 * Creates the WalkMessageHandler.
	 *
	 * @param world The {@link World} the {@link WalkMessage} occurred in.
	 */
	public ObjectActionPathfindingHandler(World world) {
		super(world);

		pathfindingAlgorithm = new AStarPathfindingAlgorithm(world.getCollisionManager(), new EuclideanHeuristic());
	}

	@Override
	public void handle(Player player, ObjectActionMessage message) {
		WalkingQueue queue = player.getWalkingQueue();

		queue.addSteps(pathfindingAlgorithm.find(player.getPosition(), message.getPosition()));
		queue.setRunning(player.isRunning());
	}
}
