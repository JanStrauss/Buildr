package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class Buildr_Listener_Weather implements Listener {
	private Buildr plugin;
	
	public Buildr_Listener_Weather(Buildr buildr) {
		this.plugin = buildr;
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (plugin.checkWorldBuildMode(event.getWorld())) {
			if (plugin.getConfigValue("GLOBALBUILD_WEATHER")) {
				if (event.toWeatherState()) {
					event.setCancelled(true);
					event.getWorld().setStorm(false);
				}
			}

		}
	}
}
