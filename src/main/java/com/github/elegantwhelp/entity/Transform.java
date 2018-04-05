package com.github.elegantwhelp.entity;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Transform {
	public Vector2f pos;
	public Vector2f scale;
	
	public Transform() {
		pos = new Vector2f();
		scale = new Vector2f(1, 1);
	}
	
	public Matrix4f getProjection(Matrix4f target) {
		target.translate(pos.x, pos.y, 0);
		target.scale(scale.x, scale.y, 1);
		return target;
	}
}
