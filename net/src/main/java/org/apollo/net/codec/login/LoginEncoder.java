package org.apollo.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * A {@link MessageToByteEncoder} which encodes login response messages.
 *
 * @author Graham
 */
public final class LoginEncoder extends MessageToByteEncoder<LoginResponse> {

	/**
	 * Creates the login encoder.
	 */
	public LoginEncoder() {
		super(LoginResponse.class);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, LoginResponse response, ByteBuf out) {
		out.writeByte(response.getStatus());

		if (response.getStatus() == LoginConstants.STATUS_OK) {
			out.writeBoolean(false);

			out.writeInt(0);

			out.writeByte(response.getRights());
			out.writeBoolean(false);

			out.writeShort(response.getIndex());
			out.writeByte(0);
		}
	}

}