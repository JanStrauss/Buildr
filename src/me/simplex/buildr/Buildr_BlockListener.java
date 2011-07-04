package me.simplex.buildr;

import me.simplex.buildr.util.Buildr_BlockToDropConv;

import org.bukkit.Material;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.ItemStack;

public class Buildr_BlockListener extends BlockListener {
	private Buildr plugin;

	public Buildr_BlockListener(Buildr buildr) {
		plugin = buildr;
	}
	
	// Instant block brake
	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		if (plugin.checkPlayerBuildMode(event.getPlayer())) {
			event.getBlock().setType(Material.AIR);
			// Check for Drops
			if (!plugin.checkWorldBuildMode(event.getBlock().getWorld())) {
				for (ItemStack stk : Buildr_BlockToDropConv.convert(event.getBlock())) {
					if (stk != null) {
						event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), stk);
					}
				}
			}
			event.setCancelled(true);
		}
	} 
}
