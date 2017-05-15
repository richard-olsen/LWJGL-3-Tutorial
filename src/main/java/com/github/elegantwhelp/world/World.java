package com.github.elegantwhelp.world;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.joml.*;

import com.github.elegantwhelp.collision.AABB;
import com.github.elegantwhelp.entity.*;
import com.github.elegantwhelp.io.Window;
import com.github.elegantwhelp.render.Camera;
import com.github.elegantwhelp.render.Shader;

public class World {
	private int viewX;
	private int viewY;
	private byte[] tiles;
	private AABB[] bounding_boxes;
	private List<Entity> entities;
	private int width;
	private int height;
	private int scale;
	
	private Matrix4f world;
	
	public World(String world, Camera camera) {
		try {
			BufferedImage tile_sheet = ImageIO.read(new File("./levels/" + world + "/tiles.png"));
			BufferedImage entity_sheet = ImageIO.read(new File("./levels/" + world + "/entities.png"));
			
			width = tile_sheet.getWidth();
			height = tile_sheet.getHeight();
			scale = 32;
			
			this.world = new Matrix4f().setTranslation(new Vector3f(0));
			this.world.scale(scale);
			
			int[] colorTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);
			int[] colorEntitySheet = entity_sheet.getRGB(0, 0, width, height, null, 0, width);
			
			tiles = new byte[width * height];
			bounding_boxes = new AABB[width * height];
			entities = new ArrayList<>();
			
			Transform transform;
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;
					int entity_index = (colorEntitySheet[x + y * width] >> 16) & 0xFF;
					int entity_alpha = (colorEntitySheet[x + y * width] >> 24) & 0xFF;
					
					Tile t;
					try {
						t = Tile.tiles[red];
					}
					catch (ArrayIndexOutOfBoundsException e) {
						t = null;
					}
					
					if (t != null) setTile(t, x, y);
					
					if (entity_alpha > 0) {
						transform = new Transform();
						transform.pos.x = x * 2;
						transform.pos.y = -y * 2;
						switch (entity_index) {
							case 1 :							// Player
								Player player = new Player(transform);
								entities.add(player);
								camera.getPosition().set(transform.pos.mul(-scale, new Vector3f()));
								break;
							default :
								break;
						}
					}
				}
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public World() {
		width = 64;
		height = 64;
		scale = 16;
		
		tiles = new byte[width * height];
		bounding_boxes = new AABB[width * height];
		
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(scale);
	}
	
	public void calculateView(Window window) {
		viewX = (window.getWidth() / (scale * 2)) + 4;
		viewY = (window.getHeight() / (scale * 2)) + 4;
	}
	
	public Matrix4f getWorldMatrix() {
		return world;
	}
	
	public void render(TileRenderer render, Shader shader, Camera cam) {
		int posX = (int) cam.getPosition().x / (scale * 2);
		int posY = (int) cam.getPosition().y / (scale * 2);
		
		for (int i = 0; i < viewX; i++) {
			for (int j = 0; j < viewY; j++) {
				Tile t = getTile(i - posX - (viewX / 2) + 1, j + posY - (viewY / 2));
				if (t != null) render.renderTile(t, i - posX - (viewX / 2) + 1, -j - posY + (viewY / 2), shader, world, cam);
			}
		}
		
		for (Entity entity : entities) {
			entity.render(shader, cam, this);
		}
	}
	
	public void update(float delta, Window window, Camera camera) {
		for (Entity entity : entities) {
			entity.update(delta, window, camera, this);
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).collideWithTiles(this);
			for (int j = i + 1; j < entities.size(); j++) {
				entities.get(i).collideWithEntity(entities.get(j));
			}
			entities.get(i).collideWithTiles(this);
		}
	}
	
	public void correctCamera(Camera camera, Window window) {
		Vector3f pos = camera.getPosition();
		
		int w = -width * scale * 2;
		int h = height * scale * 2;
		
		if (pos.x > -(window.getWidth() / 2) + scale) pos.x = -(window.getWidth() / 2) + scale;
		if (pos.x < w + (window.getWidth() / 2) + scale) pos.x = w + (window.getWidth() / 2) + scale;
		
		if (pos.y < (window.getHeight() / 2) - scale) pos.y = (window.getHeight() / 2) - scale;
		if (pos.y > h - (window.getHeight() / 2) - scale) pos.y = h - (window.getHeight() / 2) - scale;
	}
	
	public void setTile(Tile tile, int x, int y) {
		tiles[x + y * width] = tile.getId();
		if (tile.isSolid()) {
			bounding_boxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
		}
		else {
			bounding_boxes[x + y * width] = null;
		}
	}
	
	public Tile getTile(int x, int y) {
		try {
			return Tile.tiles[tiles[x + y * width]];
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public AABB getTileBoundingBox(int x, int y) {
		try {
			return bounding_boxes[x + y * width];
		}
		catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public int getScale() {
		return scale;
	}
}
