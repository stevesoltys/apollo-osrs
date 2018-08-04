import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.entity.obj.ObjectType
import org.apollo.game.plugin.api.getObject
import org.apollo.game.plugin.api.startDistancedAction

on { ObjectActionMessage::class }
    .where { option == 1 }
    .then { player ->
        val gameObject = player.world.getObject(position, id)

        if (!gameObject.isDoor()) {
            return@then
        }

        val door = gameObject.getDoor()!!
        val positions = gameObject.bounds.interactionPositions

        if(ObjectType.valueOf(gameObject.type) == ObjectType.DIAGONAL_WALL) {
            positions.remove(gameObject.getDoor()!!.toggledPosition(gameObject))
        }

        player.startDistancedAction(positions, 0, true) {

            if (door.otherDoor != null) {
                val otherDoorPosition = door.secondDoorPosition(gameObject)
                val otherDoorId = door.otherDoor

                Door.toggle(gameObject.world.getObject(otherDoorPosition, otherDoorId))
            }

            Door.toggle(gameObject)
            player.turnTo(door.turnToPosition(player.position, gameObject))
            stop()
        }
    }