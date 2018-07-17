package org.apollo.game.sync.task;

import org.apollo.game.model.Position;
import org.apollo.game.model.area.RegionCoordinates;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.Npc;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the specified npc.
 *
 * @author Major
 */
public final class PreNpcSynchronizationTask extends SynchronizationTask {

	/**
	 * The npc.
	 */
	private final Npc npc;

	/**
	 * Creates the {@link PreNpcSynchronizationTask} for the specified npc.
	 *
	 * @param npc The npc.
	 */
	public PreNpcSynchronizationTask(Npc npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		Position old = npc.getPosition();
		npc.getWorld().getCollisionManager().build(npc.getWorld().getRegionRepository()
			.get(old.getRegionCoordinates()));

		npc.getWalkingQueue().pulse();

		Position position = npc.getPosition();
		RegionRepository repository = npc.getWorld().getRegionRepository();
		Set<RegionCoordinates> oldViewable = repository.fromPosition(old).getSurrounding();
		Set<RegionCoordinates> newViewable = repository.fromPosition(position).getSurrounding();

		Set<RegionCoordinates> differences = new HashSet<>(newViewable);
		differences.retainAll(oldViewable);

		for (RegionCoordinates difference : differences) {
			npc.getWorld().getCollisionManager().build(npc.getWorld().getRegionRepository().get(difference));
		}
	}
}