package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

public class Buildr_EntityListener extends EntityListener {
	private Buildr plugin;
	
	public Buildr_EntityListener(Buildr plugin) {
		this.plugin = plugin;
	}
	
// Godmode: No Dmg
@Override
public void onEntityDamage(EntityDamageEvent event) {
	if (event.getEntity() instanceof Player) {
		if (plugin.checkPlayerBuildMode((Player)event.getEntity())) {
			event.setCancelled(true);
		}
	}
}

//Globalbuildmode: No Drops
@Override
	public void onItemSpawn(ItemSpawnEvent event) {
		if (plugin.checkWorldBuildMode(event.getLocation().getWorld())) {
			event.setCancelled(true);
		}
	}

//Godmode: No Target
@Override
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player) {
			if (plugin.checkPlayerBuildMode((Player)event.getTarget())) {
				event.setCancelled(true);
			}
		}
	}

}
