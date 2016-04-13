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

import jsettlers.common.movable.EMovableType;
import jsettlers.graphics.action.ConvertAction;
import jsettlers.graphics.androidui.R;

/**
 * @author Michael Zangl
 */
public class SpecialistSelectionMenu extends SelectionMenu {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.selection_specialist, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		loadButton(view, R.id.selection_specialist_geologist, R.id.selection_specialist_geologist_button,
				selection.getMovableCount(EMovableType.GEOLOGIST) + "", IMAGE_GEOLOGIST);
		loadButton(view, R.id.selection_specialist_thief, R.id.selection_specialist_thief_button, selection.getMovableCount(EMovableType.THIEF) + "",
				IMAGE_THIEF);
		loadButton(view, R.id.selection_specialist_pioneer, R.id.selection_specialist_pioneer_button,
				selection.getMovableCount(EMovableType.PIONEER) + "", IMAGE_PIONEER);

		loadMoveButton(view, R.id.selection_specialist_move);
		loadDoWorkButton(view, R.id.selection_specialist_work);

		Button convert = (Button) view.findViewById(R.id.selection_specialist_pioneer_convert);
		convert.setOnClickListener(generateActionListener(new ConvertAction(EMovableType.BEARER, Short.MAX_VALUE), false));

	}
}
