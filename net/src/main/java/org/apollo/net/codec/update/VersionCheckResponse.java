package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Steve Soltys
 */
public class VersionCheckResponse {

	private boolean validVersion;

	public VersionCheckResponse(boolean valid) {
		this.validVersion = valid;
	}

	public ByteBuf getData() {
		ByteBuf buffer = Unpooled.buffer(1);
		buffer.writeByte((byte) (validVersion ? 0 : 6));
		return buffer;
	}
}
