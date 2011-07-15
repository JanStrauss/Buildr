package me.simplex.buildr.runnable.builder;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Runnable_Builder_Cuboid implements Runnable {
	private Block position1,position2;
	private Material material;
	private boolean aironly;
	private boolean hollow;
	private Buildr plugin;
	private Player player;
	private byte material_data;
	
	public Buildr_Runnable_Builder_Cuboid(Block position1, Block position2,Material material, boolean aironly, boolean hollow,Buildr plugin, Player player, byte material_data) {
		this.position1 = position1;
		this.position2 = position2;
		this.material = material;
		this.aironly = aironly;
		this.plugin = plugin;
		this.player = player;
		this.hollow = hollow;
		this.material_data = material_data;
	}

	@Override
	public void run() {
		HashMap<Block, Buildr_Container_UndoBlock> undoBlocks= null;
		undoBlocks = buildCuboid();
		
		plugin.getUndoList().addToStack(undoBlocks, player);
		player.sendMessage("done! Placed "+undoBlocks.size()+" blocks");
		plugin.log(player.getName()+" builded a cuboid: "+undoBlocks.size()+" blocks affected");
	}
	
	private HashMap<Block, Buildr_Container_UndoBlock> buildCuboid(){
		HashMap<Block, Buildr_Container_UndoBlock> undo = new HashMap<Block, Buildr_Container_UndoBlock>();
		

		int start_y 	= calcStartPoint(position1.getY(), position2.getY());
		int end_y		= calcEndPoint(position1.getY(), position2.getY());
		
		int start_x 	= calcStartPoint(position1.getX(), position2.getX());
		int end_x 		= calcEndPoint(position1.getX(), position2.getX());
		
		int start_z 	= calcStartPoint(position1.getZ(), position2.getZ());
		int end_z 		= calcEndPoint(position1.getZ(), position2.getZ());
		
		for (int pos_x = start_x; pos_x <= end_x; pos_x++) {
			for (int pos_z = start_z; pos_z <= end_z; pos_z++) {
				for (int pos_y = start_y; pos_y <= end_y; pos_y++) {
					Block block_handle = player.getWorld().getBlockAt(pos_x, pos_y, pos_z);
					if (aironly && hollow) {
						if (block_handle.getType().equals(Material.AIR) && checkBlockIsOutside(block_handle)) {
							undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
							block_handle.setType(material);
							block_handle.setData(material_data);
						}
					}
					else if (!aironly && hollow) {
						if (checkBlockIsOutside(block_handle)) {
							undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
							block_handle.setType(material);
							block_handle.setData(material_data);
						}
					}
					else if (aironly && !hollow) {
						if (block_handle.getType().equals(Material.AIR)) {
							undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
							block_handle.setType(material);
							block_handle.setData(material_data);
						}
					}
					else {
						undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
						block_handle.setType(material);
						block_handle.setData(material_data);
					}
				}
			}
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
	
	private int calcEndPoint(int coordinate1, int coordinate2){
		if (coordinate1>coordinate2) {
			return coordinate1;
		}
		else {
			return coordinate2;
		}
	}
	
	private boolean checkBlockIsOutside(Block block){
		if (block.getX() == position1.getX() ||
			block.getX() == position2.getX() ||
			block.getY() == position1.getY() ||
			block.getY() == position2.getY() ||
			block.getZ() == position1.getZ() ||
			block.getZ() == position2.getZ()) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
