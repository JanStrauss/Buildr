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
