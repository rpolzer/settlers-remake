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
package jsettlers.testutils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

/**
 * @author  Michael Zangl
 */
public class SizeStats {

		private final IdentityHashMap<Object, Object> foundObjects = new IdentityHashMap<>();
		private final IdentityHashMap<Class<?>, Long> objectCounts = new IdentityHashMap<>();
		private final IdentityHashMap<Class<?>, Long> objectSize = new IdentityHashMap<>();

	private long totalSize = 0;

		public SizeStats() {
		}

	public void computeSize(Object o) throws IllegalAccessException {
			if (o == null) {
				return;
			}
			if (foundObjects.containsKey(o)) {
				return;
			}
			foundObjects.put(o, o);

			long size = SizeFetcher.getObjectSize(o);
			totalSize += size;
			Long count = objectCounts.get(o.getClass());
			Long classSize = objectSize.get(o.getClass());
			count = count == null ? 1 : count + 1;
			classSize = classSize == null ? size : classSize + size;
			objectCounts.put(o.getClass(), count);
			objectSize.put(o.getClass(), classSize);


			if (o.getClass().isArray()) {
				computeArraySize(o);
			} else {
				computeObjectSize(o);
			}
		}

		private void computeArraySize(Object o) throws IllegalAccessException {
			if (!o.getClass().getComponentType().isPrimitive()) {
				int len = Array.getLength(o);
				for (int i = 0; i < len; i++) {
					computeSize(Array.get(o, i));
				}
			}
		}

		private void computeObjectSize(Object o) throws IllegalAccessException {
			Class<?> classToTest = o.getClass();
			while (classToTest != null) {
				Field[] fields = classToTest.getDeclaredFields();
				for (Field field : fields) {
					if (!field.getType().isPrimitive()) {
						field.setAccessible(true);
						computeSize(field.get(o));
					}
				}

				classToTest = classToTest.getSuperclass();
			}
		}

	public long getTotalSize() {
		return totalSize;
	}


		public void print() {
			System.out.println("Total size: " + totalSize);

			objectSize.entrySet().stream().sorted(
					(a, b) -> -a.getValue().compareTo(b.getValue())
			).forEach(
					e -> System.out.println("    " + e.getKey().getName() + ": " + e.getValue() + " Bytes for " + objectCounts.get(e.getKey()) + " Objects")
			);
		}
}
