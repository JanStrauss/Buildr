package me.simplex.buildr.runnable;

import me.simplex.buildr.Buildr;

import org.bukkit.World;

public class Buildr_Runnable_TimeChecker implements Runnable {
	private Buildr plugin;

	public Buildr_Runnable_TimeChecker(Buildr plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (World world : plugin.getWorldBuildMode()) {
			plugin.log("Time: " + world.getTime());
			plugin.log("FullTime: " + world.getFullTime());
			if (plugin.getConfigValue("GLOBALBUILD_TIME")) {
				if (world.getTime() >= 9000) {
					world.setTime(0);
					plugin.log("time reset");
				}
			}
		}
		plugin.log("time checked");
	}
}
