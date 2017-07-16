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
package go.graphics.lwjgl.event;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import go.graphics.UIPoint;
import go.graphics.event.GOEventHandlerProvider;
import go.graphics.event.interpreter.AbstractEventConverter;

/**
 * This class listens to swing events, converts them to a go events and sends them to handlers.
 * 
 * @author michael
 */
public class LwjglEventConverter extends AbstractEventConverter {

	private static final int MOUSE_MOVE_TRESHOLD = 10;

	private static final double MOUSE_TIME_TRSHOLD = 5;

	/**
	 * Are we currently panning with button 3?
	 */
	private boolean panWithButton3;

	private int scaleFactor = 1;

	private boolean oldButton1;

	private boolean oldButton2;

	/**
	 * Creates a new event converter, that converts swing events to go events.
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
			case GLFW.GLFW_KEY_Q:
				text = "Q";
				break;
			default:
				text = "";
			}
		return text;
	}

	public void updateMouse(UIPoint p, boolean button1, boolean button2) {
		updateHoverPosition(p);
		updateDrawPosition(p);
		updatePanPosition(p);
		
		if (button1 && !oldButton1) {
			startDraw(p);
		} else if (!button1 && oldButton1) {
			endDraw(p);
		}

		if (button2 && !oldButton2) {
			startPan(p);
		} else if (!button2 && oldButton2) {
			endPan(p);
		}
		
		oldButton1 = button1;
		oldButton2 = button2;
	}

//	private UIPoint convertToLocal(MouseEvent e) {
//		return new UIPoint(e.getX() * scaleFactor, (e.getComponent().getHeight() - e.getY()) * scaleFactor);
//
//	}
//
//	private void updateScaleFactor(Component component) {
//		GraphicsConfiguration config = component.getGraphicsConfiguration();
//		if (config == null) {
//			return;
//		}
//
//		GraphicsDevice myScreen = config.getDevice();
//
//		try {
//			Field field = myScreen.getClass().getDeclaredField("scale");
//			if (field == null) {
//				return;
//			}
//			field.setAccessible(true);
//			Object scaleOfField = field.get(myScreen);
//			if (scaleOfField instanceof Integer) {
//				scaleFactor = ((Integer) scaleOfField).intValue();
//			}
//		} catch (NoSuchFieldException exception) {
//			// if there is no Field scale then we have a scale factor of 1
//			// this is expected for Oracle JRE < 1.7.0_u40
//		} catch (Exception exception) {
//			exception.printStackTrace();
//		}
//	}
//
//	@Override
//	public void mouseEntered(MouseEvent e) {
//		startHover(convertToLocal(e));
//	}
//
//	@Override
//	public void mouseMoved(MouseEvent e) {
//		updateHoverPosition(convertToLocal(e));
//	}
//
//	@Override
//	public void mouseExited(MouseEvent e) {
//		endHover(convertToLocal(e));
//	}
//
//	@Override
//	public void mouseClicked(MouseEvent e) {
//	}
//
//	@Override
//	public void mousePressed(MouseEvent e) {
//		int mouseButton = e.getButton();
//		UIPoint local = convertToLocal(e);
//		if (mouseButton == MouseEvent.BUTTON1) {
//			startDraw(local);
//		} else {
//			boolean isPanClick = mouseButton == MouseEvent.BUTTON2 || mouseButton == MouseEvent.BUTTON3;
//			if (isPanClick) {
//				startPan(local);
//				panWithButton3 = mouseButton == MouseEvent.BUTTON3;
//			}
//		}
//	}
//
//	@Override
//	public void mouseDragged(MouseEvent e) {
//		UIPoint local = convertToLocal(e);
//		updateDrawPosition(local);
//		updatePanPosition(local);
//		updateHoverPosition(local);
//	}
//
//	@Override
//	public void mouseReleased(MouseEvent e) {
//		UIPoint local = convertToLocal(e);
//		if (e.getButton() == MouseEvent.BUTTON1) {
//			endDraw(local);
//
//		} else if (panWithButton3 && e.getButton() == MouseEvent.BUTTON3) {
//			endPan(local);
//
//		} else if (!panWithButton3 && e.getButton() == MouseEvent.BUTTON2) {
//			endPan(local);
//		}
//	}
//
//	@Override
//	public void keyPressed(KeyEvent e) {
//		String text = getKeyName(e);
//		startKeyEvent(text);
//		/*
//		 * if (ongoingKeyEvent == null) { if (e.getKeyCode() == GLFW.GLFW_KEY_ESCAPE) { ongoingKeyEvent.setHandler(getCancelHandler()); } else if
//		 * (e.getKeyCode() == GLFW.GLFW_KEY_UP) { ongoingKeyEvent.setHandler(getPanHandler(0, -KEYPAN)); } else if (e.getKeyCode() == GLFW.GLFW_KEY_DOWN)
//		 * { ongoingKeyEvent.setHandler(getPanHandler(0, KEYPAN)); } else if (e.getKeyCode() == GLFW.GLFW_KEY_LEFT) {
//		 * ongoingKeyEvent.setHandler(getPanHandler(KEYPAN, 0)); } else if (e.getKeyCode() == GLFW.GLFW_KEY_RIGHT) {
//		 * ongoingKeyEvent.setHandler(getPanHandler(-KEYPAN, 0)); }
//		 * 
//		 * provider.handleEvent(ongoingKeyEvent);
//		 * 
//		 * ongoingKeyEvent.started(); }
//		 */
//	}
//
//	private String getKeyName(KeyEvent e) {
//	}
//
//	@Override
//	public void keyReleased(KeyEvent e) {
//		endKeyEvent(getKeyName(e));
//	}
//
//	@Override
//	public void keyTyped(KeyEvent e) {
//	}
//
//	@Override
//	public void mouseWheelMoved(MouseWheelEvent e) {
//		float factor = (float) Math.exp(-e.getUnitsToScroll() / 20.0);
//		startZoom();
//		endZoomEvent(factor, convertToLocal(e));
//	}
//
//	@Override
//	public void componentResized(ComponentEvent e) {
//	}
//
//	@Override
//	public void componentMoved(ComponentEvent componentEvent) {
//		updateScaleFactor(componentEvent.getComponent());
//	}
//
//	@Override
//	public void componentShown(ComponentEvent componentEvent) {
//		updateScaleFactor(componentEvent.getComponent());
//	}
//
//	@Override
//	public void componentHidden(ComponentEvent e) {
//	}
//
//	@Override
//	public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
//		Component component = hierarchyEvent.getComponent();
//		privateRegisterComponentListenerToParentWindowOf(component, component);
//	}
//
//	void privateRegisterComponentListenerToParentWindowOf(Component component, Component childComponent) {
//		if (component == null) {
//			return;
//		} else if (component instanceof Window) {
//			updateScaleFactor(component);
//			component.addComponentListener(this);
//			childComponent.removeComponentListener(this);
//		} else {
//			privateRegisterComponentListenerToParentWindowOf(component.getParent(), childComponent);
//		}
//	}
}
