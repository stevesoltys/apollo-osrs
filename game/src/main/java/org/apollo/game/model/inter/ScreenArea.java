package org.apollo.game.model.inter;


import com.google.common.collect.ImmutableMap;
import com.oldscape.tool.util.Preconditions;

import java.util.Map;

/**
 * A certain area of the game screen. This is used to specify where an interface is to be created or changed.
 *
 * @author Steve Soltys
 */
public enum ScreenArea {

	GAME(ImmutableMap.of(
		WindowPane.SCREEN_FIXED, 19,
		WindowPane.SCREEN_RESIZE, 12,
		WindowPane.SCREEN_REARRANGED, 12
	)),

	GAME_WALKABLE(ImmutableMap.of(
		WindowPane.SCREEN_FIXED, 12,
		WindowPane.SCREEN_RESIZE, 10,
		WindowPane.SCREEN_REARRANGED, 10
	)),

	SIDEBAR(ImmutableMap.of(
		WindowPane.SCREEN_FIXED, 61,
		WindowPane.SCREEN_RESIZE, 59,
		WindowPane.SCREEN_REARRANGED, 57
	)),

	CHAT(ImmutableMap.of());

	private Map<WindowPane, Integer> entries;

	ScreenArea(Map<WindowPane, Integer> entries) {
		this.entries = entries;
	}

	public int getId(WindowPane windowPane) {
		Preconditions.checkArgument(entries.containsKey(windowPane), "Unsupported window pane specified.");
		return entries.get(windowPane);
	}
}
