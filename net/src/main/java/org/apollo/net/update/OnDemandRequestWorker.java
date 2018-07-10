package org.apollo.net.update;

import com.oldscape.tool.cache.Cache;
import com.oldscape.tool.cache.ChecksumTable;
import com.oldscape.tool.cache.Container;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.apollo.cache.FileDescriptor;
import org.apollo.net.codec.update.OnDemandRequest;
import org.apollo.net.codec.update.OnDemandResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * A worker which services 'on-demand' requests.
 *
 * @author Graham
 */
public final class OnDemandRequestWorker extends RequestWorker<OnDemandRequest, Cache> {

	private ChecksumTable checksumTable;

	private Container checksumTableContainer;

	/**
	 * The maximum length of the first chunk, in bytes.
	 */
	private static final int FIRST_CHUNK_LENGTH = 509;

	/**
	 * The maximum length of a chunk, in bytes.
	 */
	private static final int CHUNK_LENGTH = 511;

	/**
	 * Creates the 'on-demand' request worker.
	 *
	 * @param dispatcher The dispatcher.
	 * @param fs         The file system.
	 */
	public OnDemandRequestWorker(UpdateDispatcher dispatcher, Cache fs) {
		super(dispatcher, fs);

		try {
			checksumTable = fs.createChecksumTable();
			checksumTableContainer = new Container(Container.COMPRESSION_NONE, checksumTable.encode());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ChannelRequest<OnDemandRequest> nextRequest(UpdateDispatcher dispatcher) throws InterruptedException {
		return dispatcher.nextOnDemandRequest();
	}

	@Override
	protected void service(Cache cache, Channel channel, OnDemandRequest request) throws IOException {
		FileDescriptor desc = request.getFileDescriptor();
		boolean crcTableRequest = desc.getFile() == 255 && desc.getType() == 255;

		if(request.getPriority() == OnDemandRequest.Priority.IGNORE) {
			return;
		}

		ByteBuffer fileData;

		if (crcTableRequest) {
			fileData = checksumTableContainer.encode();

		} else {
			fileData = cache.getStore().read(desc.getType(), desc.getFile());
			fileData.rewind();
		}

		int fileSize = fileData.limit();

		if(desc.getType() != 255 && fileSize > 1) {
			fileSize -= 2;
		}

		// TODO: Why do we skip version?
		fileData = ByteBuffer.wrap(fileData.array(), 0, fileSize);

		List<ByteBuffer> chunks = new LinkedList<>();
		for (int chunkId = 0; fileData.hasRemaining(); chunkId++) {
			int chunkSize = fileData.remaining();
			if (chunkId > 0) {
				if (chunkSize > CHUNK_LENGTH) {
					chunkSize = CHUNK_LENGTH;
				}
			} else if (chunkSize > FIRST_CHUNK_LENGTH) {
				chunkSize = FIRST_CHUNK_LENGTH;
			}
			byte[] tmp = new byte[chunkSize];
			fileData.get(tmp, 0, tmp.length);
			chunks.add(ByteBuffer.wrap(tmp));
		}

		int responseSize = fileSize + (chunks.size() - 1);
		ByteBuf responseBuffer = Unpooled.buffer(responseSize);

		for (int index = 0; index < chunks.size(); index++) {
			ByteBuffer chunk = chunks.get(index);
			if (index > 0) {
				// we write a byte of value -1 before every chunk after the first
				responseBuffer.writeByte(-1);
			}
			responseBuffer.writeBytes(chunk);
		}

		OnDemandResponse response = new OnDemandResponse(desc, responseSize, responseBuffer);
		channel.writeAndFlush(response);
	}
}