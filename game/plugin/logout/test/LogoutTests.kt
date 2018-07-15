
import org.apollo.game.message.impl.ButtonMessage
import org.apollo.game.plugin.testing.KotlinPluginTest
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class LogoutTests : KotlinPluginTest() {

	companion object {
        const val LOGOUT_INDEX = 1
        const val LOGOUT_INTERFACE_ID = 182
		const val LOGOUT_BUTTON_ID = 6
        const val LOGIN_CHILD_BUTTON_ID = -1
        const val LOGIN_ITEM_ID = -1
	}

	@Test
    fun `The player should be logged out when they click the logout button`() {
		player.notify(ButtonMessage(LOGOUT_INDEX, LOGOUT_INTERFACE_ID, LOGOUT_BUTTON_ID, LOGIN_CHILD_BUTTON_ID,
            LOGIN_ITEM_ID))

		verify(player, times(1)).logout()
	}

}