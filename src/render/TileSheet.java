package render;

import org.joml.Matrix4f;

public class TileSheet {
	private Texture texture;
	
	private Matrix4f scale;
	private Matrix4f translation;
	
	private int amountOfTiles;
	
	public TileSheet(String texture, int amountOfTiles) {
		this.texture = new Texture("sheets/" + texture);
		
		scale = new Matrix4f().scale(1.0f / (float)amountOfTiles);
		translation = new Matrix4f();
		
		this.amountOfTiles = amountOfTiles;
	}
	
	public void bindTile(Shader shader, int x, int y) {
		scale.translate(x, y, 0, translation);
		
		shader.setUniform("sampler", 0);
		shader.setUniform("texModifier", translation);
		texture.bind(0);
	}
	
	public void bindTile(Shader shader, int tile) {
		int x = tile % amountOfTiles;
		int y = tile / amountOfTiles;
		bindTile(shader, x, y);
	}
}
