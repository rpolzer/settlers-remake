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
package jsettlers.graphics.androidui.actions;

import jsettlers.common.menu.action.EActionType;
import jsettlers.common.menu.action.IAction;
import jsettlers.graphics.action.MoveToAction;
import jsettlers.graphics.action.PointAction;
import jsettlers.graphics.localization.Labels;

public class MoveToOnClick extends ContextAction {

	private final boolean auto;
	private final boolean force;
	private final boolean work;

	public MoveToOnClick() {
		this(true, false, true);
	}

	public MoveToOnClick(boolean force, boolean work) {
	this(false, force, work);
	}

	private MoveToOnClick(boolean auto, boolean force, boolean work) {
		this.auto = auto;
		this.force = force;
		this.work = work;
	}

	@Override
	public String getDesciption() {
		return Labels.getString("click_to_move");
	}

	@Override
	public IAction replaceAction(IAction action) {
		if (auto) {
			//TODO: click duration?
		}
		if (action.getActionType() == EActionType.SELECT_POINT) {
			return new MoveToAction(((PointAction) action).getPosition(), force, work);
		}
		return super.replaceAction(action);
	}

}
