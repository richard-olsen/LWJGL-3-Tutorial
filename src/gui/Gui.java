package gui;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import assets.Assets;
import io.Window;
import render.Camera;
import render.Shader;
import render.TileSheet;

public class Gui {
	private Shader shader;
	private Camera camera;
	private TileSheet sheet;
	
	public Gui(Window window) {
		shader = new Shader("gui");
		camera = new Camera(window.getWidth(), window.getHeight());
		sheet = new TileSheet("test.png", 3);
	}
	
	public void resizeCamera(Window window) {
		camera.setProjection(window.getWidth(), window.getHeight());
	}
	
	public void render() {
		Matrix4f mat = new Matrix4f();
		camera.getProjection().scale(64, mat);
		mat.translate(-2, -2, 0);
		shader.bind();
		
		shader.setUniform("projection", mat);
		
		sheet.bindTile(shader, 3);
		//shader.setUniform("color", new Vector4f(0,0,0,0.4f));
		
		Assets.getModel().render();
	}
}
