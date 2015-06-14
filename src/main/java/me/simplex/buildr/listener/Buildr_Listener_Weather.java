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
