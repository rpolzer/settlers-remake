/*******************************************************************************
 * Copyright (c) 2015
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.ai.economy;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import jsettlers.ai.construction.BuildingCount;
import jsettlers.ai.highlevel.AiMapInformation;
import jsettlers.common.buildings.EBuildingType;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static jsettlers.common.buildings.EBuildingType.*;
import static jsettlers.common.buildings.EBuildingType.IRONMELT;
import static jsettlers.common.buildings.EBuildingType.WEAPONSMITH;

/**
 * This economy minister is as optimized as possible to create fast and many level 3 soldiers with high combat strength. It builds a longterm economy
 * with 8 lumberjacks first, then mana, food, weapons and gold economy.
 *
 * @author codingberlin
 */
public class WinnerEconomyMinister extends BuildingListEconomyMinister implements EconomyMinister {

	public WinnerEconomyMinister(AiMapInformation aiMapInformation) {
		super();
		initializeBuildingsToBuild(aiMapInformation);
	}

	private void initializeBuildingsToBuild(AiMapInformation aiMapInformation) {
		buildingsToBuild.add(LUMBERJACK);
		buildingsToBuild.add(LUMBERJACK);
		buildingsToBuild.add(SAWMILL);
		buildingsToBuild.add(LUMBERJACK);
		buildingsToBuild.add(FORESTER);
		buildingsToBuild.add(STONECUTTER);
		buildingsToBuild.add(LUMBERJACK);
		buildingsToBuild.add(FORESTER);
		buildingsToBuild.add(LUMBERJACK);
		buildingsToBuild.add(SAWMILL);
		buildingsToBuild.add(LUMBERJACK);
		buildingsToBuild.add(FORESTER);
		buildingsToBuild.add(STONECUTTER);
		buildingsToBuild.add(LUMBERJACK);
		buildingsToBuild.add(LUMBERJACK);
		buildingsToBuild.add(SAWMILL);
		buildingsToBuild.add(FORESTER);
		buildingsToBuild.add(STONECUTTER);
		buildingsToBuild.add(STONECUTTER);
		buildingsToBuild.add(STONECUTTER);
		for (int i = 0; i < aiMapInformation.getNumberOfWineGrower(); i++) {
			buildingsToBuild.add(WINEGROWER);
		}
		buildingsToBuild.add(IRONMELT);
		buildingsToBuild.add(WEAPONSMITH);
		buildingsToBuild.add(BARRACK);
		buildingsToBuild.add(COALMINE);
		buildingsToBuild.add(IRONMELT);
		buildingsToBuild.add(COALMINE);
		buildingsToBuild.add(IRONMELT);
		buildingsToBuild.add(WEAPONSMITH);
		for (int i = 0; i < aiMapInformation.getNumberOfWineGrower(); i++) {
			buildingsToBuild.add(TEMPLE);
		}
		if (aiMapInformation.getNumberOfBigTemples() > 0) {
			buildingsToBuild.add(BIG_TEMPLE);
		}

		int remainingNumberOfWeaponSmiths = aiMapInformation.getNumberOfWeaponSmiths() - 2;
		List<EBuildingType> weaponsBuildings = new ArrayList<>();
		if (remainingNumberOfWeaponSmiths < 4) {
			addGoldBuildings(aiMapInformation, weaponsBuildings);
		}
		for (int i = 0; i < aiMapInformation.getNumberOfWeaponSmiths() - 2; i++) {
			weaponsBuildings.add(COALMINE);
			if (i % 2 == 0) {
				weaponsBuildings.add(IRONMINE);
			}
			weaponsBuildings.add(IRONMELT);
			weaponsBuildings.add(WEAPONSMITH);
			if (i % 4 == 0) {
				weaponsBuildings.add(BARRACK);
			}
			if (i == 3) {
				addGoldBuildings(aiMapInformation, weaponsBuildings);
			}
		}

		List<EBuildingType> foodBuildings = new ArrayList<>();
		for (int i = 0; i < aiMapInformation.getNumberOfFisher(); i++) {
			foodBuildings.add(FISHER);
		}
		for (int i = 0; i < aiMapInformation.getNumberOfFarms(); i++) {
			foodBuildings.add(FARM);
			if (i % 2 == 0) {
				foodBuildings.add(WATERWORKS);
			}
			if (i % 3 == 0) {
				foodBuildings.add(MILL);
			}
			if (i % 3 == 0) {
				foodBuildings.add(BAKER);
				foodBuildings.add(BAKER);
			}
			if (i % 3 == 1) {
				foodBuildings.add(BAKER);
			}
			if (i % 6 == 1 || i % 6 == 2 || i % 6 == 5) {
				foodBuildings.add(PIG_FARM);
			} if (i % 6 == 1) {
				foodBuildings.add(SLAUGHTERHOUSE);
			}
		}

		List<EBuildingType> buildingMaterialBuidlings = new ArrayList<>();
		for (int i = 0; i < aiMapInformation.getNumberOfLumberJacks() - 8; i++) {
			buildingMaterialBuidlings.add(LUMBERJACK);
			if (i % 3 == 1) {
				buildingMaterialBuidlings.add(FORESTER);
			}
			if (i % 3 == 1) {
				buildingMaterialBuidlings.add(SAWMILL);
			}
			if (i % 2 == 1) {
				buildingMaterialBuidlings.add(STONECUTTER);
			}
		}

		for (int i = 0; i < Math.max(foodBuildings.size(), Math.max(buildingMaterialBuidlings.size(), weaponsBuildings.size())); i++) {
			if (i < foodBuildings.size()) {
				buildingsToBuild.add(foodBuildings.get(i));
			}
			if (i < buildingMaterialBuidlings.size()) {
				buildingsToBuild.add(buildingMaterialBuidlings.get(i));
			}
			if (i < weaponsBuildings.size()) {
				buildingsToBuild.add(weaponsBuildings.get(i));
			}
		}
	}

	private void addGoldBuildings(AiMapInformation aiMapInformation, List<EBuildingType> weaponsBuildings) {
		if (aiMapInformation.getNumberOfGoldMelts() > 0) {
			weaponsBuildings.add(GOLDMINE);
			for (int ii = 0; ii < aiMapInformation.getNumberOfGoldMelts(); ii++) {
				weaponsBuildings.add(GOLDMELT);
				weaponsBuildings.add(STOCK);
			}
		}
	}

	private BuildingCount take(EBuildingType buildingType, List<BuildingCount> buildingCounts) {
		for (BuildingCount buildingCount : buildingCounts) {
			if (buildingCount.buildingType == buildingType) {
				return buildingCount;
			}
		}
		return null;
	}

	@Override

	public int getNumberOfParallelConstructionSides() {
		return 5;
	}

	@Override
	public List<EBuildingType> getBuildingsToBuild() {
		return buildingsToBuild;
	}

	@Override
	public byte getMidGameNumberOfStoneCutters() {
		return 5;
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
