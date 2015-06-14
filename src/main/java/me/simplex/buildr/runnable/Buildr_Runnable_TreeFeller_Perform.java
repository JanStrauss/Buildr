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
package me.simplex.buildr.runnable;

import java.util.ArrayList;
import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Runnable_TreeFeller_Perform implements Runnable {
	
	private ArrayList<Block> logs;
	private ArrayList<Block> leaves;
	private HashMap<Block, Buildr_Container_UndoBlock> undo;
	
	private Buildr plugin;
	private Player player;

	public Buildr_Runnable_TreeFeller_Perform(ArrayList<Block> logs,ArrayList<Block> leaves, Buildr plugin, Player player) {
		super();
		this.logs = logs;
		this.leaves = leaves;
		this.plugin = plugin;
		this.player = player;
		this.undo = new HashMap<Block, Buildr_Container_UndoBlock>();
	}

	public void run() {
		for (Block blk : logs) {
			undo.put(blk, new Buildr_Container_UndoBlock(blk.getType(), blk.getData()));
			blk.setType(Material.AIR);
		}
		if (plugin.getConfigValue("TREECUTTER_CUT_LEAVES")) {
			if (checkSize()) {	
				for (Block blk : leaves) {
					undo.put(blk,  new Buildr_Container_UndoBlock(blk.getType(), blk.getData()));
					blk.setType(Material.AIR);
				}
			}
			else {
				player.sendMessage(ChatColor.YELLOW+"WARNING: Too many blocks, will only remove log");
			}
		}
			
		player.sendMessage("Felt Tree. Blocks changed: "+undo.size());
		plugin.getUndoList().addToStack(undo, player);
		plugin.getPlayerCuttingTree().remove(player);
		plugin.log(player.getName()+" felt a tree: "+undo.size()+" blocks affected");
	}
	
	private boolean checkSize(){
		if (logs.size()+leaves.size()>2000) {
			return false;
		}
		return true;
	}
	
}
