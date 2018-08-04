import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.game.model.Animation
import org.apollo.game.plugin.api.attack
import org.apollo.game.plugin.api.getObject
import org.apollo.game.plugin.api.startDistancedAction

/**
 * A list of [ObjectDefinition] identifiers which are training dummies.
 */
val DUMMY_IDS = setOf(1764)

/**
 * The maximum level a player can be before the dummy stops giving XP.
 */
val LEVEL_THRESHOLD = 8

/**
 * The number of experience points per hit.
 */
val EXP_PER_HIT = 5.0

/**
 * The [Animation] played when a player hits a dummy.
 */
val PUNCH_ANIMATION = Animation(422)

on { ObjectActionMessage::class }
    .where { option == 1 && id in DUMMY_IDS }
    .then { player ->
        val dummyObject = player.world.getObject(position, id)

        player.startDistancedAction(dummyObject, delay = 1) {
            player.sendMessage("You hit the dummy.")
            player.turnTo(position)
            player.playAnimation(PUNCH_ANIMATION)
            wait()

            if (player.attack.maximum >= LEVEL_THRESHOLD) {
                player.sendMessage("There is nothing more you can learn from hitting a dummy.")

            } else {
                player.attack.experience += EXP_PER_HIT
            }
        }
    }
