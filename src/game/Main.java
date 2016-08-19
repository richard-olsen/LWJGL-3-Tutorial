package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

import collision.AABB;
import entity.Player;
import io.Timer;
import io.Window;
import render.Camera;
import render.Shader;
import world.Tile;
import world.TileRenderer;
import world.World;

public class Main {
	public Main() {
		Window.setCallbacks();
		
		
		if(glfwInit() != 1) {
			System.err.println("GLFW Failed to initialize!");
			System.exit(1);
		}
		
		Window window = new Window();
		window.setSize(640, 480);
		window.setFullscreen(false);
		window.createWindow("Game");
		
		GL.createCapabilities();
		
		Camera camera = new Camera(window.getWidth(), window.getHeight());
		glEnable(GL_TEXTURE_2D);
		
		TileRenderer tiles = new TileRenderer();
		
//		float[] vertices = new float[] {
//				-1f, 1f, 0, //TOP LEFT     0
//				1f, 1f, 0,  //TOP RIGHT    1
//				1f, -1f, 0, //BOTTOM RIGHT 2
//				-1f, -1f, 0,//BOTTOM LEFT  3
//		};
//		
//		float[] texture = new float[] {
//				0,0,
//				1,0,
//				1,1,
//				0,1,
//		};
//		
//		int[] indices = new int[] {
//				0,1,2,
//				2,3,0
//		};
//		
//		Model model = new Model(vertices, texture, indices);
		Shader shader = new Shader("shader");
		
		World world = new World("test_level");
		
		Player player = new Player();
		
		double frame_cap = 1.0/60.0;
		
		double frame_time = 0;
		int frames = 0;
		
		double time = Timer.getTime();
		double unprocessed = 0;
		
		while(!window.shouldClose()) {
			boolean can_render = false;
			
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed+=passed;
			frame_time +=passed;
			
			time = time_2;
			
			while(unprocessed >= frame_cap) {
				unprocessed-=frame_cap;
				can_render = true;
				
				if(window.getInput().isKeyReleased(GLFW_KEY_ESCAPE)) {
					glfwSetWindowShouldClose(window.getWindow(), GL_TRUE);
				}
				
				player.update((float)frame_cap, window, camera, world);
				
				world.correctCamera(camera, window);
				
				window.update();
				
				if(frame_time >= 1.0) {
					frame_time = 0;
					System.out.println("FPS: " + frames);
					frames = 0;
				}
			}
			
			if(can_render) {
				glClear(GL_COLOR_BUFFER_BIT);
				
//				shader.bind();
//				shader.setUniform("sampler", 0);
//				shader.setUniform("projection", camera.getProjection().mul(target));
				//model.render();
				//tex.bind(0);
				
				world.render(tiles, shader, camera, window);
				
				player.render(shader, camera);
				
				window.swapBuffers();
				frames++;
			}
		}
		
		glfwTerminate();
	}

	public static void main(String[] args) {
		new Main();
	}

}
