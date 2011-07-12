package me.simplex.buildr.manager;

import java.util.ArrayList;
import java.util.HashMap;

import me.simplex.buildr.util.Buildr_Container_UndoBlock;
import me.simplex.buildr.util.Buildr_Stack_Undo;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_UndoStack {
	private ArrayList<Buildr_StackItem> playerUndos;

	
	public Buildr_Manager_UndoStack() {
		playerUndos = new ArrayList<Buildr_StackItem>();
	}

	public void addToStack(HashMap<Block, Buildr_Container_UndoBlock> blocks, Player player){
		if (checkPlayerUndoStackExist(player)) {
			giveStack(player).push(blocks);
		}
		else {
			newStack(player,blocks);
		}
	}
	
	private void newStack(Player player, HashMap<Block, Buildr_Container_UndoBlock> blocks) {
		playerUndos.add(new Buildr_StackItem(player, blocks));
	}

	public HashMap<Block, Buildr_Container_UndoBlock> getAndDeleteFromStack(Player player){
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
		
		public Buildr_StackItem(Player player, HashMap<Block, Buildr_Container_UndoBlock> blocks) {
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
