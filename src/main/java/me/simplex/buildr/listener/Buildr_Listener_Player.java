/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Buildr.
 * 
 * Buildr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Buildr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Buildr  If not, see <http://www.gnu.org/licenses/>.
 */
package me.simplex.buildr.listener;

import java.util.Set;
import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.Buildr_Runnable_TreeFeller_Collect;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Super;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Super.MsgType;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class Buildr_Listener_Player implements Listener {
	
	private final Buildr plugin;
    private final Set<Material> airSet = java.util.EnumSet.of(Material.AIR);
	
	public Buildr_Listener_Player(Buildr plugin) {
		super();
		this.plugin = plugin;
	}
	
	private void breakBlock(Block block, boolean drops){
		if (drops) {
			block.breakNaturally();
		}
		else {
			block.setType(Material.AIR);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		//block break pickaxe
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsPickaxe(player) && plugin.checkPlayerBuildMode(player)) {
			if (plugin.getConfigValue("BUILDMODE_INSTANT_BLOCK_BREAK") && plugin.checkPermission(player, "buildr.feature.instantblockbreak")) {
				
				BlockBreakEvent brk_event = new BlockBreakEvent(block, player);
				plugin.getServer().getPluginManager().callEvent(brk_event);
				
				if (!brk_event.isCancelled()) {
					if (!(plugin.checkWorldBuildMode(block.getWorld()))) {
						if (block.getType().equals(Material.BEDROCK)) {
							if (plugin.checkPermission(player, "buildr.feature.break_bedrock")) {
								breakBlock(block, true);
							}
						}
						else {
							breakBlock(block, true);
						}
					}
					else {
						breakBlock(block, false);
					}
				}
				return;
			}
		}
		
		
		//Tree feller
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsAxe(event.getPlayer()) && plugin.checkPlayerBuildMode(event.getPlayer())) {
			if (event.getClickedBlock().getType() == Material.LOG || plugin.checkTreecuterFireOnLeaves(event.getClickedBlock())) {
				if (plugin.getConfigValue("BUILDMODE_TREECUTTER")) {
					if (plugin.checkPermission(event.getPlayer(), "buildr.feature.treecutter")) {
						if (!plugin.getPlayerCuttingTree().contains(event.getPlayer())) {
							plugin.getPlayerCuttingTree().add(event.getPlayer());
							plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Buildr_Runnable_TreeFeller_Collect(event.getClickedBlock(), plugin, event.getPlayer()));
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		}
		
		//Block Break all
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.checkPlayerBuildMode(event.getPlayer())) {
			if (plugin.getConfigValue("BUILDMODE_INSTANT_BLOCK_BREAK_ALL") && plugin.checkPermission(event.getPlayer(), "buildr.feature.instantblockbreakall")) {
				
				BlockBreakEvent brk_event = new BlockBreakEvent(block, player);
				plugin.getServer().getPluginManager().callEvent(brk_event);
				
				if (!brk_event.isCancelled()) {
					if (!(plugin.checkWorldBuildMode(block.getWorld()))) {
						if (block.getType().equals(Material.BEDROCK)) {
							if (plugin.checkPermission(player, "buildr.feature.break_bedrock")) {
								breakBlock(block, true);
							}
						}
						else {
							breakBlock(block, true);
						}
					}
					else {
						breakBlock(block, false);
					}
				}
				return;
			}
			
		}
		
		// Builders
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && plugin.checkPlayerItemInHandIsStick(event.getPlayer(), event.getHand())) {
			if (!plugin.playerClickedBuildingBlock(event.getPlayer(),event.getClickedBlock())) {
				if (player.hasPermission("buildr.feature.block_info")) {
					String loc = ChatColor.GOLD + "X: " +  block.getX() +  " Y: " +  block.getY() +  " Z: " +  block.getZ() + ChatColor.WHITE;
					
					String drops = "";
					for (ItemStack is : block.getDrops()) {
						drops = drops + is.getType().toString() + "(" + is.getTypeId() + "), ";
					}
					try {
						drops = drops.substring(0, drops.length() - 2);
					} catch (IndexOutOfBoundsException e) {
						drops = "-";
					}
								
					Buildr_Manager_Command_Super.sendTo(player, MsgType.INFO, "Info for block at ["+ loc + "]:");
					Buildr_Manager_Command_Super.sendTo(player, MsgType.INFO, "Type: " + block.getType().toString() + " (" + block.getTypeId()+ ")");
					Buildr_Manager_Command_Super.sendTo(player, MsgType.INFO, "Data: " + block.getData());
					Buildr_Manager_Command_Super.sendTo(player, MsgType.INFO, "Light level: " + block.getLightLevel());
					Buildr_Manager_Command_Super.sendTo(player, MsgType.INFO, "Biome: " + block.getBiome().toString());
					Buildr_Manager_Command_Super.sendTo(player, MsgType.INFO, "Chunk: X: " + block.getChunk().getX() + " Z: " + block.getChunk().getZ());
					Buildr_Manager_Command_Super.sendTo(player, MsgType.INFO, "Drops: " + drops);
				}
			}
			return;
		}
		
		// Compass jmp
		ItemStack compassjumpitem = event.getPlayer().getItemInHand();

		if (compassjumpitem.getType() == Material.COMPASS) {

			if (event.getAction() == Action.RIGHT_CLICK_AIR
					|| event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				if (!plugin.getConfigValue("BUILDMODE_JUMP")){
					return;
				}
				if (!plugin.checkPermission(player, "buildr.feature.compassjump")){
					return;
				}

				Block target = player.getTargetBlock(airSet, 500);
				if (target == null || target.getType().equals(Material.AIR)) {
					player.sendMessage("No block in range");
					return;
				}

				Location loc = target.getLocation();
				loc.setPitch(player.getLocation().getPitch());
				loc.setYaw(player.getLocation().getYaw());
				loc.setY(loc.getY()+1);
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

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if (plugin.getConfigValue("BUILDMODE_DISABLEPICKUP")) {
			if (plugin.checkPlayerBuildMode(event.getPlayer())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		World from = event.getFrom().getWorld();
		World to = event.getTo().getWorld();
		Player player = event.getPlayer();

		if (!from.equals(to)) {
			plugin.log("worldchange fired");
			if (plugin.getConfigValue("GLOBALBUILD_FORCE_BUILDMODE") && plugin.checkWorldBuildMode(to)) {
				plugin.enterBuildmode(player);
				player.sendMessage(ChatColor.BLUE+"Buildr: "+ChatColor.WHITE+"Forced Buildmode because you ported to a world with globalbuildmode activated");
			}
			else {
				if (plugin.checkPlayerBuildMode(player)) {
					plugin.leaveBuildmode(player);
					player.sendMessage(ChatColor.BLUE+"Buildr: "+ChatColor.WHITE+"Left Buildmode because of Port between worlds");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		Location loc_to = event.getTo();
		Location loc_from = event.getFrom();
		
		if (loc_from == null || loc_to == null) {
			return;
		}
		World from = loc_from.getWorld();
		World to = loc_to.getWorld();
		Player player = event.getPlayer();

		if (!from.equals(to)) {
			plugin.log("worldchange fired");
			if (plugin.getConfigValue("GLOBALBUILD_FORCE_BUILDMODE") && plugin.checkWorldBuildMode(to)) {
				plugin.enterBuildmode(player);
				player.sendMessage(ChatColor.BLUE+"Buildr: "+ChatColor.WHITE+"Forced Buildmode because you ported to a world with globalbuildmode activated");
			}
			else {
				if (plugin.checkPlayerBuildMode(player)) {
					plugin.leaveBuildmode(player);
					player.sendMessage(ChatColor.BLUE+"Buildr: "+ChatColor.WHITE+"Left Buildmode because of Port between worlds");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.handlePlayerOnLogin(event.getPlayer());
	}
}