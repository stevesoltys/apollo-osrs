import org.apollo.game.message.impl.ToggleMouseZoomMessage
import org.apollo.game.model.entity.attr.*
import org.apollo.game.model.event.impl.LoginEvent
import org.apollo.game.model.inter.InterfaceConstants

AttributeMap.define("mouse_zoom", AttributeDefinition(true, AttributePersistence.PERSISTENT, AttributeType.BOOLEAN))

on_button(interfaceId = InterfaceConstants.SETTINGS_ID, id = 4)
        .then { player ->
            val mouseZoom = ((player.attributes["mouse_zoom"]?.value ?: false) as Boolean).not()

            player.setAttribute("mouse_zoom", BooleanAttribute(mouseZoom))
        }


on_player_event { LoginEvent::class }
        .then { player ->
            val mouseZoom = ((player.attributes["mouse_zoom"]?.value ?: false) as Boolean)

            if (mouseZoom) {
                player.send(ToggleMouseZoomMessage())
            }
        }
