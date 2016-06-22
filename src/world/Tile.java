package world;

public class Tile {
	public static Tile tiles[] = new Tile[16];
	public static byte not = 0;
	
	public static final Tile test_tile = new Tile("bg");
	public static final Tile test2 = new Tile("checker");
	
	private byte id;
	private String texture;
	
	public Tile(String texture) {
		this.id = not;
		not++;
		this.texture = texture;
		if(tiles[id] != null)
			throw new IllegalStateException("Tiles at ["+id+"] is already being used!");
		tiles[id] = this;
	}

	public byte getId( ) {
		return id;
	}

	public String getTexture( ) {
		return texture;
	}
}
