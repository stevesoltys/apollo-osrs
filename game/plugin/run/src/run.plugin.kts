import org.apollo.game.message.impl.UpdateRunEnergyMessage
import org.apollo.game.model.event.impl.LoginEvent

on_button(interfaceId = 261, id = 67)
        .then {
            it.toggleRunning()
        }

on_button(interfaceId = 160, id = 22)
        .then {
            it.toggleRunning()
        }

on_player_event { LoginEvent::class }
        .then {
            it.send(UpdateRunEnergyMessage(it.runEnergy))
        }