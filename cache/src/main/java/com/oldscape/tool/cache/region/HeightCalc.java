/**
* Copyright (c) Kyle Fricilone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oldscape.tool.cache.region;

/**
 * @author Kyle Friz
 * @since Feb 20, 2016
 */
public class HeightCalc {

	private static final int JAGEX_CIRCULAR_ANGLE = 2048;
	private static final double ANGULAR_RATIO = 360D / JAGEX_CIRCULAR_ANGLE;
	private static final double JAGEX_RADIAN = Math.toRadians(ANGULAR_RATIO);

	private static final int[] SIN = new int[JAGEX_CIRCULAR_ANGLE];
	private static final int[] COS = new int[JAGEX_CIRCULAR_ANGLE];

	static {
		for (int i = 0; i < JAGEX_CIRCULAR_ANGLE; i++) {
			SIN[i] = (int) (65536.0D * Math.sin(i * JAGEX_RADIAN));
			COS[i] = (int) (65536.0D * Math.cos(i * JAGEX_RADIAN));
		}
	}

	static final int calculate(int baseX, int baseY, int x, int y) {
		int xc = (baseX >> 3) + 932638 + x;
		int yc = (baseY >> 3) + 556190 + y;
		int n = interpolateNoise(xc + '\ub135', yc + 91923, 4) - 128
				+ (interpolateNoise(10294 + xc, yc + '\u93bd', 2) - 128 >> 1)
				+ (interpolateNoise(xc, yc, 1) - 128 >> 2);
		n = 35 + (int) (n * 0.3D);
		if (n >= 10) {
			if (n > 60) {
				n = 60;
			}
		} else {
			n = 10;
		}

		return n;
	}

	static final int interpolateNoise(int x, int y, int frequency) {
		int intX = x / frequency;
		int fracX = x & frequency - 1;
		int intY = y / frequency;
		int fracY = y & frequency - 1;
		int v1 = smoothedNoise1(intX, intY);
		int v2 = smoothedNoise1(intX + 1, intY);
		int v3 = smoothedNoise1(intX, intY + 1);
		int v4 = smoothedNoise1(1 + intX, 1 + intY);
		int i1 = interpolate(v1, v2, fracX, frequency);
		int i2 = interpolate(v3, v4, fracX, frequency);
		return interpolate(i1, i2, fracY, frequency);
	}

	static final int smoothedNoise1(int x, int y) {
		int corners = noise(x - 1, y - 1) + noise(x + 1, y - 1) + noise(x - 1, 1 + y) + noise(x + 1, y + 1);
		int sides = noise(x - 1, y) + noise(1 + x, y) + noise(x, y - 1) + noise(x, 1 + y);
		int center = noise(x, y);
		return center / 4 + sides / 8 + corners / 16;
	}

	static final int noise(int x, int y) {
		int n = x + y * 57;
		n ^= n << 13;
		return ((n * (n * n * 15731 + 789221) + 1376312589) & Integer.MAX_VALUE) >> 19 & 255;
	}

	static final int interpolate(int a, int b, int x, int y) {
		int f = 65536 - COS[1024 * x / y] >> 1;
		return (f * b >> 16) + (a * (65536 - f) >> 16);
	}

}