package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.Buildr_TreeFeller;
import me.simplex.buildr.util.Buildr_BlockToDropConverter;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Buildr_PlayerListener extends PlayerListener {
	
	private Buildr plugin;
	private Buildr_BlockToDropConverter converter;
	
	public Buildr_PlayerListener(Buildr plugin) {
		super();
		this.plugin = plugin;
		this.converter = new Buildr_BlockToDropConverter(plugin);
	}

@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsPickaxe(event.getPlayer()) && plugin.checkPlayerBuildMode(event.getPlayer())) {
			if (plugin.getConfigValue("BUILDMODE_INSTANT_BLOCK_BREAK")) {
				// Check for Drops
				if (!(plugin.checkWorldBuildMode(event.getClickedBlock().getWorld()))) {
					for (ItemStack stk : converter.convert(event.getClickedBlock())) {
						if (stk != null) {
							event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), stk);
						}
					}
				}
				event.getClickedBlock().setType(Material.AIR);
				event.setCancelled(true);
			}
		}
		else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsStick(event.getPlayer())) {
			plugin.playerClickedWallBlock(event.getPlayer(),event.getClickedBlock());
		}
		else if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsAxe(event.getPlayer()) && plugin.checkPlayerBuildMode(event.getPlayer())) {
			if (plugin.getConfigValue("BUILDMODE_TREECUTTER")) {
				if (plugin.checkPermission(event.getPlayer(), "buildr.treecutter")) {
					new Thread(new Buildr_TreeFeller(event.getClickedBlock(), plugin, event.getPlayer())).start();
				}
			}
		}
	}

	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (plugin.getConfigValue("BUILDMODE_DISABLEPICKUP")) {
			if (plugin.checkPlayerBuildMode(event.getPlayer())) {
				event.setCancelled(true);
			}
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.checkPlayerIsToProcess(event.getPlayer());
			if (plugin.checkPlayerBuildMode(event.getPlayer())) {
				plugin.log("Treated "+event.getPlayer().getName()+". Inventory restored.");
				plugin.leaveBuildmode(event.getPlayer());
			}
	}
}