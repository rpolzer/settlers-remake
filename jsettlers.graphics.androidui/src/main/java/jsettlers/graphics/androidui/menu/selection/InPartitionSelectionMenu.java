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
package jsettlers.graphics.androidui.menu.selection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jsettlers.common.images.ImageLink;
import jsettlers.common.movable.EMovableType;
import jsettlers.graphics.action.ConvertAction;
import jsettlers.graphics.androidui.R;
import jsettlers.graphics.androidui.utils.OriginalImageProvider;

/**
 * A menu to display while we are in a partition and there is nothing (or only bearers) selected.
 *
 * @author Michael Zangl
 */
public class InPartitionSelectionMenu extends SelectionMenu {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.selection_in_partition, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		loadButton(view, R.id.selection_none_geologist_one, R.id.selection_none_geologist_five, R.id.selection_none_geologist_count,
				R.id.selection_none_geologist_icon, EMovableType.GEOLOGIST, IMAGE_GEOLOGIST);
		loadButton(view, R.id.selection_none_thief_one, R.id.selection_none_thief_five, R.id.selection_none_thief_count,
				R.id.selection_none_thief_icon, EMovableType.THIEF, IMAGE_THIEF);
		loadButton(view, R.id.selection_none_pioneer_one, R.id.selection_none_pioneer_five, R.id.selection_none_pioneer_count,
				R.id.selection_none_pioneer_icon, EMovableType.PIONEER, IMAGE_PIONEER);
	}

	private void loadButton(View view, int oneId, int fiveId, int countId, int iconId, final EMovableType type, ImageLink image) {
		Button one = (Button) view.findViewById(oneId);
		Button five = (Button) view.findViewById(fiveId);
		TextView count = (TextView) view.findViewById(countId);
		ImageView icon = (ImageView) view.findViewById(iconId);
		count.setText(""); // TODO: Count in whole game?
		OriginalImageProvider.get(image).setAsButtonHigh(icon);

		one.setOnClickListener(generateActionListener(new ConvertAction(type, (short) 1), false));
		five.setOnClickListener(generateActionListener(new ConvertAction(type, (short) 5), false));
	}
}
