import org.apollo.game.message.impl.*
import org.apollo.game.model.entity.attr.*
import org.apollo.game.model.entity.setting.PrivilegeLevel

AttributeMap.define("debug_mode", AttributeDefinition(false, AttributePersistence.PERSISTENT, AttributeType.BOOLEAN))

on_command("debug", PrivilegeLevel.ADMINISTRATOR)
        .then { player ->
            val debugMode = ((player.attributes["debug_mode"]?.value ?: false) as Boolean).not()

            player.setAttribute("debug_mode", BooleanAttribute(debugMode))
            player.sendMessage("Debug mode is now ${if (debugMode) "enabled" else "disabled"}.")
        }

on { ButtonMessage::class }
        .then { player ->
            val debugMode = (player.attributes["debug_mode"]?.value ?: false) as Boolean

            if (debugMode) {
                player.sendMessage("ButtonMessage -> { Index: $index, Interface: $interfaceId, Button: $button, " +
                        "Child button: $childButton, I Item: $itemId }")
            }
        }

on { ObjectActionMessage::class }
        .then { player ->
            val debugMode = (player.attributes["debug_mode"]?.value ?: false) as Boolean

            if (debugMode) {
                player.sendMessage("ObjectActionMessage -> { Object: $id, Option: $option, Position: " +
                        "(${position.x}, ${position.y}, ${position.height}) }")
            }
        }

on { ItemOptionMessage::class }
        .then { player ->
            val debugMode = (player.attributes["debug_mode"]?.value ?: false) as Boolean

            if (debugMode) {
                player.sendMessage("ItemOptionMessage -> { Interface: $interfaceId, Slot: $slot, Item: $id, " +
                        "Option: $option }")
            }
        }

on { ItemOnItemMessage::class }
        .then { player ->
            val debugMode = (player.attributes["debug_mode"]?.value ?: false) as Boolean

            if (debugMode) {
                player.sendMessage("ItemOnItemMessage -> { Interface: $interfaceId -> $targetInterfaceId, " +
                        "Slot: $slot -> $targetSlot, Item: $id -> $targetId }")
            }
        }

on { NpcActionMessage::class }
        .then { player ->
            val debugMode = (player.attributes["debug_mode"]?.value ?: false) as Boolean

            if (debugMode) {
                val npc = player.world.npcRepository.get(index)

                player.sendMessage("NpcActionMessage -> { NPC: ${npc.id}, Option: $option }")
            }
        }