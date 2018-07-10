package org.apollo.net.update;

import io.netty.channel.Channel;
import org.apollo.net.codec.update.VersionCheckRequest;
import org.apollo.net.codec.update.VersionCheckResponse;

import java.io.IOException;

/**
 * @author Steve Soltys
 */
public class VersionCheckRequestWorker extends RequestWorker<VersionCheckRequest, Integer> {

	public VersionCheckRequestWorker(UpdateDispatcher dispatcher, Integer version) {
		super(dispatcher, version);
	}

	@Override
	protected ChannelRequest<VersionCheckRequest> nextRequest(UpdateDispatcher dispatcher) throws InterruptedException {
		return dispatcher.nextVersionCheckRequest();
	}

	@Override
	protected void service(Integer version, Channel channel, VersionCheckRequest request) throws IOException {
		channel.writeAndFlush(new VersionCheckResponse(version == request.getVersion()));
	}

}