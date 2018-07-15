package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ScreenResizeMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.attr.NumericalAttribute;
import org.apollo.game.model.inter.WindowPane;

/**
 * A {@link MessageHandler} that responds to {@link ScreenResizeMessage}s.
 *
 * @author Steve Soltys
 */
public final class ScreenResizeMessageHandler extends MessageHandler<ScreenResizeMessage> {

	/**
	 * Creates the ScreenResizeMessageHandler.
	 *
	 * @param world The {@link World} the {@link ScreenResizeMessage} occurred in.
	 */
	public ScreenResizeMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ScreenResizeMessage message) {

		if(message.getMode() == 1) {
			player.setAttribute("selected_window_pane",
				new NumericalAttribute(WindowPane.SCREEN_FIXED.getId()));

		} else if(message.getMode() == 2) {
			player.setAttribute("selected_window_pane",
				new NumericalAttribute(WindowPane.SCREEN_RESIZE.getId()));
		}

		player.getInterfaceSet().sendDefaultUserInterfaces();
	}

}