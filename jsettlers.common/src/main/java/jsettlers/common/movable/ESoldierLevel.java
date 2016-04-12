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
package jsettlers.common.movable;

import java.util.EnumSet;

/**
 * The level a soldier is of.
 * 
 * @author Michael Zangl
 */
public enum ESoldierLevel {
	LEVEL_1(EMovableType.SWORDSMAN_L1, EMovableType.BOWMAN_L1, EMovableType.PIKEMAN_L1),
	LEVEL_2(EMovableType.SWORDSMAN_L2, EMovableType.BOWMAN_L2, EMovableType.PIKEMAN_L2),
	LEVEL_3(EMovableType.SWORDSMAN_L3, EMovableType.BOWMAN_L3, EMovableType.PIKEMAN_L3);

	private final EnumSet<EMovableType> soldiers;

	ESoldierLevel(EMovableType type, EMovableType... types) {
		soldiers = EnumSet.of(type, types);
	}

	public boolean isOfLevel(EMovableType type) {
		return soldiers.contains(type);
	}
}
