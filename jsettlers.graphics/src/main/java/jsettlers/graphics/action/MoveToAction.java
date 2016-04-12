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
package jsettlers.graphics.action;

import jsettlers.common.menu.action.EActionType;
import jsettlers.common.position.ShortPoint2D;

/**
 * This action lets movables walk to a given position.
 *
 * @author Michael Zangl
 */
public class MoveToAction extends PointAction {
	private final boolean force;
	private final boolean work;

	/**
	 * Create a new MoveToAction.
	 * 
	 * @param position
	 *            The position the user clicked at.
	 * @param force
	 *            If we should force a go to there.
	 * @param force
	 *            If we should start working when going there
	 */
	public MoveToAction(ShortPoint2D position, boolean force, boolean work) {
		super(EActionType.MOVE_TO, position);
		this.force = force;
		this.work = work;
	}

	public boolean isForce() {
		return force;
	}

	public boolean isWork() {
		return work;
	}
}
