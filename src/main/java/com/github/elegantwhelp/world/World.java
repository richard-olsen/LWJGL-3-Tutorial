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
import com.github.elegantwhelp.render.Animation;
import com.github.elegantwhelp.render.Camera;
import com.github.elegantwhelp.render.Shader;
import com.github.elegantwhelp.render.Sprite;

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
			scale = 64;
			
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
						transform.pos.x = x;
						transform.pos.y = y;
						transform.scale.x = 1;
						transform.scale.y = 1; // Scale is based off the size of tiles. 2 would be the size of 2 tiles, 1/2 would be the size of half a tile etc.
						// Recommended to use squares as rectangles cause a bug in the sorting code.
						switch (entity_index) {
							case 1 :							// Player
								Player player = new Player(transform);
								entities.add(player);
								transform.pos.add(player.getBoundingBox().getHalfExtent(), camera.getPosition()).mul(scale);
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
		scale = 64;
		
		tiles = new byte[width * height];
		bounding_boxes = new AABB[width * height];
		
		world = new Matrix4f().setTranslation(new Vector3f(0));
		world.scale(scale);
	}
	
	public void calculateView(Window window) {
//		viewX = (window.getWidth() / (scale * 2)) + 4;
//		viewY = (window.getHeight() / (scale * 2)) + 4;
	}
	
	public Matrix4f getWorldMatrix() {
		return world;
	}
	
	public void renderTiles(Sprite tileSprites, Camera camera) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				float x = i * scale;
				float y = j * scale;
				tileSprites.drawSprite(x, y, x + scale, y + scale, getTile(i, j).getTileIndex());
			}
		}
	}
	
	public void renderEntities(Sprite tileEntity, Camera camera) {
		for (Entity entity : entities) {
			Transform transform = entity.getTransform();
			float x = transform.pos.x * scale;
			float y = transform.pos.y * scale;
			float entityScaleX = transform.scale.x * scale;
			float entityScaleY = transform.scale.y * scale;
			
			Animation animation = entity.getCurrentAnimation();
			tileEntity.drawSprite(x, y, x + entityScaleX, y + entityScaleY, animation.getIndex());
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
		float halfWinWidth = window.getWidth() * 0.5f;
		float halfWinHeight = window.getHeight() * 0.5f;
		
		float minX = halfWinWidth;
		float minY = halfWinHeight;
		float maxX = width * scale - halfWinWidth;
		float maxY = height * scale - halfWinHeight;
		
		Vector2f pos = camera.getPosition();
		
		if (pos.x > maxX) pos.x = maxX;
		if (pos.y > maxY) pos.y = maxY;
		if (pos.x < minX) pos.x = minX;
		if (pos.y < minY) pos.y = minY;
	}
	
	public void setTile(Tile tile, int x, int y) {
		tiles[x + y * width] = tile.getId();
		if (tile.isSolid()) {
			//bounding_boxes[x + y * width] = new AABB(new Vector2f(x, y), new Vector2f(1, 1));
			Vector2f posCoords = new Vector2f(x, y);
			Vector2f sizeCoords = new Vector2f(1, 1).add(posCoords);
			bounding_boxes[x + y * width] = AABB.createAABB(posCoords, sizeCoords);
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
