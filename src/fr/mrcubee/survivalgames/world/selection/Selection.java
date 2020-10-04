package fr.mrcubee.survivalgames.world.selection;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * 
 * @author MrCubee
 *
 */
public class Selection {
	
	private Location locationMax;
	private Location locationMin;
	
	/**
	 * Protects the constructor of the Class from certain error cases.
	 * <pre>
	 *     Selection.createSelection(loc1, loc2)
	 * </pre>
	 * @param loc1 First location of the selection.
	 * @param loc2 Second location of the selection.
	 * @return Returns a selection instance based on both locations.
	 * @since 1.7
	 */
	public static Selection createSelection(final Location loc1, final Location loc2) {
		if (loc1 == null || loc2 == null || (!loc1.getWorld().equals(loc2.getWorld())))
			return null;
		return new Selection(loc1, loc2);
	}
	
	/**
	 * Constructor of the Selection Class.
	 * @param loc1 First location of the selection
	 * @param loc2 Second location of the selection.
	 * @since 1.7
	 */
	private Selection(final Location loc1, final Location loc2) {
		locationMax = new Location(loc1.getWorld(), 0, 0, 0);
		locationMin = new Location(loc1.getWorld(), 0, 0, 0);

		locationMax.setX((loc1.getX() >= loc2.getX()) ? loc1.getX() : loc2.getX());
		locationMax.setY((loc1.getY() >= loc2.getY()) ? loc1.getY() : loc2.getY());
		locationMax.setZ((loc1.getZ() >= loc2.getZ()) ? loc1.getZ() : loc2.getZ());
		locationMin.setX((loc1.getX() < loc2.getX()) ? loc1.getX() : loc2.getX());
		locationMin.setY((loc1.getY() < loc2.getY()) ? loc1.getY() : loc2.getY());
		locationMin.setZ((loc1.getZ() < loc2.getZ()) ? loc1.getZ() : loc2.getZ());
	}
	
	/**
	 * Return the highest location.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     Location locationMax = selection.getLocationMax();
	 * </pre>
	 * @return Return the highest location.
	 * @since 1.7
	 */
	public Location getLocationMax() {
		return locationMax;
	}
	
	/**
	 * Return the lowest location.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     Location locationMin = selection.getLocationMin();
	 * </pre>
	 * @return Return the lowest location.
	 * @since 1.7
	 */
	public Location getLocationMin() {
		return locationMin;
	}
	
	/**
	 * Returns the width of the selection.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     int width = selection.getWidth();
	 * </pre>
	 * @return Returns the width of the selection.
	 * @since 1.7
	 */
	public double getWidth() {
		return (locationMax.getX() - locationMin.getX() + 1);
	}
	
	/**
	 * Returns the length of the selection.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     int length = selection.getLength();
	 * </pre>
	 * @return Returns the length of the selection.
	 * @since 1.7
	 */
	public double getLength() {
		return (locationMax.getZ() - locationMin.getZ() + 1);
	}
	
	/**
	 * Returns the height of the selection.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     int height = selection.getHeight();
	 * </pre>
	 * @return Returns the length of the selection.
	 * @since 1.7
	 */
	public double getHeight() {
		return (locationMax.getY() - locationMin.getY() + 1);
	}
	
	/**
	 * Returns the size of the selection.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     int size = selection.getSize();
	 * </pre>
	 * @return Returns the size of the selection.
	 * @since 1.7
	 */
	public double getSize() {
		return (getWidth() * getLength() * getHeight());
	}
	
	/**
	 * Return the blocks in the selection.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     Block[] blocks = selection.getBlocks();
	 * </pre>
	 * @return Return the blocks in the selection.
	 * @since 1.7
	 */
	public Block[] getBlocks() {
		Block[] blocks = new Block[(int) getSize()];
		int     height = (int) getHeight();
		int     width = (int) getWidth();
		int     length = (int) getLength();
		
		for (int y = 0; y < height; y++) {
			for (int z = 0; z < length; z++) {
				for (int x = 0; x < width; x++) {
					blocks[x + y + z] = locationMin.add(x, y, z).getBlock();
				}
			}
		}
		return blocks;
	}
	
	/**
	 * Returns the number of chunks in the selection.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     int numberChunks = selection.getNumberChunks();
	 * </pre>
	 * @return Returns the number of chunks in the selection.
	 * @since 1.7
	 */
	public int getNumberChunks() {
		int chunksX = (int) (((locationMax.getX() - locationMin.getX()) / 16) + 1);
		int chunksZ = (int) (((locationMax.getZ() - locationMin.getZ()) / 16) + 1);

		return (chunksX * chunksZ);
	}
	
	/**
	 * Return the chunks in the selection.
	 * <pre>
	 *     Selection selection = Selection.createSelection(loc1, loc2);
	 *     <br>
	 *     Chunk[] chunks = selection.getChunks();
	 * </pre>
	 * @return Return the chunks in the selection.
	 * @since 1.7
	 */
	public Chunk[] getChunks() {
		Chunk[]  chunks;
		int      chunksX = (int) (((locationMax.getX() - locationMin.getX()) / 16) + 1);
		int      chunksZ = (int) (((locationMax.getZ() - locationMin.getZ()) / 16) + 1);
		int      nb_chunks =  chunksX * chunksZ;
		Location loc;
		
		chunks = new Chunk[nb_chunks];
		for (int z = 0; z < chunksZ; z++) {
			for (int x = 0; x < chunksX; x ++) {
				loc = locationMin.clone().add(x * 16, 0, z * 16);
				chunks[x + z * chunksX] = loc.getChunk();
			}
		}
		return chunks;
	}
}
