import org.apollo.game.model.entity.obj.GameObject

object Doors {
    val list = mutableListOf<Door>()
}

fun door(open: Int, closed: Int, type: DoorType, otherDoorOpen: Int? = null, otherDoorClosed: Int? = null) {
    Doors.list.add(Door(id = open, replacement = closed, open = true, type = type, otherDoor = otherDoorOpen))
    Doors.list.add(Door(id = closed, replacement = open, open = false, type = type, otherDoor = otherDoorClosed))
}

fun double_door(leftOpen: Int, leftClosed: Int, rightOpen: Int, rightClosed: Int) {
    door(leftOpen, leftClosed, DoorType.LEFT_HINGE, rightOpen, rightClosed)
    door(rightOpen, rightClosed, DoorType.RIGHT_HINGE, leftOpen, leftClosed)
}

fun gate(innerOpen: Int, innerClosed: Int, outerOpen: Int, outerClosed: Int) {
    door(innerOpen, innerClosed, DoorType.INNER_GATE, outerOpen, outerClosed)
    door(outerOpen, outerClosed, DoorType.OUTER_GATE, innerOpen, innerClosed)
}

fun GameObject.isDoor(): Boolean {
    return Doors.list.any { it.id == id }
}

fun GameObject.getDoor(): Door? {
    return Doors.list.firstOrNull { it.id == id }
}
