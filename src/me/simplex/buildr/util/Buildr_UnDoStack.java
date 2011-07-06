package me.simplex.buildr.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import me.simplex.buildr.Buildr;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_UnDoStack {
	private ArrayList<Buildr_StackItem> playerUndos;
	private Buildr plugin;
	
	public Buildr_UnDoStack(Buildr buildr) {
		this.plugin = buildr;
		playerUndos = new ArrayList<Buildr_StackItem>();
	}

	public void addToStack(HashMap<Block, Material> blocks, Player player){
		if (checkPlayerUndoStackExist(player)) {
			giveStack(player).push(blocks);
		}
		else {
			newStack(player,blocks);
		}
	}
	
	private void newStack(Player player, HashMap<Block, Material> blocks) {
		playerUndos.add(new Buildr_StackItem(player, blocks));
	}

	public HashMap<Block, Material> getAndDeleteFromStack(Player player){
		if (checkPlayerUndoStackExist(player)) {
			if (!giveStack(player).empty()) {
				plugin.log(player.getName()+" used /bu: "+giveStack(player).pop().size()+" blocks affected");
				return giveStack(player).pop();
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
	
	private Stack<HashMap<Block, Material>> giveStack(Player player){
		for (Buildr_StackItem undos : playerUndos) {
			if (undos.getPlayer() == player) {
				return undos.getOrig_blocks();
			}
		}
		return null;
	}
	
	private class Buildr_StackItem{
		Stack<HashMap<Block, Material>> orig_blocks;
		Player player;
		
		public Buildr_StackItem(Player player, HashMap<Block, Material> blocks) {
			this.orig_blocks = new Stack<HashMap<Block, Material>>();
			this.orig_blocks.add(blocks);
			this.player = player;
		}

		public Stack<HashMap<Block, Material>> getOrig_blocks() {
			return orig_blocks;
		}
		
		public Player getPlayer(){
			return player;
		}


	}
}
