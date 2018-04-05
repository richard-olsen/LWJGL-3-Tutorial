package com.github.elegantwhelp.render;

public class Sprite {
	private VertexBatcher batcher;
	
	private int amountOfTilesX;
	private int amountOfTilesY;
	
	private float tileCellX;
	private float tileCellY;
	
	// Needed to offset tiles with borders. Each tile as a 1 pixel border surrounding it, making two pixels between to tiles on the atlas.
	private float pixelSizeX;
	private float pixelSizeY;
	
	private Texture tileSheet;
	
	public Sprite(VertexBatcher batcher, String textureAtlasName, int amountOfTilesXY) { this(batcher, textureAtlasName, amountOfTilesXY, amountOfTilesXY); }
	public Sprite(VertexBatcher batcher, String textureAtlasName, int amountOfTilesX, int amountOfTilesY) {
		this.batcher = batcher;
		this.amountOfTilesX = amountOfTilesX;
		this.amountOfTilesY = amountOfTilesY;
		this.tileCellX = 1.0f / (float)amountOfTilesX;
		this.tileCellY = 1.0f / (float)amountOfTilesY;
		this.tileSheet = new Texture(textureAtlasName);
		this.pixelSizeX = 1.0f / (float)tileSheet.getWidth();
		this.pixelSizeY = 1.0f / (float)tileSheet.getHeight();
	}
	
	public void bindTexture() {
		tileSheet.bind(0);
	}
	
	public void drawSprite(float x, float y, float x2, float y2, int xTile, int yTile) {
//		float u = xTile * tileCellX;
//		float v = yTile * tileCellY;
//		batcher.addVertex(x,	y,	u,				v);
//		batcher.addVertex(x2,	y,	u+tileCellX,	v);
//		batcher.addVertex(x2,	y2,	u+tileCellX,	v+tileCellY);
//
//		batcher.addVertex(x2,	y2,	u+tileCellX,	v+tileCellY);
//		batcher.addVertex(x,	y2,	u,				v+tileCellY);
//		batcher.addVertex(x,	y,	u,				v);
		
		// The borders are used to eliminate texture bleeding. What happens is OpenGL sometimes grabs pixels outside the range we define while filtering the texture.
		// This causes parts of the neighboring tile on the atlas to display on the sides of any tile. 
		// Note: This happens WAY more often when interpolating the camera's position. Without interpolating the camera's position, this actually doesn't happen as often. Still happens though.
		
		// We use the borders to make the texture a little more seamless, so when it does grab pixels outside the range, it doesn't take any from the neighboring
		// texture.
		
		// All that's left to do is apart of the texture file itself. Replace the border color with sides of the texture to make the texture more seamless in game.
		
		float u = xTile * tileCellX + pixelSizeX; // Always an offset for the border
		float v = yTile * tileCellY + pixelSizeY;
		float tileCellXO = tileCellX - pixelSizeX*2; // Fix tile size to exclude the border
		float tileCellYO = tileCellY - pixelSizeY*2;
		batcher.addVertex(x,	y,	u,				v);
		batcher.addVertex(x2,	y,	u+tileCellXO,	v);
		batcher.addVertex(x2,	y2,	u+tileCellXO,	v+tileCellYO);

		batcher.addVertex(x2,	y2,	u+tileCellXO,	v+tileCellYO);
		batcher.addVertex(x,	y2,	u,				v+tileCellYO);
		batcher.addVertex(x,	y,	u,				v);
	}
	
	public void drawSprite(float x, float y, float x2, float y2, int tile) {
		int xTile = tile % amountOfTilesX;
		int yTile = tile / amountOfTilesX;
		drawSprite(x,y,x2,y2,xTile,yTile);
	}
}
