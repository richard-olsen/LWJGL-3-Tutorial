package com.github.elegantwhelp.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Vector3f position;
	private Matrix4f projection;
	
	public Camera(int width, int height) {
		position = new Vector3f(0,0,0);
		setProjection(width, height);
	}
	
	public void setProjection(int width, int height) {
		projection = new Matrix4f().setOrtho2D(-width/2, width/2, height/2, -height/2);
	}
	
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	public void addPosition(Vector3f position) {
		this.position.add(position);
	}
	
	public Vector3f getPosition() { return position; }
	
	public Matrix4f getUntransformedProjection() {
		return projection;
	}
	
	public Matrix4f getTransformedProjection() {
		return projection.translate(position.mul(-1, new Vector3f()), new Matrix4f());
	}
}
