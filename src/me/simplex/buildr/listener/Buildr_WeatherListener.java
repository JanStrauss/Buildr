package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.WeatherListener;

public class Buildr_WeatherListener extends WeatherListener {
	private Buildr plugin;
@Override
public void onWeatherChange(WeatherChangeEvent event) {
	if (plugin.checkWorldBuildMode(event.getWorld())) {
		event.setCancelled(true);
		event.getWorld().setStorm(false);
		event.getWorld().setThundering(false);
	}
}
}
