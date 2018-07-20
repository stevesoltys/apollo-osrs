import org.apollo.game.message.impl.ObjectActionMessage
import org.apollo.plugin.entity.walkto.walkTo

on { ObjectActionMessage::class }
        .then { player -> player.walkTo(position, smart = true) }