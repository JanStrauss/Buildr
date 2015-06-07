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
import me.simplex.buildr.util.Buildr_Runnable_Builder_Super;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Runnable_Builder_Cuboid extends Buildr_Runnable_Builder_Super{
	
	public Buildr_Runnable_Builder_Cuboid(Block position1, Block position2,Material material, boolean replace, Material replace_mat, boolean hollow,Buildr plugin, Player player, byte material_data) {
		this.position1 = position1;
		this.position2 = position2;
		this.material = material;
		this.aironly = replace;
		this.plugin = plugin;
		this.player = player;
		this.hollow = hollow;
		this.material_data = material_data;
		this.replace_mat = replace_mat;
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
						if (block_handle.getType().equals(replace_mat) && checkBlockIsOutside(block_handle)) {
							changeBlock(block_handle, undo);
						}
					}
					else if (!aironly && hollow) {
						if (checkBlockIsOutside(block_handle)) {
							changeBlock(block_handle, undo);
						}
					}
					else if (aironly && !hollow) {
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
		return undo;
	}
		
}
