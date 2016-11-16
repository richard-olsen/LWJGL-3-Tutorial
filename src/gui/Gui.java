package gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import assets.Assets;
import render.Camera;
import render.Shader;

public class Gui {
	private Shader shader;
	
	public Gui() {
		shader = new Shader("gui");
	}
	
	public void render(Camera camera) {
		Matrix4f mat = new Matrix4f();
		camera.getUntransformedProjection().scale(87, mat);
		mat.translate(-3, -3, 0);
		shader.bind();
		
		shader.setUniform("projection", mat);
		shader.setUniform("color", new Vector4f(0,0,0,0.4f));
		
		Assets.getModel().render();
	}
}
