package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Buildr_BlockListener extends BlockListener {
	Buildr plugin;
	
	public Buildr_BlockListener(Buildr buildr) {
		this.plugin = buildr;
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		//System.out.println("Block Place");
		if (plugin.checkPlayerBuildMode(event.getPlayer())) {
			event.getPlayer().setItemInHand(new ItemStack(event.getItemInHand().getType(), 64));
			//System.out.println("itemchanged");
		}

	}


}
