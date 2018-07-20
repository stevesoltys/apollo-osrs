
import org.apollo.game.action.DistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.api.findObject
import org.apollo.net.message.Message
import org.apollo.plugin.entity.walkto.walkToClosest
import java.util.*

on { ObjectActionMessage::class }
        .where { option == 1 }
        .then {
            val region = it.world.regionRepository.fromPosition(position)
            val gameObject = region.findObject(position, id).orElse(null)

            if (gameObject != null && gameObject.isDoor()) {
                OpenDoorAction.start(this, it, gameObject)
            }
        }

class OpenDoorAction(private val player: Player, private val gameObject: GameObject, positions: Set<Position>) :
        DistancedAction<Player>(0, false, player, positions, DISTANCE) {

    companion object {
        const val DISTANCE = 0

        fun start(message: Message, player: Player, gameObject: GameObject) {
            val door = gameObject.getDoor()!!
            player.walkToClosest(door.walkPositions(gameObject), smart = true)

            player.startAction(OpenDoorAction(player, gameObject, door.walkPositions(gameObject)))
            message.terminate()
        }
    }

    override fun executeAction() {
        val door = gameObject.getDoor()!!

        if (door.otherDoor != null) {
            val otherDoorPosition = door.secondDoorPosition(gameObject)
            val otherDoorRegion = gameObject.world.regionRepository.fromPosition(otherDoorPosition)
            val otherGameObject = otherDoorRegion.findObject(otherDoorPosition, door.otherDoor).orElse(null)

            if (otherGameObject == null) {
                stop()
                return
            }

            Door.toggle(otherGameObject)
        }

        Door.toggle(gameObject)
        player.turnTo(door.turnToPosition(player.position, gameObject))
        stop()
    }

    override fun equals(other: Any?): Boolean {
        return other is OpenDoorAction && positions == other.positions && player == other.player &&
                gameObject == other.gameObject
    }

    override fun hashCode(): Int = Objects.hash(positions, player, gameObject)
}