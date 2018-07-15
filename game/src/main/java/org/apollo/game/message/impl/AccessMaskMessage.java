package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * I have absolutely no idea what this is.
 *
 * @author Steve Soltys
 */
public class AccessMaskMessage extends Message {

	private final int first;

	private final int second;

	private final int third;

	private final int fourth;

	public AccessMaskMessage(int first, int second, int third, int fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}

	public int getFirst() {
		return first;
	}

	public int getSecond() {
		return second;
	}

	public int getThird() {
		return third;
	}

	public int getFourth() {
		return fourth;
	}
}
