import Ground_item_action_plugin.TakeGroundItemAction
import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.DropItemMessage
import org.apollo.game.message.impl.TakeTileItemMessage
import org.apollo.game.model.entity.EntityType
import org.apollo.game.model.entity.GroundItem
import org.apollo.game.model.entity.Player
import org.apollo.net.message.Message

/**
 * A set of item identifiers for items which cannot be dropped, only destroyed.
 */
val DESTROY_ITEMS = setOf<Int>(

)

/**
 * Handles any [DropItemMessage]s by removing the item from the user's inventory and creating a ground item.
 */
on { DropItemMessage::class }
        .then {
            val item = it.inventory.get(slot)

            if (!DESTROY_ITEMS.contains(item.id)) {
                it.inventory.reset(slot)

                it.addGroundItem(item, it.position)
            }
        }

/**
 * Handles any [TakeTileItemMessage]s by starting a [TakeGroundItemAction].
 */
on { TakeTileItemMessage::class }
        .then { player ->
            val region = player.world.regionRepository[position.regionCoordinates]

            region.getEntities<GroundItem>(EntityType.GROUND_ITEM)
                    .filter { it.visibleTo(player) && it.position == position && it.item.id == id }
                    .findFirst().ifPresent { groundItem ->
                        TakeGroundItemAction.start(this, player, groundItem)
                    }
        }

/**
 * An async distanced action which takes a ground item and places it into the user's inventory.
 */
class TakeGroundItemAction(val player: Player, private val groundItem: GroundItem) :
        AsyncDistancedAction<Player>(delay = 0, immediate = true, mob = player,
                position = groundItem.position, distance = 0) {

    companion object {
        fun start(message: Message, player: Player, groundItem: GroundItem) {
            player.startAction(TakeGroundItemAction(player, groundItem))
            message.terminate()
        }
    }

    override fun action(): ActionBlock = {
        val region = player.world.regionRepository[position.regionCoordinates]

        if(region.contains(groundItem) && groundItem.isAvailable) {
            groundItem.isAvailable = false

            region.removeEntity(groundItem)
            player.world.groundItems.remove(groundItem)
            player.inventory.add(groundItem.item)
        }

        stop()
    }
}
