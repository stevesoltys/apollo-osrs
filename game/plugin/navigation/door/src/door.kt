import DoorType.*
import org.apollo.game.model.Direction
import org.apollo.game.model.Position
import org.apollo.game.model.entity.obj.DynamicGameObject
import org.apollo.game.model.entity.obj.GameObject

enum class DoorType {
    LEFT_HINGE, RIGHT_HINGE,
    INNER_GATE, OUTER_GATE
}

data class Door(val id: Int, private val replacement: Int, val open: Boolean,
                private val type: DoorType, val otherDoor: Int?) {

    companion object {
        private val toggledDoors = HashMap<GameObject, GameObject>()

        fun toggle(gameObject: GameObject) {
            val door = gameObject.getDoor()!!
            val newPosition = door.toggledPosition(gameObject)
            val newOrientation = door.toggledDirection(gameObject).toOrientationInteger()

            val oldRegion = gameObject.world.regionRepository.fromPosition(gameObject.position)
            oldRegion.removeEntity(gameObject)

            val toggledDoor = toggledDoors[gameObject] ?: DynamicGameObject.createPublic(gameObject.world,
                door.replacement, newPosition, gameObject.type, newOrientation)

            if(toggledDoors.containsKey(gameObject)) {
                toggledDoors.remove(gameObject)
            } else {
                toggledDoors[toggledDoor] = gameObject
            }

            val newRegion = gameObject.world.regionRepository.fromPosition(newPosition)
            newRegion.addEntity(toggledDoor)
        }
    }

    fun secondDoorPosition(gameObject: GameObject): Position {
        var direction = if (open) {
            toggledDirection(gameObject)
        } else {
            Direction.WNES[gameObject.orientation]
        }

        direction = if (type == LEFT_HINGE || (!open && type == INNER_GATE)) {
            direction.clockwise().clockwise()
        } else if (type == RIGHT_HINGE || (!open && type == OUTER_GATE)) {
            direction.counterClockwise().counterClockwise()
        } else if(type == OUTER_GATE && open) {
            direction.opposite()
        } else {
            direction
        }

        return gameObject.position.step(1, direction)
    }

    fun toggledPosition(gameObject: GameObject): Position {
        var direction = when {
            open -> toggledDirection(gameObject).opposite()
            else -> Direction.WNES[gameObject.orientation]
        }

        if (diagonal(gameObject)) {
            direction = direction.clockwise().clockwise()
        }

        if (type == OUTER_GATE) {
            return gameObject.position.step(2, direction)
                .step(1, direction.counterClockwise().counterClockwise())
        }

        return gameObject.position.step(1, direction)
    }

    fun toggledDirection(gameObject: GameObject): Direction {
        return if (open && type == RIGHT_HINGE) {
            Direction.WNES[gameObject.orientation].counterClockwise().counterClockwise()
        } else if (!open && (type == LEFT_HINGE || isGate())) {
            Direction.WNES[gameObject.orientation].counterClockwise().counterClockwise()
        } else {
            Direction.WNES[gameObject.orientation].clockwise().clockwise()
        }
    }

    fun turnToPosition(position: Position, gameObject: GameObject): Position {
        return if (isGate()) {
            position

        } else if (open) {
            if (position == toggledPosition(gameObject)) {
                toggledPosition(gameObject).step(1, toggledDirection(gameObject))

            } else {
                toggledPosition(gameObject)
            }
        } else {
            if (position == gameObject.position) {
                gameObject.position.step(1, Direction.WNES[gameObject.orientation])

            } else {
                gameObject.position
            }
        }
    }

    fun walkPositions(gameObject: GameObject): Set<Position> {
        return if (open && !isGate()) {
            var first = toggledPosition(gameObject).step(1, toggledDirection(gameObject))
            var second = toggledPosition(gameObject)

            if (diagonal(gameObject)) {
                first = first.step(1, toggledDirection(gameObject).clockwise().clockwise())
                second = second.step(1, toggledDirection(gameObject).opposite().clockwise())
            }

            setOf(first, second)

        } else {
            var first = gameObject.position.step(1, Direction.WNES[gameObject.orientation])
            var second = gameObject.position

            if (diagonal(gameObject)) {
                first = first.step(1, Direction.WNES[gameObject.orientation].clockwise().clockwise())
                second = second.step(1, Direction.WNES[gameObject.orientation].opposite().clockwise())
            }

            setOf(first, second)
        }
    }

    private fun isGate() = type == INNER_GATE || type == OUTER_GATE

    private fun diagonal(gameObject: GameObject) = gameObject.type == 9
}