package me.simplex.buildr.runnable;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Runnable_Undo implements Runnable {
	private Player player;
	private HashMap<Block, Buildr_Container_UndoBlock> undos;
	private Buildr plugin;
	
public Buildr_Runnable_Undo(Player player, Buildr plugin) {
	this.player = player;
	this.plugin = plugin;
	this.undos = plugin.getUndoList().getAndDeleteFromStack(player);
}
	@Override
	public void run() {
		if (undos != null) {
			for (Block block : undos.keySet()) {
				block.setType(undos.get(block).getMaterial());
				block.setData(undos.get(block).getMaterialData());
			}
			plugin.log(player.getName()+" used /undo: "+undos.size()+" blocks affected");
			player.sendMessage(undos.size()+" blocks restored");
		}
		else {
			player.sendMessage("Nothing to undo");
		}
		
	}

}
