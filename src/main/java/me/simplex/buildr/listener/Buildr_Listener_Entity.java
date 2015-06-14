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

import me.simplex.buildr.Buildr;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

public class Buildr_Listener_Entity implements Listener {
	private Buildr plugin;

	public Buildr_Listener_Entity(Buildr plugin) {
		this.plugin = plugin;
	}

	// Godmode: No Dmg
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (plugin.getConfigValue("BUILDMODE_GODMODE")) {
				if (plugin.checkPlayerBuildMode((Player) event.getEntity())) {
					event.setCancelled(true);
				}
			}
		}
	}

	// Globalbuildmode: No Drops
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		if (plugin.getConfigValue("GLOBALBUILD_NODROPS")) {
			if (plugin.checkWorldBuildMode(event.getLocation().getWorld())) {
				event.setCancelled(true);
			}
		}
	}

	// Godmode: No Target
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player) {
			if (plugin.getConfigValue("BUILDMODE_GODMODE")) {
				if (plugin.checkPlayerBuildMode((Player) event.getTarget())) {
					event.setCancelled(true);
				}
			}
		}
	}

}
