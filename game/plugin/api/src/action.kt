package org.apollo.game.plugin.api

import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedEntityAction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Entity
import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.Player

class AsyncDistancedActionProxy(mob: Mob, delay: Int, immediate: Boolean, val interactionPositions: Set<Position>,
                                val block: ActionBlock) : AsyncDistancedEntityAction<Mob>(delay, immediate, mob) {

    override fun action(): ActionBlock = block

    override fun getTriggerPositions(): Set<Position> {
        return interactionPositions
    }
}

fun Player.startDistancedAction(entity: Entity, delay: Int = 0, immediate: Boolean = false, block: ActionBlock) {
    walkToClosest(entity.bounds.interactionPositions, smart = true)

    startAction(AsyncDistancedActionProxy(this, delay, immediate, entity.bounds.interactionPositions, block))
}

fun Player.startDistancedAction(triggerPositions: Set<Position>, delay: Int = 0, immediate: Boolean = false,
                                block: ActionBlock) {
    walkToClosest(triggerPositions, smart = true)

    startAction(AsyncDistancedActionProxy(this, delay, immediate, triggerPositions, block))
}
