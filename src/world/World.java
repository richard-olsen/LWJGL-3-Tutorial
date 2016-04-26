package world;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import io.Window;
import render.Camera;
import render.Shader;

public class World {
	private byte[] tiles;
	private int width;
	private int height;
	private int scale;
	
	private Matrix4f world;
	
	public World() {
		width = 64;
		height = 64;
		scale = 16;
		
		tiles = new byte[width * height];
		
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(scale);
	}
	
	public void render(TileRenderer render, Shader shader, Camera cam) {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				render.renderTile(tiles[j + i * width], j, -i, shader, world, cam);
			}
		}
	}
	
	public void correctCamera(Camera camera, Window window) {
		Vector3f pos = camera.getPosition();
		
		int w = -width * scale * 2;
		int h = height * scale * 2;
		
		if(pos.x > -(window.getWidth()/2)+scale) 
			pos.x = -(window.getWidth()/2)+scale;
		if(pos.x < w + (window.getWidth()/2) + scale)
			pos.x = w + (window.getWidth()/2) + scale;
		
		if(pos.y < (window.getHeight()/2)-scale)
			pos.y = (window.getHeight()/2)-scale;
		if(pos.y > h-(window.getHeight()/2)-scale)
			pos.y = h-(window.getHeight()/2)-scale;
	}
	
	public void setTile(Tile tile, int x, int y) {
		tiles[x + y * width] = tile.getId();
	}
}
