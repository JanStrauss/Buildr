package me.simplex.buildr.runnable.builder;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Runnable_Builder_Sphere implements Runnable {
	private Block centerblock,radius_marker;
	private Material material;
	private boolean aironly;
	private boolean hollow;
	private boolean halfcube;
	private Buildr plugin;
	private Player player;

	public Buildr_Runnable_Builder_Sphere(Block position1, Block position2,Material material, boolean aironly, boolean hollow,boolean halfcube,Buildr plugin, Player player) {
		this.centerblock = position1;
		this.radius_marker = position2;
		this.material = material;
		this.aironly = aironly;
		this.plugin = plugin;
		this.player = player;
		this.hollow = hollow;
		this.halfcube =halfcube;
	}

	@Override
	public void run() {
		HashMap<Block, Buildr_Container_UndoBlock> undoBlocks= null;
		undoBlocks = buildSphere();
		
		plugin.getUndoList().addToStack(undoBlocks, player);
		player.sendMessage("done! Placed "+undoBlocks.size()+" blocks");
		plugin.log(player.getName()+" builded a cuboid: "+undoBlocks.size()+" blocks affected");
	}
	
	private HashMap<Block, Buildr_Container_UndoBlock> buildSphere(){
		HashMap<Block, Buildr_Container_UndoBlock> undo = new HashMap<Block, Buildr_Container_UndoBlock>();
		
		double radius 	= calcVecDistance(centerblock.getLocation(), radius_marker.getLocation());
		Location start 	= calcStartLocation(radius, centerblock);
		Location end 	= calcEndLocation(radius, centerblock);
		
		for (int pos_x = start.getBlockX(); pos_x <= end.getBlockX(); pos_x++) {
			for (int pos_y = start.getBlockY(); pos_y <= end.getBlockY(); pos_y++) {
				for (int pos_z = start.getBlockZ(); pos_z <= end.getBlockZ(); pos_z++) {
					
					Block block_handle = player.getWorld().getBlockAt(pos_x, pos_y, pos_z);
					
					if (isBlockInRadius(radius, block_handle)) {
						if (aironly && hollow) {
							if (block_handle.getType().equals(Material.AIR) && checkBlockIsOutside(radius,block_handle)) {
								undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
								block_handle.setType(material);
							}
						}
						else if (!aironly && hollow) {
							if (checkBlockIsOutside(radius,block_handle)) {
								undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
								block_handle.setType(material);
							}
						}
						else if (aironly && !hollow) {
							if (block_handle.getType().equals(Material.AIR)) {
								undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
								block_handle.setType(material);
							}
						}
						else {
							undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
							block_handle.setType(material);
						}
					}

				}
			}
		}
		return undo;
	}
	
	private Location calcStartLocation(double radius, Block center){
		int radius_round = (int)Math.round(radius);
		if (!halfcube) {
			return center.getRelative(-radius_round, -radius_round, -radius_round).getLocation();
		}
		else {
			return center.getRelative(-radius_round, 0, -radius_round).getLocation();
		}
	}
	
	private Location calcEndLocation(double radius, Block center){
		int radius_round = (int)Math.round(radius);
		return center.getRelative(+radius_round, +radius_round, +radius_round).getLocation();
	}
	
	private double calcVecDistance(Location pos1, Location pos2){
		int side_a 		= calcDistance(pos1.getBlockX(), pos2.getBlockX());
		int side_b 		= calcDistance(pos1.getBlockZ(), pos2.getBlockZ());
		int side_c		= calcDistance(pos1.getBlockY(), pos2.getBlockY());
		return Math.sqrt((side_a*side_a)+(side_b*side_b)+(side_c*side_c));
	}
	
	private int calcDistance(int coordinate1, int coordinate2){
		int distance = Math.abs(coordinate1-coordinate2);
		return distance;
	}
	
	private boolean isBlockInRadius(double radius, Block handle){
		if (calcVecDistance(centerblock.getLocation(), handle.getLocation())<= radius) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean checkBlockIsOutside(double radius, Block handle){
		if (calcVecDistance(centerblock.getLocation(), handle.getLocation()) >= radius-1) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
