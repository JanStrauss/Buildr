package me.simplex.buildr;

import java.util.HashMap;

import me.simplex.buildr.util.Buildr_WallType;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Wallbuilder {
	private Player wallcreater;
	private Block position1,position2;
	private Buildr_WallType type;
	private Material material;
	private boolean aironly;
	private boolean coordinate1placed = false;
	
	public Buildr_Wallbuilder(Player player, Material material, boolean aironly) {
		this.wallcreater=player;
		this.material = material;
		this.aironly = aironly;
	}
	
	public void addCoordinate1(Block position1){
		this.position1 = position1;
		coordinate1placed = true;
	}
	
	public boolean checkCoordinates(Block position2){
		this.position2=position2;
		
		if (checkWallType()==null) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public HashMap<Block, Material> startBuild(){
		HashMap<Block, Material> undoItem=null;
		switch (type) {
		case WALL_X: undoItem = buildWallX(); break;
		case WALL_Y: undoItem = buildWallY(); break;
		case WALL_Z: undoItem = buildWallZ(); break;
		default:
			break;
		}
		return undoItem;
	}
	
	private HashMap<Block, Material> buildWallX(){
		HashMap<Block, Material> undo = new HashMap<Block, Material>();
		int fixed_X = position1.getX();
		int starty = calcStartPoint(position1.getY(), position2.getY());
		int startz = calcStartPoint(position1.getZ(), position2.getZ());
		
		int y = starty;
		int z = startz;
		
		for (int i = 0; i < calcDistance(position1.getX(), position2.getX()); i++) {			//Y
			for (int j = 0; j < calcDistance(position1.getZ(), position2.getZ()); j++) {		//z
				
				Block actionBlock = position1.getWorld().getBlockAt(fixed_X, y, z);
				
				if (aironly) {
					if (actionBlock.getType().equals(Material.AIR)) {
						undo.put(actionBlock, actionBlock.getType());
						actionBlock.setType(material);
					}
				}
				else {
					undo.put(actionBlock, actionBlock.getType());
					actionBlock.setType(material);
				}

				z++;
			}
			y++;
			z=startz;
		}
		return undo;
	}
	
	private HashMap<Block, Material> buildWallY(){
		HashMap<Block, Material> undo = new HashMap<Block, Material>();
		int fixed_Y = position1.getY();
		int startx = calcStartPoint(position1.getX(), position2.getX());
		int startz = calcStartPoint(position1.getZ(), position2.getZ());
		
		int x = startx;
		int z = startz;
		
		for (int i = 0; i < calcDistance(position1.getX(), position2.getX()); i++) {			//x
			for (int j = 0; j < calcDistance(position1.getZ(), position2.getZ()); j++) {		//z
				
				Block actionBlock = position1.getWorld().getBlockAt(x, fixed_Y, z);
				
				if (aironly) {
					if (actionBlock.getType().equals(Material.AIR)) {
						undo.put(actionBlock, actionBlock.getType());
						actionBlock.setType(material);
					}
				}
				else {
					undo.put(actionBlock, actionBlock.getType());
					actionBlock.setType(material);
				}

				z++;
			}
			x++;
			z=startz;
		}
		return undo;
	}
	
	private HashMap<Block, Material> buildWallZ(){
		HashMap<Block, Material> undo = new HashMap<Block, Material>();
		int fixed_Z = position1.getZ();
		int startx = calcStartPoint(position1.getX(), position2.getX());
		int starty = calcStartPoint(position1.getY(), position2.getY());
		
		int x = startx;
		int y = starty;
		
		for (int i = 0; i < calcDistance(position1.getX(), position2.getX()); i++) {			//x
			for (int j = 0; j < calcDistance(position1.getZ(), position2.getZ()); j++) {		//y
				
				Block actionBlock = position1.getWorld().getBlockAt(x, y, fixed_Z);
				
				if (aironly) {
					if (actionBlock.getType().equals(Material.AIR)) {
						undo.put(actionBlock, actionBlock.getType());
						actionBlock.setType(material);
					}
				}
				else {
					undo.put(actionBlock, actionBlock.getType());
					actionBlock.setType(material);
				}

				y++;
			}
			x++;
			y=starty;
		}
		return undo;
	}
	
	private int calcStartPoint(int coordinate1, int coordinate2){
		if (coordinate1<=coordinate2) {
			return coordinate1;
		}
		else {
			return coordinate2;
		}
	}
	
	private int calcDistance(int coordinate1, int coordinate2){
		int distance = Math.abs(coordinate1-coordinate2);
		if (distance >100) {
			distance = 100;
		}
		return distance;
	}
	
	private Buildr_WallType checkWallType(){
		if (position1.getX() == position2.getX()) {
			return Buildr_WallType.WALL_X;
		}
		if (position1.getY() == position2.getY()) {
			return Buildr_WallType.WALL_Y;
		}
		if (position1.getZ() == position2.getZ()) {
			return Buildr_WallType.WALL_Z;
		}
		else {
			return null;
		}
	}

	/**
	 * @return the wallcreater
	 */
	public Player getWallcreater() {
		return wallcreater;
	}
	/**
	 * 
	 * @return is coordinate1placed
	 */
	public boolean isCoordinate1placed(){
		return coordinate1placed;
	}
}
