import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.plugin.api.getObject
import org.apollo.game.plugin.api.startDistancedAction

on { ObjectActionMessage::class }
    .where { option == 1 }
    .then { player ->
        val gameObject = player.world.getObject(position, id)

        if (!gameObject.isDoor()) {
            return@then
        }

        player.startDistancedAction(gameObject, 0, true) {
            val door = gameObject.getDoor()!!

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