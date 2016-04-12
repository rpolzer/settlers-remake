/*******************************************************************************
 * Copyright (c) 2015
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
import java.util.Set;

/**
 * @author codingberlin
 * @author Andreas Eberle
 */
public enum ESoldierType {
	BOWMAN(ESoldierClass.BOWMAN, EMovableType.BOWMAN_L1, EMovableType.BOWMAN_L2, EMovableType.BOWMAN_L3),
	SWORDSMAN(ESoldierClass.INFANTRY, EMovableType.SWORDSMAN_L1, EMovableType.SWORDSMAN_L2, EMovableType.SWORDSMAN_L3),
	PIKEMAN(ESoldierClass.INFANTRY, EMovableType.PIKEMAN_L1, EMovableType.PIKEMAN_L2, EMovableType.PIKEMAN_L3);

	public static final ESoldierType[] VALUES = ESoldierType.values();
	private static final Set<EMovableType> allSoldiers = EnumSet.noneOf(EMovableType.class);
	static {
		for (ESoldierType v : VALUES) {
			allSoldiers.addAll(v.getAllOfType());
		}
	}

	public final int ordinal;
	private final EnumSet<EMovableType> types;
	private final ESoldierClass soldierClass;

	ESoldierType(ESoldierClass soldierClass, EMovableType type1, EMovableType... types) {
		this.soldierClass = soldierClass;
		this.ordinal = ordinal();
		this.types = EnumSet.of(type1, types);
	}

	public ESoldierClass getSoldierClass() {
		return soldierClass;
	}

	public boolean isOfType(EMovableType movableType) {
		return types.contains(movableType);
	}

	public Set<EMovableType> getAllOfType() {
		return types;
	}

	public static Set<EMovableType> getAllSoldiers() {
		return allSoldiers;
	}

	public static boolean isSoldier(EMovableType movableType) {
		return getAllSoldiers().contains(movableType);
	}
}
