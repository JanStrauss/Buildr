package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.Buildr_Runnable_TreeFeller_Collect;
import me.simplex.buildr.util.Buildr_Converter_BlockToDrop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class Buildr_Listener_Player extends PlayerListener {
	
	private Buildr plugin;
	private Buildr_Converter_BlockToDrop converter;
	
	public Buildr_Listener_Player(Buildr plugin) {
		super();
		this.plugin = plugin;
		this.converter = new Buildr_Converter_BlockToDrop();
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
			plugin.playerClickedBuildingBlock(event.getPlayer(),event.getClickedBlock());
		}
		
		else if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsAxe(event.getPlayer()) && plugin.checkPlayerBuildMode(event.getPlayer())) {
			if (event.getClickedBlock().getType() == Material.LOG || plugin.checkTreecuterFireOnLeaves(event.getClickedBlock())) {
				if (plugin.getConfigValue("BUILDMODE_TREECUTTER")) {
					if (plugin.checkPermission(event.getPlayer(), "buildr.feature.treecutter")) {
						if (!plugin.getPlayerCuttingTree().contains(event.getPlayer())) {
							plugin.getPlayerCuttingTree().add(event.getPlayer());
							plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Buildr_Runnable_TreeFeller_Collect(event.getClickedBlock(), plugin, event.getPlayer()));
							event.setCancelled(true);
						}
					}
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
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
			System.out.println("worldchange fired");
			if (plugin.checkPlayerBuildMode(event.getPlayer())) {
				plugin.leaveBuildmode(event.getPlayer());
				event.getPlayer().sendMessage(ChatColor.BLUE+"Buildr: "+ChatColor.WHITE+"Left Buildmode because of Port between worlds");
			}
		}
	}
	@Override
	public void onPlayerPortal(PlayerPortalEvent event) {
		if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
			System.out.println("worldchange fired");
			if (plugin.checkPlayerBuildMode(event.getPlayer())) {
				plugin.leaveBuildmode(event.getPlayer());
				event.getPlayer().sendMessage(ChatColor.BLUE+"Buildr: "+ChatColor.WHITE+"Left Buildmode because of Port between worlds");
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