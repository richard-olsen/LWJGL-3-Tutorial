package com.github.elegantwhelp.io;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

public class Input {
	private long window;
	
	private boolean keys[];
	
	private static Vector2f mousePos = new Vector2f();
	private static double[] x = new double[1], y = new double[1];
	private static int[] winWidth = new int[1], winHeight = new int[1];
	
	public Input(long window) {
		this.window = window;
		this.keys = new boolean[GLFW_KEY_LAST];
		for (int i = 0; i < GLFW_KEY_LAST; i++)
			keys[i] = false;
	}
	
	public boolean isKeyDown(int key) {
		return glfwGetKey(window, key) == 1;
	}
	
	public boolean isKeyPressed(int key) {
		return (isKeyDown(key) && !keys[key]);
	}
	
	public boolean isKeyReleased(int key) {
		return (!isKeyDown(key) && keys[key]);
	}
	
	public boolean isMouseButtonDown(int button) {
		return glfwGetMouseButton(window, button) == 1;
	}
	
	public Vector2f getMousePosition() {
		glfwGetCursorPos(window, x, y);
		
		glfwGetWindowSize(window, winWidth, winHeight);
		
		mousePos.set((float) x[0] - (winWidth[0] / 2.0f), -((float) y[0] - (winHeight[0] / 2.0f)));
		
		return mousePos;
	}
	
	public void update() {
		for (int i = 32; i < GLFW_KEY_LAST; i++)
			keys[i] = isKeyDown(i);
	}
}
