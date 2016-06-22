package entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
	public Vector3f pos;
	public Vector3f scale;
	
	public Transform() {
		pos = new Vector3f();
		scale = new Vector3f(1,1,1);
	}
	
	public Matrix4f getProjection(Matrix4f target) {
		target.scale(scale);
		target.translate(pos);
		return target;
	}
}
