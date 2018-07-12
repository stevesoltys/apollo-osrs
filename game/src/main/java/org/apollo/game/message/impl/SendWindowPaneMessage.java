package org.apollo.game.message.impl;


import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to open a window pane.
 *
 * @author Steve Soltys
 */
public class SendWindowPaneMessage extends Message {

    /**
     * The window pane id.
     */
    private int id;

    public SendWindowPaneMessage(int id) {
        this.id = id;
    }

    /**
     * Gets the window pane id.
     *
     * @return The window pane id.
     */
    public int getWindowPaneId() {
        return id;
    }
}
