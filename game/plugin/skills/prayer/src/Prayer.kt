import com.google.common.collect.MultimapBuilder
import com.google.common.collect.SetMultimap
import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.message.impl.ConfigMessage
import org.apollo.game.model.entity.Player
import org.apollo.game.model.inter.InterfaceConstants

enum class Prayer(val button: Int, val level: Int, val setting: Int, val drain: Double) {
    THICK_SKIN(button = 0, level = 1, setting = 83, drain = 0.01),
    BURST_OF_STRENGTH(button = 1, level = 4, setting = 84, drain = 0.01),
    CLARITY_OF_THOUGHT(button = 2, level = 7, setting = 85, drain = 0.01),
    ROCK_SKIN(button = 3, level = 10, setting = 86, drain = 0.04),
    SUPERHUMAN_STRENGTH(button = 4, level = 13, setting = 87, drain = 0.04),
    IMPROVED_REFLEXES(button = 5, level = 16, setting = 88, drain = 0.04),
    RAPID_RESTORE(button = 6, level = 19, setting = 89, drain = 0.01),
    RAPID_HEAL(button = 7, level = 22, setting = 90, drain = 0.01),
    PROTECT_ITEM(button = 8, level = 25, setting = 91, drain = 0.01),
    STEEL_SKIN(button = 9, level = 28, setting = 92, drain = 0.1),
    ULTIMATE_STRENGTH(button = 10, level = 31, setting = 93, drain = 0.1),
    INCREDIBLE_REFLEXES(button = 11, level = 34, setting = 94, drain = 0.1),
    PROTECT_FROM_MAGIC(button = 12, level = 37, setting = 95, drain = 0.15),
    PROTECT_FROM_MISSILES(button = 13, level = 40, setting = 96, drain = 0.15),
    PROTECT_FROM_MELEE(button = 14, level = 43, setting = 97, drain = 0.15),
    RETRIBUTION(button = 15, level = 46, setting = 98, drain = 0.15),
    REDEMPTION(button = 16, level = 49, setting = 99, drain = 0.15),
    SMITE(button = 17, level = 52, setting = 100, drain = 0.2);

    companion object {
        private val prayers = Prayer.values().associateBy(Prayer::button)

        fun forButton(button: Int) = prayers[button]

        internal fun ButtonMessage.isPrayerButton(): Boolean =
            interfaceId == InterfaceConstants.PRAYER_ID && button in prayers
    }
}

val Player.currentPrayers: Set<Prayer>
    get() = playerPrayers[this]

fun Player.updatePrayer(prayer: Prayer) {
    val value = if (currentPrayers.contains(prayer)) 1 else 0
    send(ConfigMessage(prayer.setting, value))
}

internal val playerPrayers: SetMultimap<Player, Prayer> = MultimapBuilder.hashKeys()
    .enumSetValues(Prayer::class.java)
    .build<Player, Prayer>()