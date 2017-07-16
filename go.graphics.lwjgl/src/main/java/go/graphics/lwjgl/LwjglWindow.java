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

import go.graphics.UIPoint;
import go.graphics.area.Area;
import go.graphics.lwjgl.event.LwjglEventConverter;
import go.graphics.lwjgl.opengl.LwjglDrawContext;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * This is a window that consists of exactly one area.
 * 
 * @author michael
 *
 */
public class LwjglWindow {
	private final Area area;
	private final long window;
	private LwjglEventConverter events;

	public LwjglWindow(Area area, Rectangle position, String title) {
		setUpLwjgl();
		
		this.area = area;
		window = GLFW.glfwCreateWindow(position.width, position.height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			throw new LwjglException("Could not create window");
		}

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);
		
		events = new LwjglEventConverter(area);
		GLFW.glfwSetKeyCallback(window, events.getKeyCallback());

		GLFW.glfwSetWindowPos(window, position.x, position.y);

		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);
		
		GLFW.glfwShowWindow(window);
		
		new Thread(new LwjglLoop()).start();
	}

	private void setUpLwjgl() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!GLFW.glfwInit()) {
			throw new LwjglException("Could not initialize glfw");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
	}

	public void close() {
		GLFW.glfwSetWindowShouldClose(window, true);
	}

	public Area getArea() {
		return area;
	}
	
	private class LwjglLoop extends Thread {
		public LwjglLoop() {
			super("lwjgl-loop");
		}
		
		public void run() {
			try {
				LwjglDrawContext context = initialize();
	
				while (!GLFW.glfwWindowShouldClose(window)) {
					mainLoop(context);
				}
				
				terminate();
			} catch (Throwable t) {
				t.printStackTrace();
				// TODO: Notify main game that we crashed
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
				glfwGetWindowSize(window, pWidth, pHeight);

				area.setWidth(pWidth.get(0));
				area.setHeight(pHeight.get(0));
			}
			context.updateWindowSize(area.getWidth(), area.getHeight());
			context.startFrame();
			
			area.drawArea(context);
			DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
			GLFW.glfwGetCursorPos(window, b1, b2);
			events.updateMouse(new UIPoint(b1.get(), area.getHeight() - b2.get()), 
					glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) != 0, 
					glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_2) != 0);

			glfwSwapBuffers(window);

			// Keys, ...
			glfwPollEvents();
		}

		private void terminate() {
			Callbacks.glfwFreeCallbacks(window);
			GLFW.glfwDestroyWindow(window);
		
			// Terminate GLFW and free the error callback
			GLFW.glfwTerminate();
			GLFW.glfwSetErrorCallback(null).free();
		}
	}
}
