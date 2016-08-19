package world;

public class Tile {
	public static Tile tiles[] = new Tile[255];
	public static byte not = 0;
	
	public static final Tile test_tile = new Tile("bg");
	public static final Tile test2 = new Tile("checker").setSolid();
	
	private byte id;
	private boolean solid;
	private String texture;
	
	public Tile(String texture) {
		this.id = not;
		not++;
		this.texture = texture;
		this.solid = false;
		if(tiles[id] != null)
			throw new IllegalStateException("Tiles at ["+id+"] is already being used!");
		tiles[id] = this;
	}

	public Tile setSolid() { this.solid = true; return this; }
	public boolean isSolid() { return solid; }
	
	public byte getId( ) {
		return id;
	}

	public String getTexture( ) {
		return texture;
	}
}
