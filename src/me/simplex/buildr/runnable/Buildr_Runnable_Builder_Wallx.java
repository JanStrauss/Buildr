package me.simplex.buildr.runnable;

import java.util.ArrayList;
import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Runnable_Builder_Wallx implements Runnable {
	private Block position1,position2;
	private Material material;
	private boolean aironly;
	private Buildr plugin;
	private Player player;
	
	public Buildr_Runnable_Builder_Wallx(Block position1, Block position2,Material material, boolean aironly,Buildr plugin, Player player) {
		this.position1 = position1;
		this.position2 = position2;
		this.material = material;
		this.aironly = aironly;
		this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void run() {
		HashMap<Block, Buildr_Container_UndoBlock> undoBlocks= null;
		undoBlocks = buildWall();
		
		plugin.getUndoList().addToStack(undoBlocks, player);
		player.sendMessage("done! Placed "+undoBlocks.size()+" blocks");
		plugin.log(player.getName()+" builded a wall: "+undoBlocks.size()+" blocks affected");
	}
	
	private HashMap<Block, Buildr_Container_UndoBlock> buildWall(){
		ArrayList<Block> groundblocks = new ArrayList<Block>();
		ArrayList<Block> toBuild = new ArrayList<Block>();
		HashMap<Block, Buildr_Container_UndoBlock> undo = new HashMap<Block, Buildr_Container_UndoBlock>();
		 
		int height 		= calcDistance(position1.getY(), position2.getY());
		int level  		= calcStartPoint(position1.getY(), position2.getY());
		
		int side_a 		= calcDistance(position1.getX(), position2.getX());
		int side_b 		= calcDistance(position1.getZ(), position2.getZ());
		
		int[] data_x 	= calcStartAndEndPoint(position1.getX(), position2.getX());
		int start_x 	= data_x[0];
		int end_x    	= data_x[1];
			
		int[] data_z 	= calcStartAndEndPoint(position1.getZ(), position2.getZ());
		int start_z 	= data_z[0];
		int end_z    	= data_z[1];
		
		int lenght 		= (int)(Math.sqrt((side_a*side_a)+(side_b*side_b)));
		
		for (double i = 0; i < lenght; i=i+0.25) {
			int pos_x = (int)(start_x + i*(end_x-start_x));
			int pos_z = (int)(start_z + i*(end_z-start_z));
			
			Block block_handle = player.getWorld().getBlockAt(pos_x, level,pos_z);
			if (!groundblocks.contains(block_handle)) {
				groundblocks.add(block_handle);
				for (int j = level; j < level+height; j++) {
					if (aironly) {
						if (block_handle.getType() == Material.AIR) {
							toBuild.add(player.getWorld().getBlockAt(pos_x, j,pos_z));
						}
					}
					else {
						toBuild.add(player.getWorld().getBlockAt(pos_x, j,pos_z));
					}
					
				}
			}
		}
		for (Block block : toBuild) {
			undo.put(block, new Buildr_Container_UndoBlock(block.getType(), block.getData()));
			block.setType(material);
		}
		return undo;
	}

	
	private int[] calcStartAndEndPoint(int coordinate1, int coordinate2){
		int[] ret = new int[2];
		if (coordinate1<=coordinate2) {
			ret[0]= coordinate1;
			ret[1]= coordinate2;
		}
		else {
			ret[0]= coordinate2;
			ret[1]= coordinate1;
		}
		return ret;
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
