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
