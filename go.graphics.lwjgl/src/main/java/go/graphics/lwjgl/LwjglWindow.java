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
package go.graphics.lwjgl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.awt.Rectangle;
import java.nio.IntBuffer;

import go.graphics.area.Area;
import go.graphics.lwjgl.event.LwjglEventConverter;
import go.graphics.lwjgl.opengl.LwjglDrawContext;

/**
 * This is a window that consists of exactly one area.
 * 
 * @author michael
 *
 */
public class LwjglWindow {
	private final Area area;
	private LwjglEventConverter events;
	private GLFWKeyCallback keyCallback;
	private GLFWCharCallback charCallback;
	private GLFWScrollCallback scrollCallback;
	private GLFWCursorPosCallback mousePositionCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	private static boolean initialized;
	private final long window;

	public LwjglWindow(Area area, Rectangle position, String title) {
		if (!initialized) {
			setUpLwjgl();
		}
		
		this.area = area;
		
		// Only works once for now
		window = GLFW.glfwCreateWindow(position.width, position.height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			throw new LwjglException("Could not create window");
		}

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);
		
		events = new LwjglEventConverter(area);
		keyCallback = events.getKeyCallback();
		GLFW.glfwSetKeyCallback(window, keyCallback);
		charCallback = events.getCharCallback();
		GLFW.glfwSetCharCallback(window, charCallback);
		scrollCallback = events.getScrollCallback();
		GLFW.glfwSetScrollCallback(window, scrollCallback);
		mousePositionCallback = events.getMousePositionCallback();
		GLFW.glfwSetCursorPosCallback(window, mousePositionCallback);
		mouseButtonCallback = events.getMouseButtonCallback();
		GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback);

		GLFW.glfwSetWindowPos(window, position.x, position.y);
		
		GLFW.glfwShowWindow(window);
		
		new Thread(new LwjglLoop()).start();
	}

	private static void setUpLwjgl() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!GLFW.glfwInit()) {
			throw new LwjglException("Could not initialize glfw");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
		initialized = true;
	}

	public void close() {
		GLFW.glfwSetWindowShouldClose(window, true);
	}
	
	protected void onClose() {
	}

	public Area getArea() {
		return area;
	}
	
	private class LwjglLoop extends Thread {

		public LwjglLoop() {
			super("lwjgl-loop");
		}

		public void run() {
			LwjglDrawContext context = initialize();
			try {
				while (!GLFW.glfwWindowShouldClose(window)) {
					mainLoop(context);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				terminate(context);
			}
		}

		private LwjglDrawContext initialize() {
			GLFW.glfwMakeContextCurrent(window);
			GLCapabilities capabilities = GL.createCapabilities(false);
		
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			return new LwjglDrawContext(capabilities);
		}

		private void mainLoop(LwjglDrawContext context) {
			try ( MemoryStack stack = MemoryStack.stackPush() ) {
				IntBuffer pWidth = stack.mallocInt(1); // int*
				IntBuffer pHeight = stack.mallocInt(1); // int*
				// Get the window size passed to glfwCreateWindow
				GLFW.glfwGetWindowSize(window, pWidth, pHeight);
				events.setWindowHeight(pHeight.get(0));
				area.setWidth(pWidth.get(0));
				area.setHeight(pHeight.get(0));
			}
			context.updateWindowSize(area.getWidth(), area.getHeight());
			context.startFrame();
			
			area.drawArea(context);
			GLFW.glfwSwapBuffers(window);
			GLFW.glfwPollEvents();
		}

		private void terminate(LwjglDrawContext context) {
			context.disposeAll();

			GLFW.glfwSetKeyCallback(window, null);
			keyCallback.free();
			GLFW.glfwSetCharCallback(window, null);
			charCallback.free();
			GLFW.glfwSetScrollCallback(window, null);
			scrollCallback.free();
			GLFW.glfwSetCursorPosCallback(window, null);
			mousePositionCallback.free();
			GLFW.glfwSetMouseButtonCallback(window, null);
			mouseButtonCallback.free();
			GLFW.glfwDestroyWindow(window);
		
			// Terminate GLFW and free the error callback
			//GLFW.glfwTerminate();
			//GLFW.glfwSetErrorCallback(null).free();
			
			onClose();
		}
	}
}
