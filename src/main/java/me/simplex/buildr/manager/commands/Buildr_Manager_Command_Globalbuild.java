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

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Globalbuild extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Globalbuild(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("globalbuild")) {
			if (args.length != 0) {
				return false;
			}
			
			if (plugin.checkPermission((Player)sender, "buildr.cmd.globalbuild")) {
				World world;
				if (args.length > 0) {
					world = plugin.getServer().getWorld(args[0]);
				}
				else {
					world = ((Player)sender).getWorld();
				}
				if (world != null) {
					this.cmd_globalbuild(sender, world);
				}
				else {
					sendTo(sender, MsgType.ERROR, "There is no world with this name");
				}
				return true;
			}
			else {
				sendTo(sender, MsgType.ERROR, "You dont have the permission to perform this action");
				return true;
			}
			
		}
		return false;
	}
	
	public void cmd_globalbuild(CommandSender sender,World world){
		if (!plugin.getConfigValue("GLOBALBUILD_ENABLE")) {
			return;
		}
		if (plugin.checkWorldBuildMode(world)) {
			plugin.leaveGlobalbuildmode(world);
			sender.sendMessage(ChatColor.YELLOW+"Globalbuildmode disabled");
			for (Player inhab : world.getPlayers()) {
				sendTo(inhab, MsgType.INFO, ((Player)sender).getName()+" disabled the Globalbuildmode for the world you are currently in");
			}
			plugin.log("Globalbuildmode disabled in World "+world.getName());
		}
		else {
			sender.sendMessage(ChatColor.YELLOW+"Globalbuildmode enabled");
			for (Player inhab : world.getPlayers()) {
				sendTo(inhab, MsgType.INFO, ((Player)sender).getName()+" enabled the Globalbuildmode for the world you are currently in");
			}
			plugin.enterGlobalbuildmode(world);
			plugin.log("Globalbuildmode enabled in World "+world.getName());
		}
	}

}
