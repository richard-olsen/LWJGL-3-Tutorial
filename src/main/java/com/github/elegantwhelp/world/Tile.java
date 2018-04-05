package com.github.elegantwhelp.world;

public class Tile {
	public static Tile tiles[] = new Tile[255];
	public static byte not = 0;
	
	public static final Tile grass = new Tile();
	public static final Tile bricks = new Tile().setSolid();
	
	private byte id;
	private boolean solid;
	
	public Tile() {
		this.id = not;
		not++;
		this.solid = false;
		if (tiles[id] != null) throw new IllegalStateException("Tiles at [" + id + "] is already being used!");
		tiles[id] = this;
		System.out.println("Registered Tile ["+id+"]");
	}
	
	public Tile setSolid() {
		this.solid = true;
		return this;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public byte getId() {
		return id;
	}
}
