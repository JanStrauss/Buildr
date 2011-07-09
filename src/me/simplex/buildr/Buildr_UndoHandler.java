package me.simplex.buildr;

import java.util.ArrayList;
import java.util.HashMap;

import me.simplex.buildr.util.Buildr_UndoBlockContainer;
import me.simplex.buildr.util.Buildr_UndoStack;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_UndoHandler {
	private ArrayList<Buildr_StackItem> playerUndos;

	
	public Buildr_UndoHandler() {
		playerUndos = new ArrayList<Buildr_StackItem>();
	}

	public void addToStack(HashMap<Block, Buildr_UndoBlockContainer> blocks, Player player){
		if (checkPlayerUndoStackExist(player)) {
			giveStack(player).push(blocks);
		}
		else {
			newStack(player,blocks);
		}
	}
	
	private void newStack(Player player, HashMap<Block, Buildr_UndoBlockContainer> blocks) {
		playerUndos.add(new Buildr_StackItem(player, blocks));
	}

	public HashMap<Block, Buildr_UndoBlockContainer> getAndDeleteFromStack(Player player){
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
	
	private Buildr_UndoStack giveStack(Player player){
		for (Buildr_StackItem undos : playerUndos) {
			if (undos.getPlayer() == player) {
				return undos.getOrig_blocks();
			}
		}
		return null;
	}
	
	private class Buildr_StackItem{
		Buildr_UndoStack orig_blocks;
		Player player;
		
		public Buildr_StackItem(Player player, HashMap<Block, Buildr_UndoBlockContainer> blocks) {
			this.orig_blocks = new Buildr_UndoStack(20);
			this.orig_blocks.push(blocks);
			this.player = player;
		}

		public Buildr_UndoStack getOrig_blocks() {
			return orig_blocks;
		}
		
		public Player getPlayer(){
			return player;
		}
	}
}
