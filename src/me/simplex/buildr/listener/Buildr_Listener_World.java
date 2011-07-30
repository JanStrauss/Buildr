package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.World;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

public class Buildr_Listener_World extends WorldListener {
	private Buildr plugin;
	public Buildr_Listener_World(Buildr plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onWorldLoad(WorldLoadEvent event) {
		plugin.log("handle worldload");
		World world = event.getWorld();
		if (plugin.getConfigValue(world.getName())) {
			plugin.enterGlobalbuildmode(world);
			plugin.log("Enabled Globalbuildmode for world "+world.getName()+". (ConfigValue)");
		}
	}
}