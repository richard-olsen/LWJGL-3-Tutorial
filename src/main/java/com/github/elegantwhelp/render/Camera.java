package com.github.elegantwhelp.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
	private Vector2f position;
	private Matrix4f projection;
	
	private Matrix4f transformedProjection;
	private Vector3f transformedPosition;
	
	public Camera(int width, int height) {
		position = new Vector2f(0,0);
		setProjection(width, height);
		
		transformedProjection = new Matrix4f();
		transformedPosition = new Vector3f();
	}
	
	public void setProjection(int width, int height) {
		projection = new Matrix4f().setOrtho2D(-width/2, width/2, height/2, -height/2);
	}
	
	public void setPosition(Vector2f position) {
		this.position.set(position);
	}

	public void addPosition(Vector2f position) {
		this.position.add(position);
	}
	
	public Vector2f getPosition() { return position; }
	
	public Matrix4f getUntransformedProjection() {
		return projection;
	}
	
	public Matrix4f getTransformedProjection() {
		transformedPosition.set(position, 0);
		transformedPosition.mul(-1);
		return projection.translate(transformedPosition, transformedProjection);
	}
}
