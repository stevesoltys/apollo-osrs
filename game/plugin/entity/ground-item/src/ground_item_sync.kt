import org.apollo.game.GameConstants
import org.apollo.game.model.entity.GroundItem
import org.apollo.game.scheduling.ScheduledTask

/**
 * A [ScheduledTask] that manages the globalization and expiration of [GroundItem]s.
 */
class GroundItemSynchronizationTask(private val groundItem: GroundItem) : ScheduledTask(DELAY, false) {

    companion object {

        /**
         * The delay between executions of this task, in pulses.
         */
        const val DELAY = 1

        /**
         * The amount of time, in pulses, in which this [GroundItem] will be globally visible.
         */
        const val TRADABLE_TIME_UNTIL_GLOBAL = 10000 / GameConstants.PULSE_DELAY

        /**
         * The amount of time, in pulses, in which this [GroundItem] will expire and be removed from the [World].
         */
        const val UNTRADABLE_TIME_UNTIL_EXPIRE = 180000 / GameConstants.PULSE_DELAY

        /**
         * The amount of time, in pulses, in which this [GroundItem] will expire and be removed from the [World].
         */
        const val TIME_UNTIL_EXPIRE = 180000 / GameConstants.PULSE_DELAY
    }

    /**
     * The amount of game pulses this [ScheduledTask] has been alive.
     */
    private var pulses = 0

    override fun execute() {
        val world = groundItem.world
        val owner = groundItem.owner
        val untradable = false // TODO: item.getDefinition().isTradable();

        if (!groundItem.isAvailable) {
            stop()
            return
        }

        // Untradable items never go global
//        if (untradable) {
//            if (pulses >= UNTRADABLE_TIME_UNTIL_EXPIRE) {
//                world.removeGroundItem(groundItem)
//                stop()
//            }
//            return
//        }

        if (groundItem.isGlobal) {
            if (pulses >= TIME_UNTIL_EXPIRE) {
                world.removeGroundItem(world, groundItem)
                stop()
            }
        } else {
            if (pulses >= TRADABLE_TIME_UNTIL_GLOBAL) {
                world.globalizeGroundItem(groundItem)
            }
        }

        pulses++
    }

}