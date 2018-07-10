package org.apollo.net.codec.login;

/**
 * Represents a login response.
 *
 * @author Graham
 */
public final class LoginResponse {

	/**
	 * The flagged flag.
	 */
	private final boolean flagged;

	/**
	 * The player index.
	 */
	private int index;

	/**
	 * The rights level.
	 */
	private final int rights;

	/**
	 * The login status.
	 */
	private final int status;

	/**
	 * Creates the login response.
	 *  @param status The login status.
	 * @param index
	 * @param rights The rights level.
	 * @param flagged The flagged flag.
	 */
	public LoginResponse(int status, int index, int rights, boolean flagged) {
		this.status = status;
		this.index = index;
		this.rights = rights;
		this.flagged = flagged;
	}

	/**
	 * Gets the index.
	 *
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the rights level.
	 *
	 * @return The rights level.
	 */
	public int getRights() {
		return rights;
	}

	/**
	 * Gets the status.
	 *
	 * @return The status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Checks if the player should be flagged.
	 *
	 * @return The flagged flag.
	 */
	public boolean isFlagged() {
		return flagged;
	}

}