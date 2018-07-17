import org.apollo.game.model.Item
import org.apollo.game.model.Position
import org.apollo.game.model.World
import org.apollo.game.model.entity.GroundItem
import org.apollo.game.model.entity.Player

/**
 * Spawns a new local [GroundItem] for this Player at the specified [Position].
 */
fun Player.addGroundItem(item: Item, position: Position) {
    world.addGroundItem(world, GroundItem.dropped(world, position, item, this))
}

internal fun World.globalizeGroundItem(item: GroundItem) {
    val region = regionRepository.fromPosition(item.position)

    region.removeEntity(item)
    item.globalize()
    region.addEntity(item, true)
}

internal fun World.addGroundItem(world: World, item: GroundItem) {
    world.groundItems.add(item)
    item.index = world.groundItems.indexOf(item)

    val region = regionRepository.fromPosition(item.position)
    region.addEntity(item, true)
    schedule(GroundItemSynchronizationTask(item))
}

internal fun World.removeGroundItem(world: World, item: GroundItem) {
    world.groundItems.remove(item)

    regionRepository.fromPosition(item.position).removeEntity(item)
}