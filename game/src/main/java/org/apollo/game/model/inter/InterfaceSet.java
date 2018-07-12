package org.apollo.game.model.inter;

import org.apollo.game.message.impl.CloseInterfaceMessage;
import org.apollo.game.message.impl.OpenInterfaceMessage;
import org.apollo.game.message.impl.SendWindowPaneMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.attr.AttributeDefinition;
import org.apollo.game.model.entity.attr.AttributeMap;
import org.apollo.game.model.entity.attr.AttributePersistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the set of interfaces the player has open.
 * <p/>
 * This class manages all six distinct types of interface (the last two are not present on 317 servers).
 * <p/>
 * <ul>
 * <li><strong>Windows:</strong> Interfaces such as the bank, the wilderness warning screen, the trade screen, etc.</li>
 * <li><strong>Overlays:</strong> Displayed in the same place as windows, but don't prevent a player from moving e.g.
 * the wilderness level indicator.</li>
 * <li><strong>Dialogues:</strong> Interfaces displayed over the chat box.</li>
 * <li><strong>Sidebars:</strong> Interfaces displayed over the inventory area.</li>
 * <li><strong>Fullscreen windows:</strong> A window displayed over the whole screen e.g. the 377 welcome screen.</li>
 * <li><strong>Fullscreen background:</strong> Interfaces displayed behind the fullscreen window, typically a blank,
 * black screen.</li>
 * </ul>
 *
 * @author Graham
 */
public final class InterfaceSet {

	static {
		AttributeMap.define("selected_window_pane",
			AttributeDefinition.forInt(InterfaceConstants.DEFAULT_WINDOW_PANE, AttributePersistence.PERSISTENT));
	}

	/**
	 * The current enter amount listener.
	 */
	private EnterAmountListener amountListener;

	/**
	 * A map of open interfaces.
	 */
	private Map<InterfaceType, Integer> interfaces = new HashMap<>();

	/**
	 * The current listener.
	 */
	private InterfaceListener listener;

	/**
	 * The player whose interfaces are being managed.
	 */
	private final Player player; // TODO: maybe switch to a listener system like the inventory?

	/**
	 * Creates an interface set.
	 *
	 * @param player The player.
	 */
	public InterfaceSet(Player player) {
		this.player = player;
	}

	/**
	 * Closes the current open interface(s).
	 */
	public void close() {
		closeWindow();
		closeSidebarInterface();
		closeDialogueInterface();
	}

	/**
	 * Sent by the client when it has closed an interface.
	 */
	public void interfaceClosed() {
		closeAndNotify();
	}

	/**
	 * Closes the current sidebar interface, if there is one.
	 */
	public void closeSidebarInterface() {
		if (interfaces.containsKey(InterfaceType.SIDEBAR)) {
			interfaces.remove(InterfaceType.SIDEBAR);
			player.send(new CloseInterfaceMessage(InterfaceConstants.DEFAULT_WINDOW_PANE,
				InterfaceConstants.ScreenArea.SIDEBAR));
		}
	}

	/**
	 * Closes the current window, if there is one.
	 */
	public void closeWindow() {
		if (interfaces.containsKey(InterfaceType.WINDOW)) {
			interfaces.remove(InterfaceType.WINDOW);
			listener = null;
			player.send(new CloseInterfaceMessage(InterfaceConstants.DEFAULT_WINDOW_PANE,
				InterfaceConstants.ScreenArea.GAME));
		}
	}

	/**
	 * Closes any interfaces in the chat box area.
	 */
	public void closeDialogueInterface() {
		if (interfaces.containsKey(InterfaceType.DIALOGUE)) {
			interfaces.remove(InterfaceType.DIALOGUE);
			player.send(new CloseInterfaceMessage(InterfaceConstants.DEFAULT_WINDOW_PANE,
				InterfaceConstants.ScreenArea.CHAT));
		}
		// TODO: Clear any input so it's empty next time we open it. CS2 necessary.
	}

	/**
	 * Opens a sidebar interface.
	 */
	public void openSidebarInterface(int interfaceId) {
		interfaces.put(InterfaceType.SIDEBAR, interfaceId);
		player.send(new OpenInterfaceMessage(InterfaceConstants.ScreenArea.SIDEBAR.getId(),
			InterfaceConstants.DEFAULT_WINDOW_PANE, interfaceId));
	}

	/**
	 * Sends a sidebar interface.
	 */
	public void sendUserInterface(int layer, int interfaceId) {
		player.send(new OpenInterfaceMessage(layer, InterfaceConstants.DEFAULT_WINDOW_PANE, interfaceId, true));
	}

