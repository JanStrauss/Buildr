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
package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Build extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Build(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("build")) {
			if (args.length != 0) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.build")) {
				this.cmd_build(sender);
			}
			else {
				sendTo(sender, MsgType.ERROR, "You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_build(CommandSender sender){
		if (!plugin.getConfigValue("BUILDMODE_ENABLE")) {
			return;
		}
		if (plugin.checkPlayerBuildMode((Player)sender)) {
			if (plugin.getConfigValue("GLOBALBUILD_FORCE_BUILDMODE") && plugin.checkWorldBuildMode(((Player)sender).getWorld())) {
				sendTo(sender, MsgType.WARNING, "Buildmode is forced in this world. You can't disable it here.");
				return;
			}
			sendTo(sender, MsgType.INFO, "Buildmode disabled");
			plugin.leaveBuildmode((Player)sender);
		}
		else {
			sendTo(sender, MsgType.INFO, "Buildmode enabled");
			plugin.enterBuildmode((Player)sender);
		}
	}

}
