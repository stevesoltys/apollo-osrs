
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.plugin.api.Definitions
import org.apollo.game.plugin.skills.mining.Ore
import org.apollo.net.message.Message
import java.util.*

class ProspectingAction(
    player: Player,
    val position: Position,
    private val ore: Ore
) : AsyncDistancedAction<Player>(DELAY, true, player, position, ORE_SIZE) {

    companion object {
        private const val DELAY = 3
        private const val ORE_SIZE = 1

        /**
         * Starts a [MiningAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: ObjectActionMessage, player: Player, ore: Ore) {
            val action = ProspectingAction(player, message.position, ore)
            player.startAction(action)

            message.terminate()
        }
    }

    override fun action(): ActionBlock = {
        mob.sendMessage("You examine the rock for ores...")
        mob.turnTo(position)

        wait()

        val oreName = Definitions.item(ore.id)?.name?.toLowerCase()
        mob.sendMessage("This rock contains $oreName.")

        stop()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProspectingAction
        return mob == other.mob && positions == other.positions && ore == other.ore
    }

    override fun hashCode(): Int = Objects.hash(mob, positions, ore)

}

class ExpiredProspectingAction(
    mob: Player,
    position: Position
) : DistancedAction<Player>(DELAY, true, mob, position, ORE_SIZE) {

    companion object {
        private const val DELAY = 0
        private const val ORE_SIZE = 1

        /**
         * Starts a [ExpiredProspectingAction] for the specified [Player], terminating the [Message] that triggered it.
         */
        fun start(message: ObjectActionMessage, player: Player) {
            val action = ExpiredProspectingAction(player, message.position)
            player.startAction(action)

            message.terminate()
        }
    }

    override fun executeAction() {
        mob.sendMessage("There is currently no ore available in this rock.")
        stop()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExpiredProspectingAction
        return mob == other.mob && positions == other.positions
    }

    override fun hashCode(): Int = Objects.hash(mob, positions)

}