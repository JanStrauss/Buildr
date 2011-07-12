package me.simplex.buildr.runnable;

import java.util.ArrayList;
import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;

import org.bukkit.Location;
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
		
		int pos1_x		= position1.getX();
		int pos2_x		= position2.getX();
		
		int pos1_z		= position1.getZ();
		int pos2_z		= position2.getZ();
		
		int side_a 		= calcDistance(pos1_x, pos2_x);
		int side_b 		= calcDistance(pos1_z, pos2_z);
		
		double lenght 	= Math.sqrt((side_a*side_a)+(side_b*side_b));
		double mod 		= 1/lenght;
		double pos_x	= 0;
		double pos_z;
		double i		= 0;
		
		
		while(i<=1){
			
			pos_x = pos1_x + i*(pos2_x-pos1_x);
			pos_z = pos1_z + i*(pos2_z-pos1_z);
			
			Location loc = new Location(player.getWorld(), pos_x, level, pos_z);
			
			Block block_handle = player.getWorld().getBlockAt(loc);
			
			if (!groundblocks.contains(block_handle)) {
				groundblocks.add(block_handle);
				for (int j = 0; j < height; j++) {
					if (aironly) {
						if (block_handle.getType().equals(Material.AIR)) {
							toBuild.add(block_handle.getRelative(0, j, 0));
						}
					}
					else {
						toBuild.add(block_handle.getRelative(0, j, 0));
					}
					
				}
			}
			i=i+(mod);
		}
		
		for (Block block : toBuild) {
			undo.put(block, new Buildr_Container_UndoBlock(block.getType(), block.getData()));
			block.setType(material);
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
