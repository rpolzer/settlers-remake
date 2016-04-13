/*******************************************************************************
 * Copyright (c) 2016
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.graphics.map.draw.settlerimages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.IOException;

import jsettlers.testutils.SizeStats;

/**
 * @author  Michael Zangl
 */
public class SettlerImageMapTest {

	@Test
	public void testIsLoading() {
		long start = System.currentTimeMillis();
		SettlerImageMap map = new SettlerImageMap() {
			@Override
			protected void onIOException(IOException e) {
				e.printStackTrace();
				fail();
			}
		};
	}

	@Test
	public void testSize() {
		SettlerImageMap map = new SettlerImageMap();

		assertTrue("Size below 200kB", computeSize(map) < 200000);
	}

	private long computeSize(Object o) {
		try {
			SizeStats stats = new SizeStats();
			stats.computeSize(o);
			return stats.getTotalSize();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			fail();
			return 0;
		}
	}

}
