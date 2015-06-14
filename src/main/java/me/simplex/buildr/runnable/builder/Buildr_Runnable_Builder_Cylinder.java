/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Buildr.
 * 
 * Buildr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Buildr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Buildr  If not, see <http://www.gnu.org/licenses/>.
 */
package me.simplex.buildr.runnable.builder;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Runnable_Builder_Cylinder extends Buildr_Runnable_Builder_Super{
	private Block centerblock;
	private Block radius_marker;
	private boolean replace;

	public Buildr_Runnable_Builder_Cylinder(Block position1, Block position2,Material material, boolean replace, Material replace_mat, boolean hollow, Buildr plugin, Player player, byte material_data) {
		this.centerblock = position1;
		this.radius_marker = position2;
		this.material = material;
		this.replace = replace;
		this.plugin = plugin;
		this.player = player;
		this.hollow = hollow;
		this.material_data = material_data;
		this.replace_mat = replace_mat;
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
		int height 		= calcDistance(centerblock.getY(), radius_marker.getY());
		int level 		= calcStartPoint(centerblock.getY(), radius_marker.getY());
		Location start 	= calcStartLocation(radius, centerblock, level);
		Location end 	= calcEndLocation(radius, centerblock, level, height);
		
		for (int pos_x = start.getBlockX(); pos_x <= end.getBlockX(); pos_x++) {
			for (int pos_y = start.getBlockY(); pos_y <= end.getBlockY(); pos_y++) {
				for (int pos_z = start.getBlockZ(); pos_z <= end.getBlockZ(); pos_z++) {
					
					Block block_handle = player.getWorld().getBlockAt(pos_x, pos_y, pos_z);
					
					if (isBlockInRadius(radius, block_handle)) {
						if (replace && hollow) {
							if (block_handle.getType().equals(replace_mat) && checkBlockIsOutside(radius,block_handle)) {
								changeBlock(block_handle, undo);
							}
						}
						else if (!replace && hollow) {
							if (checkBlockIsOutside(radius,block_handle)) {
								changeBlock(block_handle, undo);
							}
						}
						else if (replace && !hollow) {
							if (block_handle.getType().equals(replace_mat)) {
								changeBlock(block_handle, undo);
							}
						}
						else {
							changeBlock(block_handle, undo);
						}
					}

				}
			}
		}
		return undo;
	}
	
	private Location calcStartLocation(double radius, Block center, int level){
		int radius_round = (int)Math.round(radius);
		Location loc = center.getRelative(-radius_round, 0, -radius_round).getLocation();
		loc.setY(level);
		return loc;
	}
	
	private Location calcEndLocation(double radius, Block center,int level,int height){
		int radius_round = (int)Math.round(radius);
		Location loc = center.getRelative(+radius_round, +radius_round, +radius_round).getLocation();
		loc.setY(level+height);
		return loc;
	}
	
	private double calcVecDistance(Location pos1, Location pos2){
		int side_a 		= calcDistance(pos1.getBlockX(), pos2.getBlockX());
		int side_b 		= calcDistance(pos1.getBlockZ(), pos2.getBlockZ());
		return Math.sqrt((side_a*side_a)+(side_b*side_b));
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
