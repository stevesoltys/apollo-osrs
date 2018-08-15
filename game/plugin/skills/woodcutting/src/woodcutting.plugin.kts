import org.apollo.game.GameConstants
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.model.entity.obj.GameObject
import org.apollo.game.plugin.api.*
import org.apollo.game.plugin.skills.woodcutting.Axe
import org.apollo.game.plugin.skills.woodcutting.Tree
import java.util.concurrent.TimeUnit

// TODO Accurate chopping rates, e.g. https://twitter.com/JagexKieren/status/713403124464107520

on { ObjectActionMessage::class }
    .where { option == 1 }
    .then { player ->
        Tree.lookup(id)?.let { tree ->
            val treeObject = player.world.getObject(position, id)

            startCutting(player, tree, treeObject)
        }
    }

val MINIMUM_RESPAWN_TIME = 30L // In seconds

fun startCutting(player: Player, tree: Tree, treeObject: GameObject) {

    player.startDistancedAction(treeObject) {
        val tool = Axe.bestFor(player)

        if (tool == null) {
            player.sendMessage("You do not have an axe for which you have the level to use.")
            return@startDistancedAction

        } else if (player.inventory.freeSlots() == 0) {
            player.inventory.forceCapacityExceeded()
            return@startDistancedAction
        }

        player.turnTo(treeObject.position)

        val level = player.woodcutting.current
        if (level < tree.level) {
            player.sendMessage("You do not have the required level to cut down this tree.")
            stop()
        }

        var cutting = true

        while (cutting) {
            player.sendMessage("You swing your axe at the tree.")
            player.playAnimation(tool.animation)

            var currentPulse = 0

            while (currentPulse++ < tool.pulses) {
                player.playAnimation(tool.animation)
                wait(1)
            }

            if (player.inventory.add(tree.id)) {
                val logName = Definitions.item(tree.id)!!.name.toLowerCase()

                player.sendMessage("You managed to cut some $logName.")
                player.woodcutting.experience += tree.exp
            }

            if (tree.isCutDown()) {
                // respawn time: http://runescape.wikia.com/wiki/Trees
                val respawn = TimeUnit.SECONDS.toMillis(MINIMUM_RESPAWN_TIME + rand(150)) / GameConstants.PULSE_DELAY

                player.world.expireObject(treeObject, tree.stump, respawn.toInt())
                player.stopAnimation()
                cutting = false
                stop()
            }
        }
    }
}