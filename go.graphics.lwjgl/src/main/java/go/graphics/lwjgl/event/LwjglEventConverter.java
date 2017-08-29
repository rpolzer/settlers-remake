/*******************************************************************************
 * Copyright (c) 2017
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
package go.graphics.lwjgl.event;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import go.graphics.UIPoint;
import go.graphics.event.GOEventHandlerProvider;
import go.graphics.event.interpreter.AbstractEventConverter;

import static java.awt.SystemColor.window;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

/**
 * This class listens to lwjgl events and sends them to handlers.
 * 
 * @author michael
 */
public class LwjglEventConverter extends AbstractEventConverter {

	private static final int MOUSE_MOVE_TRESHOLD = 10;

	private static final double MOUSE_TIME_TRSHOLD = 5;

	private UIPoint mousePosition;
	private boolean panInProgress = false;
	private boolean drawInProgress = false;
	private boolean hoverInProgress = false;
	private boolean zoomInProgress = false;
	private int windowHeight;
	private static final float MINIMUM_ZOOM = .2f;
	private static final float MAXIMUM_ZOOM = 3f;
	private float zoom = 1;

	/**
	 * Creates a new event converter, that listens to lwjgl events.
	 * 
	 * @param component
	 *            The component.
	 * @param provider
	 *            THe provider to which to send the events.
	 */
	public LwjglEventConverter(GOEventHandlerProvider provider) {
		super(provider);

		addReplaceRule(new EventReplacementRule(ReplacableEvent.DRAW, Replacement.COMMAND_SELECT, MOUSE_TIME_TRSHOLD, MOUSE_MOVE_TRESHOLD));
		addReplaceRule(new EventReplacementRule(ReplacableEvent.PAN, Replacement.COMMAND_ACTION, MOUSE_TIME_TRSHOLD, MOUSE_MOVE_TRESHOLD));
	}

	public void setWindowHeight(int height) {
		windowHeight = height;
	}

	public GLFWKeyCallback getKeyCallback() {
		return new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				String text = textForAction(key);
				if (action == GLFW.GLFW_RELEASE) {
					endKeyEvent(text);
				} else if (action == GLFW.GLFW_PRESS) {
					startKeyEvent(text);
				}
			}
		};
	}

	public GLFWScrollCallback getScrollCallback() {
		return new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double x, double y) {
				if (!zoomInProgress) {
					startZoom();
					zoomInProgress = true;
				} else {
					zoom *= (10 + y) / 10;
					if (zoom > MAXIMUM_ZOOM) {
						zoom = MAXIMUM_ZOOM;
					} else if (zoom < MINIMUM_ZOOM) {
						zoom = MINIMUM_ZOOM;
					}
					updateZoomFactor(zoom, mousePosition);
				}
			}
		};

	}

	public GLFWCursorPosCallback getMousePositionCallback() {
		return new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double x, double y) {
				mousePosition = new UIPoint(x, windowHeight - y);
				if (!hoverInProgress) {
					hoverInProgress = true;
					startHover(mousePosition);
				}
				if (drawInProgress) {
					updateDrawPosition(mousePosition);
				} else if (panInProgress) {
					updatePanPosition(mousePosition);
				} else {
					updateHoverPosition(mousePosition);
				}
			}
		};

	}

	public GLFWMouseButtonCallback getMouseButtonCallback() {
		return new GLFWMouseButtonCallback() {
			@Override
			public void invoke( long window, int button, int action, int mods) {
				if (zoomInProgress) {
					endZoomEvent(zoom, mousePosition);
					zoomInProgress = false;
				}
				switch(button) {
					case GLFW.GLFW_MOUSE_BUTTON_1:
						if (action == GLFW.GLFW_PRESS) {
							startDraw(mousePosition);
							drawInProgress = true;
						} else {
							endDraw(mousePosition);
							drawInProgress = false;
						}
						break;
					case GLFW.GLFW_MOUSE_BUTTON_2:
					case GLFW.GLFW_MOUSE_BUTTON_3:
						if (action == GLFW.GLFW_PRESS) {
							startPan(mousePosition);
							panInProgress = true;
						} else {
							endPan(mousePosition);
							panInProgress = false;
						}
						break;
				}
			}
		};
	}

	protected String textForAction(int key) {
		String text;
		switch (key) {
			case GLFW.GLFW_KEY_LEFT:
				text = "LEFT";
				break;
			case GLFW.GLFW_KEY_RIGHT:
				text = "RIGHT";
				break;
			case GLFW.GLFW_KEY_DOWN:
				text = "DOWN";
				break;
			case GLFW.GLFW_KEY_UP:
				text = "UP";
				break;
			case GLFW.GLFW_KEY_PAUSE:
				text = "PAUSE";
				break;
			case GLFW.GLFW_KEY_F1:
				text = "F1";
				break;
			case GLFW.GLFW_KEY_F2:
				text = "F2";
				break;
			case GLFW.GLFW_KEY_F3:
				text = "F3";
				break;
			case GLFW.GLFW_KEY_F4:
				text = "F4";
				break;
			case GLFW.GLFW_KEY_F5:
				text = "F5";
				break;
			case GLFW.GLFW_KEY_F6:
				text = "F6";
				break;
			case GLFW.GLFW_KEY_F7:
				text = "F7";
				break;
			case GLFW.GLFW_KEY_F8:
				text = "F8";
				break;
			case GLFW.GLFW_KEY_F9:
				text = "F9";
				break;
			case GLFW.GLFW_KEY_F10:
				text = "F10";
				break;
			case GLFW.GLFW_KEY_F11:
				text = "F11";
				break;
			case GLFW.GLFW_KEY_F12:
				text = "F12";
				break;
			case GLFW.GLFW_KEY_KP_ADD:
				//TODO: Normal add missing?
				text = "+";
				break;
			case GLFW.GLFW_KEY_KP_SUBTRACT:
			case GLFW.GLFW_KEY_MINUS:
				text = "-";
				break;
			case GLFW.GLFW_KEY_DELETE:
				text = "DELETE";
				break;
			case GLFW.GLFW_KEY_SPACE:
				text = " ";
				break;
			case GLFW.GLFW_KEY_ESCAPE:
				text = "ESCAPE";
				break;
			case GLFW.GLFW_KEY_BACKSPACE:
				text = "BACK_SPACE";
				break;
			case GLFW.GLFW_KEY_P:
				text = "P";
				break;
			case GLFW.GLFW_KEY_Q:
				text = "Q";
				break;
			default:
				text = "";
			}
		return text;
	}

}
