package me.simplex.buildr.runnable;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_WallType;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Wallbuilder implements Runnable {
	private Block position1,position2;
	private Buildr_WallType type;
	private Material material;
	private boolean aironly;
	private Buildr plugin;
	private Player player;
	
	public Buildr_Wallbuilder(Block position1, Block position2,
			Buildr_WallType type, Material material, boolean aironly,
			Buildr plugin, Player player) {
		super();
		this.position1 = position1;
		this.position2 = position2;
		this.type = type;
		this.material = material;
		this.aironly = aironly;
		this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void run() {
		HashMap<Block, Material> undoItem= null;
		switch (type) {
		case WALL_X: undoItem = buildWallX(); break;
		case WALL_Y: undoItem = buildWallY(); break;
		case WALL_Z: undoItem = buildWallZ(); break;
		default:
			break;
		}
		plugin.getUndoList().addToStack(undoItem, player);
		player.sendMessage("done! Placed "+undoItem.size()+" blocks");
	}
	
	private HashMap<Block, Material> buildWallX(){
		HashMap<Block, Material> undo = new HashMap<Block, Material>();
		int fixed_X = position1.getX();
		int starty = calcStartPoint(position1.getY(), position2.getY());
		int startz = calcStartPoint(position1.getZ(), position2.getZ());
		
		int y = starty;
		int z = startz;
		
		int distY = calcDistance(position1.getY(), position2.getY());
		int distZ = calcDistance(position1.getZ(), position2.getZ());
		
		for (int i = 0; i < distY; i++) {			//y
			for (int j = 0; j < distZ; j++) {		//z
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
		
		int distX = calcDistance(position1.getX(), position2.getX());
		int distZ = calcDistance(position1.getZ(), position2.getZ());
		
		for (int i = 0; i < distX; i++) {			//x
			for (int j = 0; j < distZ; j++) {		//z
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
		
		int distX = calcDistance(position1.getX(), position2.getX());
		int distY = calcDistance(position1.getY(), position2.getY());
		
		for (int i = 0; i < distX; i++) {			//x
			for (int j = 0; j < distY; j++) {		//y
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
		return distance+1;
	}
}
