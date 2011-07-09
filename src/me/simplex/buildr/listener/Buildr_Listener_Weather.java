package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

public class Buildr_Listener_Weather extends WeatherListener {
	private Buildr plugin;
	
public Buildr_Listener_Weather(Buildr buildr) {
		this.plugin = buildr;
	}

@Override
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
