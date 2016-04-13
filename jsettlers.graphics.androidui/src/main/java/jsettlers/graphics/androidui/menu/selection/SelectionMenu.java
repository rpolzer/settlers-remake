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

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import jsettlers.common.images.EImageLinkType;
import jsettlers.common.images.ImageLink;
import jsettlers.common.images.OriginalImageLink;
import jsettlers.common.selectable.ISelectionSet;
import jsettlers.graphics.action.ExecutableAction;
import jsettlers.graphics.androidui.actions.MoveToOnClick;
import jsettlers.graphics.androidui.menu.AndroidMenu;
import jsettlers.graphics.androidui.utils.OriginalImageProvider;

/**
 * @author Michael Zangl
 */
public abstract class SelectionMenu extends AndroidMenu {
	static final ImageLink IMAGE_GEOLOGIST = new OriginalImageLink(EImageLinkType.GUI, 14, 192);
	static final ImageLink IMAGE_THIEF = new OriginalImageLink(EImageLinkType.GUI, 14, 189);
	static final ImageLink IMAGE_PIONEER = new OriginalImageLink(EImageLinkType.GUI, 14, 210);

	static final ImageLink IMAGE_SWORDSMAN = new OriginalImageLink(EImageLinkType.GUI, 14, 213);
	static final ImageLink IMAGE_BOWMAN = new OriginalImageLink(EImageLinkType.GUI, 14, 219);
	static final ImageLink IMAGE_PIKEMAN = new OriginalImageLink(EImageLinkType.GUI, 14, 216);
	static final ImageLink IMAGE_PRIEST = new OriginalImageLink(EImageLinkType.GUI, 14, 201);
	static final ImageLink IMAGE_MANGE = new OriginalImageLink(EImageLinkType.GUI, 14, 240);

	protected ISelectionSet selection = ISelectionSet.EMPTY;

	public void setSelection(ISelectionSet selection) {
		this.selection = selection;
	}

	protected View.OnClickListener generateMoveToListener(final boolean force, final boolean work) {
		return generateActionListener(new ExecutableAction() {
			@Override
			public void execute() {
				setActiveAction(new MoveToOnClick(force, work));
			}
		}, true);
	}

	protected void loadMoveButton(View view, int buttonId) {
		Button button = (Button) view.findViewById(buttonId);

		button.setOnClickListener(generateMoveToListener(true, false));
	}

	protected void loadDoWorkButton(View view, int buttonId) {
		Button button = (Button) view.findViewById(buttonId);

		button.setOnClickListener(generateMoveToListener(false, true));
	}

	protected void loadButton(View view, int textId, int buttonId, String label, ImageLink image) {
		TextView text = (TextView) view.findViewById(textId);
		ImageButton button = (ImageButton) view.findViewById(buttonId);

		OriginalImageProvider.get(image).setAsButtonHigh(button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO: Update selection...
			}
		});

		text.setText(label);
	}
}
