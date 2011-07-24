package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.Buildr_Runnable_TreeFeller_Collect;
import me.simplex.buildr.util.Buildr_Converter_BlockToDrop;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
			if (plugin.getConfigValue("BUILDMODE_INSTANT_BLOCK_BREAK") && plugin.checkPermission(event.getPlayer(), "buildr.feature.instantblockbreakpick")) {
				// Check for Drops
				if (!(plugin.checkWorldBuildMode(event.getClickedBlock().getWorld()))) {
					for (ItemStack stk : converter.convert(event.getClickedBlock())) {
						if (stk != null) {
							event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), stk);
						}
					}
				} else {
				
					event.getClickedBlock().setType(Material.AIR);
				}
					
				event.setCancelled(true);
			}
		}
		
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerBuildMode(event.getPlayer())) {
			if (plugin.getConfigValue("BUILDMODE_INSTANT_BLOCK_BREAK") && plugin.checkPermission(event.getPlayer(), "buildr.feature.instantblockbreak")) {
				// Check for Drops
				if (!(plugin.checkWorldBuildMode(event.getClickedBlock().getWorld()))) {
					for (ItemStack stk : converter.convert(event.getClickedBlock())) {
						if (stk != null) {
							event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), stk);
						}
					}
				}
				
					if (!event.getClickedBlock().getType().equals(Material.BEDROCK)) {
						event.getClickedBlock().setType(Material.AIR);
					}
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
		
		ItemStack compassjumpitem = event.getPlayer().getItemInHand();
		Player player = event.getPlayer();

		if (compassjumpitem.getType() == Material.COMPASS) {

			if (event.getAction() == Action.RIGHT_CLICK_AIR
					|| event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				if (!plugin.getConfigValue("BUILDMODE_JUMP")){
					return;
				}
				if(!plugin.checkPermission(player, "buildr.feature.compassjump")){
					return;
				}
					
				Block target = player.getTargetBlock(null, 500);
				Location loc = target.getLocation();
				loc.setPitch(player.getLocation().getPitch());
				loc.setYaw(player.getLocation().getYaw());
				loc.setY(loc.getY()+1);
				
				if (target == null || target.getType().equals(Material.AIR)) {
					player.sendMessage("No block in range");
					return;
				}
				if (target.getRelative(0, 1, 0).getType().equals(Material.AIR)&& target.getRelative(0, 2, 0).getType().equals(Material.AIR)) {
					player.teleport(loc);
				}
				else {

					loc.setY(player.getWorld().getHighestBlockYAt(loc));
					player.teleport(loc);
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
				plugin.importantLog("Treated "+event.getPlayer().getName()+". Inventory restored.");
				plugin.leaveBuildmode(event.getPlayer());
			}
	}
}