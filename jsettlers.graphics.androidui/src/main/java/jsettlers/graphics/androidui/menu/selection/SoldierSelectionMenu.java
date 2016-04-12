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
package jsettlers.graphics.androidui.menu.selection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import jsettlers.common.images.EImageLinkType;
import jsettlers.common.images.ImageLink;
import jsettlers.common.images.OriginalImageLink;
import jsettlers.common.movable.EMovableType;
import jsettlers.common.movable.ESoldierLevel;
import jsettlers.common.movable.ESoldierType;
import jsettlers.common.selectable.ISelectionSet;
import jsettlers.graphics.action.ExecutableAction;
import jsettlers.graphics.androidui.R;
import jsettlers.graphics.androidui.actions.MoveToOnClick;
import jsettlers.graphics.androidui.menu.AndroidMenu;
import jsettlers.graphics.androidui.utils.OriginalImageProvider;

/**
 * @author Michael Zangl
 */
public class SoldierSelectionMenu extends SelectionMenu {

	private static final ImageLink IMAGE_SWORDSMAN = new OriginalImageLink(EImageLinkType.GUI, 14, 213);
	private static final ImageLink IMAGE_BOWMAN = new OriginalImageLink(EImageLinkType.GUI, 14, 219);
	private static final ImageLink IMAGE_PIKEMAN = new OriginalImageLink(EImageLinkType.GUI, 14, 216);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.selection_soldier, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		loadButton(view, R.id.selection_soldier_swordsmen, R.id.selection_soldier_swordsmen_button, ESoldierType.SWORDSMAN, IMAGE_SWORDSMAN);
		loadButton(view, R.id.selection_soldier_bowmen, R.id.selection_soldier_bowmen_button, ESoldierType.BOWMAN, IMAGE_BOWMAN);
		loadButton(view, R.id.selection_soldier_pikemen, R.id.selection_soldier_pikemen_button, ESoldierType.PIKEMAN, IMAGE_PIKEMAN);

		loadMoveButton(view, R.id.selection_soldier_move);
		loadAttackButton(view, R.id.selection_soldier_attack);
	}

	private void loadMoveButton(View view, int buttonId) {
		Button button = (Button) view.findViewById(buttonId);

		button.setOnClickListener(generateMoveToListener(true, false));
	}

	private void loadAttackButton(View view, int buttonId) {
		Button button = (Button) view.findViewById(buttonId);

		button.setOnClickListener(generateMoveToListener(false, true));
	}

	private void loadButton(View view, int textId, int buttonId, ESoldierType type, ImageLink image) {
		TextView text = (TextView) view.findViewById(textId);
		ImageButton button = (ImageButton) view.findViewById(buttonId);

		OriginalImageProvider.get(image).setAsButton(button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO: Update selection...
			}
		});

		int[] count = new int[ESoldierLevel.values().length];
		for (EMovableType m : type.getAllOfType()) {
			count[m.getLevel().ordinal()] += selection.getMovableCount(m);
		}

		int max = count.length - 1;
		while (max >= 1 && count[max] == 0) {
			// Skip high level zeros.
			max--;
		}
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < max; i++) {
			if (i != 0) {
				string.append(" ");
			}
			string.append(count[i]).append(string);
		}
		text.setText(string.toString());
	}
}
