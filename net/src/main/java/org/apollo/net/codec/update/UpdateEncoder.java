package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apollo.cache.FileDescriptor;

import java.util.List;

/**
 * A {@link MessageToMessageEncoder} for the 'on-demand' protocol.
 *
 * @author Graham
 */
public final class UpdateEncoder extends MessageToMessageEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) {

		if (msg instanceof OnDemandResponse) {
			OnDemandResponse resp = (OnDemandResponse) msg;
			FileDescriptor desc = resp.getFileDescriptor();
			ByteBuf chunkData = resp.getChunkData();

			ByteBuf msgBuffer = Unpooled.buffer(chunkData.capacity() + 3);
			msgBuffer.writeByte(desc.getType());
			msgBuffer.writeShort(desc.getFile());
			chunkData.getBytes(chunkData.readerIndex(), msgBuffer);
			out.add(msgBuffer);

		} else if (msg instanceof VersionCheckResponse) {
			VersionCheckResponse response = (VersionCheckResponse) msg;

			out.add(response.getData());
		}
	}

}