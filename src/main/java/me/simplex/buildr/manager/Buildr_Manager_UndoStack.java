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
package me.simplex.buildr.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.simplex.buildr.util.Buildr_Container_UndoBlock;
import me.simplex.buildr.util.Buildr_Stack_Undo;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_UndoStack {
	private List<Buildr_StackItem> playerUndos;

	
	public Buildr_Manager_UndoStack() {
		playerUndos = new ArrayList<Buildr_StackItem>();
	}

	public void addToStack(Map<Block, Buildr_Container_UndoBlock> blocks, Player player){
		if (checkPlayerUndoStackExist(player)) {
			giveStack(player).push(blocks);
		}
		else {
			newStack(player,blocks);
		}
	}
	
	private void newStack(Player player, Map<Block, Buildr_Container_UndoBlock> blocks) {
		playerUndos.add(new Buildr_StackItem(player, blocks));
	}

	public Map<Block, Buildr_Container_UndoBlock> getAndDeleteFromStack(Player player){
		if (checkPlayerUndoStackExist(player)) {
			if (!giveStack(player).isEmpty()) {
				return giveStack(player).poll();
			}
		}
		return null;
	}
	
	private boolean checkPlayerUndoStackExist(Player player){
		for (Buildr_StackItem undos : playerUndos) {
			if (undos.getPlayer() == player) {
				return true;
			}
		}
		return false;
	}
	
	private Buildr_Stack_Undo giveStack(Player player){
		for (Buildr_StackItem undos : playerUndos) {
			if (undos.getPlayer() == player) {
				return undos.getOrig_blocks();
			}
		}
		return null;
	}
	
	private class Buildr_StackItem{
		Buildr_Stack_Undo orig_blocks;
		Player player;
		
		public Buildr_StackItem(Player player, Map<Block, Buildr_Container_UndoBlock> blocks) {
			this.orig_blocks = new Buildr_Stack_Undo(20);
			this.orig_blocks.pushWithLimit(blocks);
			this.player = player;
		}

		public Buildr_Stack_Undo getOrig_blocks() {
			return orig_blocks;
		}
		
		public Player getPlayer(){
			return player;
		}
	}
}
