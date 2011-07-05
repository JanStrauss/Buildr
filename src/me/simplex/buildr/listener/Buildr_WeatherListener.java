package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

public class Buildr_WeatherListener extends WeatherListener {
	private Buildr plugin;
	
public Buildr_WeatherListener(Buildr buildr) {
		this.plugin = buildr;
	}

@Override
public void onWeatherChange(WeatherChangeEvent event) {
	if (plugin.checkWorldBuildMode(event.getWorld())) {
		if (event.toWeatherState()) {
			event.setCancelled(true);
		}
	}
}
}
