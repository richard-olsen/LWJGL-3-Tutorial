package com.github.elegantwhelp.world;

public class Tile {
	public static Tile tiles[] = new Tile[255];
	public static byte not = 0;
	
	public static final Tile grass = new Tile(0);
	public static final Tile bricks = new Tile(1).setSolid();
	
	private byte id;
	private boolean solid;
	private int tileIndex;
	
	public Tile(int tileIndex) {
		this.id = not;
		not++;
		this.solid = false;
		this.tileIndex = tileIndex;
		if (tiles[id] != null) throw new IllegalStateException("Tiles at [" + id + "] is already being used!");
		tiles[id] = this;
	}
	
	public Tile setSolid() {
		this.solid = true;
		return this;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public int getTileIndex() {
		return tileIndex;
	}
	
	public byte getId() {
		return id;
	}
}
