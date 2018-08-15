import org.apollo.game.action.ActionBlock
import org.apollo.game.action.AsyncDistancedAction
import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Animation
import org.apollo.game.model.Position
import org.apollo.game.model.entity.Player
import org.apollo.net.message.Message

// so i'll run through each line of this

on { ObjectActionMessage::class } // when we get an object message
        .where { option == 1 && id == 11795 } // if the option is one and the id is equal to
        .then {
            LadderAction.start(this, it, position) // start this action
        }

class LadderAction(val player: Player, val position: Position) :
        AsyncDistancedAction<Player>(0, false, player, position, DISTANCE) {

    companion object {
        const val DISTANCE = 1

        val CLIMB_ANIMATION = Animation(828)

        fun start(message: Message, player: Player, position: Position) { // goes here
            player.startAction(LadderAction(player, position)) // starts this action for the player
            message.terminate() // dw about it
        }
    }


    override fun action(): ActionBlock = {
        mob.sendMessage("You climb the ladder.") // sends the message to the player
        mob.turnTo(position) // turns the player to the position of the ladder
        mob.playAnimation(CLIMB_ANIMATION)
        // we'd have to look up an animation list and find it, there are plenty online that i'll give you
        wait() // waits 1 tick.. we should make this more maybe?

        // now we wanna teleport the player, since this is a ladder
        // each player has three coordinates: x, y, z
        // you can find their coordinates by typing ::pos
        mob.position // every player / NPC has position
        // since this is a ladder, we'll want to teleport them to x,y,z+1, just increase their height by 1


        mob.teleport(Position(mob.position.x, mob.position.y, mob.position.height - 1))
        //mob.teleport(Position(mob.position.x, mob.position.y, mob.position.height - 1))
        // we have a teleport method actually
        // that should do it !
        // restart server


    }
}