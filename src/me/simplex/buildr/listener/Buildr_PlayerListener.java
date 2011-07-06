package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_BlockToDropConverter;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
		else if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsStick(event.getPlayer())) {
			plugin.playerClickedWallBlock(event.getPlayer(),event.getClickedBlock());
		}
	}

	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (plugin.checkPlayerBuildMode(event.getPlayer())) {
			event.setCancelled(true);
		}
		//System.out.println("pickup: "+event.getItem().getItemStack().getTypeId()+"cancelled: "+event.isCancelled());
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (plugin.checkPlayerBuildMode(event.getPlayer())) {
			plugin.leaveBuildmode(event.getPlayer());
			plugin.log("removed "+event.getPlayer().getName()+" from builders");
		}
	}
	
	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		if (plugin.checkPlayerBuildMode(event.getPlayer())) {
			plugin.leaveBuildmode(event.getPlayer());
			plugin.log("removed "+event.getPlayer().getName()+" from builders");
		}
	}
}
