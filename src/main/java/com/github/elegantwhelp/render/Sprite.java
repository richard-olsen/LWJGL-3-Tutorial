package com.github.elegantwhelp.render;

public class Sprite {
	private VertexBatcher batcher;
	
	private int amountOfTilesX;
	private int amountOfTilesY;
	
	private float tileCellX;
	private float tileCellY;
	
	private Texture tileSheet;
	
	public Sprite(VertexBatcher batcher, String textureAtlasName, int amountOfTilesXY) { this(batcher, textureAtlasName, amountOfTilesXY, amountOfTilesXY); }
	public Sprite(VertexBatcher batcher, String textureAtlasName, int amountOfTilesX, int amountOfTilesY) {
		this.batcher = batcher;
		this.amountOfTilesX = amountOfTilesX;
		this.amountOfTilesY = amountOfTilesY;
		this.tileCellX = 1.0f / (float)amountOfTilesX;
		this.tileCellY = 1.0f / (float)amountOfTilesY;
		this.tileSheet = new Texture(textureAtlasName);
	}
	
	public void bindTexture() {
		tileSheet.bind(0);
	}
	
	public void drawSprite(float x, float y, float x2, float y2, int xTile, int yTile) {
		float u = xTile * tileCellX;
		float v = yTile * tileCellY;
		batcher.addVertex(x,	y,	u,				v);
		batcher.addVertex(x2,	y,	u+tileCellX,	v);
		batcher.addVertex(x2,	y2,	u+tileCellX,	v+tileCellY);

		batcher.addVertex(x2,	y2,	u+tileCellX,	v+tileCellY);
		batcher.addVertex(x,	y2,	u,				v+tileCellY);
		batcher.addVertex(x,	y,	u,				v);
	}
	
	public void drawSprite(float x, float y, float width, float height, int tile) {
		int xTile = tile % amountOfTilesX;
		int yTile = tile / amountOfTilesX;
		drawSprite(x,y,width,height,xTile,yTile);
	}
}
