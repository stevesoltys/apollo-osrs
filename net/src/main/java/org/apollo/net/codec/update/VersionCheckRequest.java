package org.apollo.net.codec.update;

/**
 * @author Steve Soltys
 */
public class VersionCheckRequest {
	private final int version;

	public VersionCheckRequest(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}
}
