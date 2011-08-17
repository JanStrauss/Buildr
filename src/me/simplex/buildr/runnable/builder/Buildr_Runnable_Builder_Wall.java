package me.simplex.buildr.runnable.builder;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;
import me.simplex.buildr.util.Buildr_Type_Wall;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Runnable_Builder_Wall implements Runnable {
	private Block position1,position2;
	private Buildr_Type_Wall type;
	private Material material;
	private boolean replace;
	private Material replace_mat;
	private Buildr plugin;
	private Player player;
	private byte material_data;
	
	public Buildr_Runnable_Builder_Wall(Block position1, Block position2,Buildr_Type_Wall type, Material material, boolean replace, Material replace_mat,Buildr plugin, Player player, byte material_data) {
		this.position1 = position1;
		this.position2 = position2;
		this.type = type;
		this.material = material;
		this.replace = replace;
		this.replace_mat = replace_mat;
		this.plugin = plugin;
		this.player = player;
		this.material_data = material_data;
	}

	@Override
	public void run() {
		HashMap<Block, Buildr_Container_UndoBlock> undoBlocks= null;
		switch (type) {
		case WALL_X: undoBlocks = buildWallX(); break;
		case WALL_Y: undoBlocks = buildWallY(); break;
		case WALL_Z: undoBlocks = buildWallZ(); break;
		default:
			break;
		}
		plugin.getUndoList().addToStack(undoBlocks, player);
		player.sendMessage("done! Placed "+undoBlocks.size()+" blocks");
		plugin.log(player.getName()+" builded a wall: "+undoBlocks.size()+" blocks affected");
	}
	
	private HashMap<Block, Buildr_Container_UndoBlock> buildWallX(){
		HashMap<Block, Buildr_Container_UndoBlock> undo = new HashMap<Block, Buildr_Container_UndoBlock>();
		int fixed_X = position1.getX();
		int starty = calcStartPoint(position1.getY(), position2.getY());
		int startz = calcStartPoint(position1.getZ(), position2.getZ());
		
		int y = starty;
		int z = startz;
		
		int distY = calcDistance(position1.getY(), position2.getY());
		int distZ = calcDistance(position1.getZ(), position2.getZ());
		
		for (int i = 0; i < distY; i++) {			//y
			for (int j = 0; j < distZ; j++) {		//z
				Block block_handle = position1.getWorld().getBlockAt(fixed_X, y, z);
				
				if (replace) {
					if (block_handle.getType().equals(replace_mat)) {
						changeBlock(block_handle, undo);
					}
				}
				else {
					changeBlock(block_handle, undo);
				}
				z++;
			}
			y++;
			z=startz;
		}
		return undo;
	}
	
	private HashMap<Block, Buildr_Container_UndoBlock> buildWallY(){
		HashMap<Block, Buildr_Container_UndoBlock> undo = new HashMap<Block, Buildr_Container_UndoBlock>();
		int fixed_Y = position1.getY();
		int startx = calcStartPoint(position1.getX(), position2.getX());
		int startz = calcStartPoint(position1.getZ(), position2.getZ());
		
		int x = startx;
		int z = startz;
		
		int distX = calcDistance(position1.getX(), position2.getX());
		int distZ = calcDistance(position1.getZ(), position2.getZ());
		
		for (int i = 0; i < distX; i++) {			//x
			for (int j = 0; j < distZ; j++) {		//z
				Block block_handle = position1.getWorld().getBlockAt(x, fixed_Y, z);
				
				if (replace) {
					if (block_handle.getType().equals(replace_mat)) {
						changeBlock(block_handle, undo);
					}
				}
				else {
					changeBlock(block_handle, undo);
				}

				z++;
			}
			x++;
			z=startz;
		}
		return undo;
	}
	
	private HashMap<Block, Buildr_Container_UndoBlock> buildWallZ(){
		HashMap<Block, Buildr_Container_UndoBlock> undo = new HashMap<Block, Buildr_Container_UndoBlock>();
		int fixed_Z = position1.getZ();
		int startx = calcStartPoint(position1.getX(), position2.getX());
		int starty = calcStartPoint(position1.getY(), position2.getY());
		
		int x = startx;
		int y = starty;
		
		int distX = calcDistance(position1.getX(), position2.getX());
		int distY = calcDistance(position1.getY(), position2.getY());
		
		for (int i = 0; i < distX; i++) {			//x
			for (int j = 0; j < distY; j++) {		//y
				Block block_handle = position1.getWorld().getBlockAt(x, y, fixed_Z);
				
				if (replace) {
					if (block_handle.getType().equals(replace_mat)) {
						changeBlock(block_handle, undo);
					}
				}
				else {
					changeBlock(block_handle, undo);
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

		return distance+1;
	}
	
	private void changeBlock(Block block_handle, HashMap<Block, Buildr_Container_UndoBlock> undo){
		undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
		if (!plugin.checkPermission(player, "buildr.feature.break_bedrock")) {
			if (!block_handle.getType().equals(Material.BEDROCK)) {
				block_handle.setType(material);
				block_handle.setData(material_data);
			}
		}
		else {
			block_handle.setType(material);
			block_handle.setData(material_data);
		}
	}
}
