package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Buildr_Listener_Block extends BlockListener {
	Buildr plugin;
	
	public Buildr_Listener_Block(Buildr buildr) {
		this.plugin = buildr;
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (plugin.getConfigValue("BUILDMODE_UNLIMITED_ITEMSTACK")) {
			if (plugin.checkPlayerBuildMode(event.getPlayer())) {
				event.getPlayer().setItemInHand(new ItemStack(event.getItemInHand().getType(), 64));
			}
		}
	}
}