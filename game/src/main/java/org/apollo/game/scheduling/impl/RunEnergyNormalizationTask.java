package org.apollo.game.scheduling.impl;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.Skill;
import org.apollo.game.model.entity.attr.AttributeDefinition;
import org.apollo.game.model.entity.attr.AttributeMap;
import org.apollo.game.model.entity.attr.AttributePersistence;
import org.apollo.game.model.entity.attr.NumericalAttribute;
import org.apollo.game.scheduling.ScheduledTask;

/**
 * A {@link ScheduledTask} which normalizes the run energy of a player.
 *
 * @author Graham
 */
public final class RunEnergyNormalizationTask extends ScheduledTask {

	static {
		AttributeMap.define("last_run_energy_restore", AttributeDefinition.forLong(0, AttributePersistence.PERSISTENT));
	}

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the skill normalization task.
	 *
	 * @param player The player.
	 */
	public RunEnergyNormalizationTask(Player player) {
		super(1, false);
		this.player = player;
	}

	@Override
	public void execute() {
		if (!player.isActive()) {
			stop();
			return;
		}

		int runEnergy = player.getRunEnergy();

		if (runEnergy < 100) {
			Number lastRecoveryAttribute = (Number) player.getAttribute("last_run_energy_restore").getValue();
			long lastRecovery = lastRecoveryAttribute.longValue();

			if (lastRecovery + getEnergyRestoreRate(player) < System.currentTimeMillis()) {
				player.setRunEnergy(runEnergy + 1);
				player.setAttribute("last_run_energy_restore", new NumericalAttribute(System.currentTimeMillis()));
			}
		}
	}

	private double getEnergyRestoreRate(Player player) {
		return 2260 - (player.getSkillSet().getCurrentLevel(Skill.AGILITY) * 10);
	}

}