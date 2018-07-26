package org.apollo.game.message.handler;

import org.apollo.game.message.impl.WalkMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.WalkingQueue;
import org.apollo.game.model.entity.path.AStarPathfindingAlgorithm;
import org.apollo.game.model.entity.path.EuclideanHeuristic;

/**
 * A {@link MessageHandler} that handles {@link WalkMessage}s.
 *
 * @author Graham
 */
public final class WalkMessageHandler extends MessageHandler<WalkMessage> {

	/**
	 * The pathfinding algorithm.
	 */
	private final AStarPathfindingAlgorithm pathfindingAlgorithm;

	/**
	 * Creates the WalkMessageHandler.
	 *
	 * @param world The {@link World} the {@link WalkMessage} occurred in.
	 */
	public WalkMessageHandler(World world) {
		super(world);

		pathfindingAlgorithm = new AStarPathfindingAlgorithm(world.getCollisionManager(), new EuclideanHeuristic());
	}

	@Override
	public void handle(Player player, WalkMessage message) {
		WalkingQueue queue = player.getWalkingQueue();
		Position destination = new Position(message.getDestination().getX(), message.getDestination().getY(),
			player.getPosition().getHeight());

		if (!player.getPosition().isWithinDistance(destination, 20)) {
			message.terminate();
			return;
		}

		queue.addSteps(pathfindingAlgorithm.find(player.getPosition(), message.getDestination()));

		queue.setRunning(message.isRunning() || player.isRunning());
		player.getInterfaceSet().close();

		if (queue.size() > 0) {
			player.stopAction();
		}

		if (player.getInteractingMob() != null) {
			player.resetInteractingMob();
		}
	}

}