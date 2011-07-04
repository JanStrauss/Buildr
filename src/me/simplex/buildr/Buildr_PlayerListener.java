package me.simplex.buildr;

import me.simplex.buildr.util.Buildr_BlockToDropConv;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

public class Buildr_PlayerListener extends PlayerListener {
	
	private Buildr plugin;
	
	public Buildr_PlayerListener(Buildr plugin) {
		super();
		this.plugin = plugin;
	}

@Override
public void onPlayerInteract(PlayerInteractEvent event) {
	  if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsPickaxe(event.getPlayer()) && plugin.checkPlayerBuildMode(event.getPlayer())) {
			event.getClickedBlock().setType(Material.AIR);
			// Check for Drops
			
			if (!plugin.checkWorldBuildMode(event.getClickedBlock().getWorld())) {
				for (ItemStack stk : Buildr_BlockToDropConv.convert(event.getClickedBlock())) {
					if (stk != null) {
						event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), stk);
					}
				}
			}
			event.setCancelled(true);
	  }
}
}
