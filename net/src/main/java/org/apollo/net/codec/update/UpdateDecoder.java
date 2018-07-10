package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apollo.cache.FileDescriptor;
import org.apollo.net.codec.update.OnDemandRequest.Priority;

import java.util.List;

/**
 * A {@link ByteToMessageDecoder} for the 'on-demand' protocol.
 *
 * @author Graham
 */
public final class UpdateDecoder extends ByteToMessageDecoder {

	/**
	 * A flag indicating whether a version check has occurred.
	 */
	private boolean checkedVersion = false;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.readableBytes() >= 4) {

			if(checkedVersion) {
				Priority priority = Priority.valueOf(buffer.readUnsignedByte());
				int type = buffer.readUnsignedByte();
				int file = buffer.readUnsignedShort();

				FileDescriptor desc = new FileDescriptor(type, file);
				out.add(new OnDemandRequest(desc, priority));

			} else {
				int revision = buffer.readInt();
				out.add(new VersionCheckRequest(revision));

				checkedVersion = true;
			}
		}
	}

}