package org.apollo.game.model.inter;

import java.util.Arrays;

/**
 * @author Steve Soltys
 */
public enum WindowPane {

	/**
	 * The fixed interface orientation.
	 */
	SCREEN_FIXED(548),

	/**
	 * The resizable interface orientation.
	 */
	SCREEN_RESIZE(161),

	/**
	 * The resizable + rearranged interface orientation.
	 */
	SCREEN_REARRANGED(164);

	private final int identifier;

	WindowPane(int identifier) {
		this.identifier = identifier;
	}

	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Gets the WindowPane with the specified value.
	 *
	 * @param identifier The integer identifier of the WindowPane.
	 * @return The WindowPane.
	 * @throws IllegalArgumentException If no WindowPane with the specified the value exists.
	 */
	public static WindowPane valueOf(int identifier) {
		return Arrays.stream(values())
			.filter(status -> status.identifier == identifier)
			.findAny().orElseThrow(() -> new IllegalArgumentException("Illegal window identifier."));
	}

}