	/**
	 * Opens a dialogue interface.
	 *
	 * @param interfaceId the dialogue interface to send.
	 */
	public void openDialogueInterface(int interfaceId) {
		interfaces.put(InterfaceType.DIALOGUE, interfaceId);
		player.send(new OpenInterfaceMessage(InterfaceConstants.ScreenArea.CHAT.getId(),
			InterfaceConstants.DEFAULT_WINDOW_PANE, interfaceId));
	}

	/**
	 * Sends the default user interfaces.
	 */
	public void sendDefaultUserInterfaces() {
		openWindowPane(InterfaceConstants.DEFAULT_WINDOW_PANE);

		Arrays.stream(InterfaceConstants.UserInterface.values())
			.filter(ui -> ui.getWindowId() == InterfaceConstants.DEFAULT_WINDOW_PANE)
			.forEach(ui -> {
				player.send(new OpenInterfaceMessage(ui.getLayer(), ui.getWindowId(), ui.getInterfaceId(), true));
			});
	}

	/**
	 * Opens the enter amount dialog.
	 *
	 * @param listener The enter amount listener.
	 */
	public void openEnterAmountDialogue(EnterAmountListener listener) {
		this.amountListener = listener;

		//TODO: Re-add this later.
//        player.send(new RunClientScriptEvent(108, new Object[]{"Enter amount:"}, "s"));
	}

	/**
	 * Opens a window and inventory sidebar.
	 *
	 * @param windowId  The window's id.
	 * @param sidebarId The sidebar's id.
	 */
	public void openWindowWithSidebar(int windowId, int sidebarId) {
		openWindowWithSidebar(null, windowId, sidebarId);
	}

	/**
	 * Opens a window and inventory sidebar with the specified listener.
	 *
	 * @param listener  The listener for this interface.
	 * @param windowId  The window's id.
	 * @param sidebarId The sidebar's id.
	 */
	public void openWindowWithSidebar(InterfaceListener listener, int windowId, int sidebarId) {
		openWindow(listener, windowId);
		openSidebarInterface(sidebarId);
	}

	/**
	 * Opens a window pane.
	 *
	 * @param windowPaneId The window pane's id.
	 */
	public void openWindowPane(int windowPaneId) {
		interfaces.put(InterfaceType.WINDOW_PANE, windowPaneId);
		player.send(new SendWindowPaneMessage(windowPaneId));
	}

	/**
	 * Opens a window interface with no listener in the default root window.
	 *
	 * @param windowId The window's id.
	 */
	public void openWindow(int windowId) {
		openWindow(null, windowId);
	}

	/**
	 * Opens a window interface with the specified listener in the default root
	 * window.
	 *
	 * @param listener The listener for this interface.
	 * @param windowId The window's id.
	 */
	public void openWindow(InterfaceListener listener, int windowId) {
		closeAndNotify();
		this.listener = listener;

		interfaces.put(InterfaceType.WINDOW, windowId);
		player.send(new OpenInterfaceMessage(InterfaceConstants.ScreenArea.GAME.getId(),
			InterfaceConstants.DEFAULT_WINDOW_PANE, windowId));
	}

	/**
	 * Gets the interface map.
	 *
	 * @return the interface map
	 */
	public Map<InterfaceType, Integer> getInterfaces() {
		return interfaces;
	}

	/**
	 * Checks if this interface sets contains the specified interface.
	 *
	 * @param id The interface's id.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(int id) {
		return interfaces.containsValue(id);
	}

	/**
	 * Checks if this interface set contains the specified interface type.
	 *
	 * @param type The interface's type.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean contains(InterfaceType type) {
		return interfaces.containsKey(type);
	}

	/**
	 * An internal method for closing the open window and notifying the
	 * listener, but not sending any events.
	 */
	private void closeAndNotify() {
		amountListener = null;

		interfaces.remove(InterfaceType.WINDOW);
		if (listener != null) {
			listener.interfaceClosed();
			listener = null;
		}
	}

	/**
	 * Called when the client has entered the specified amount. Notifies the
	 * current listener.
	 *
	 * @param amount The amount.
	 */
	public void enteredAmount(int amount) {
		if (amountListener != null) {
			amountListener.amountEntered(amount);
			amountListener = null;
		}
	}

	/**
	 * Gets the size of the interface set.
	 *
	 * @return The size.
	 */
	public int size() {
		return interfaces.size();
	}
}