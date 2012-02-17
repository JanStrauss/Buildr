package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Buildr_Listener_Block implements Listener {
	Buildr plugin;
	
	public Buildr_Listener_Block(Buildr buildr) {
		this.plugin = buildr;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (plugin.getConfigValue("BUILDMODE_UNLIMITED_ITEMSTACK")) {
			if (plugin.checkPlayerBuildMode(event.getPlayer())) {
				//ItemStack give = event.getItemInHand();
				ItemStack give = event.getPlayer().getItemInHand();
				give.setAmount(64);
				//System.out.println("dmg: "+give.getDurability());
				//System.out.println("type: "+give.getType().toString());
				
				event.getPlayer().setItemInHand(give);
			}
		}
		System.out.println("BlockPlace");
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		System.out.println("BlockBreak");
	}
}