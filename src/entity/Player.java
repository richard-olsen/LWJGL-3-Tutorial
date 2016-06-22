package entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import io.Window;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;
import world.World;

public class Player {
	private Model model;
	private Texture texture;
	private Transform transform;
	
	public Player() {
		float[] vertices = new float[] {
				-1f, 1f, 0, //TOP LEFT     0
				1f, 1f, 0,  //TOP RIGHT    1
				1f, -1f, 0, //BOTTOM RIGHT 2
				-1f, -1f, 0,//BOTTOM LEFT  3
		};
		
		float[] texture = new float[] {
				0,0,
				1,0,
				1,1,
				0,1,
		};
		
		int[] indices = new int[] {
				0,1,2,
				2,3,0
		};
		
		model = new Model(vertices, texture, indices);
		this.texture = new Texture("test.png");
		
		transform = new Transform();
		transform.scale = new Vector3f(16,16,1);
	}
	
	public void update(float delta, Window window, Camera camera, World world) {
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) 
			transform.pos.add(new Vector3f(-10*delta, 0, 0));
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) 
			transform.pos.add(new Vector3f(10*delta, 0, 0));
		
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) 
			transform.pos.add(new Vector3f(0, 10*delta, 0));
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) 
			transform.pos.add(new Vector3f(0, -10*delta, 0));
		
		camera.setPosition(transform.pos.mul(-world.getScale(), new Vector3f()));
	}
	
	public void render(Shader shader, Camera camera) {
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", transform.getProjection(camera.getProjection()));
		texture.bind(0);
		model.render();
	}
}
