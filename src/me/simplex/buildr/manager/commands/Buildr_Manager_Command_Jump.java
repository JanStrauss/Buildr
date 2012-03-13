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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Jump extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Jump(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("jump")) {
			try {
				this.cmd_jump(sender);
			} catch (IllegalStateException e) {
				sendTo(sender, MsgType.WARNING, "move a litte, seems like bukkit doesn't like your current position");
			}
			
			return true;
		}
		return false;
	}
	
	public void cmd_jump(CommandSender sender){
		Player player = (Player)sender;
		if (!plugin.getConfigValue("BUILDMODE_JUMP")){
			return;
		}
		if(!plugin.checkPermission(player, "buildr.feature.jump")){
			return;
		}
			
		Block target = player.getTargetBlock(null, 500);
		Location loc = target.getLocation();
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		loc.setY(loc.getY()+1);
		
		if (target == null || target.getType().equals(Material.AIR)) {
			sendTo(player, MsgType.WARNING, "no block in range");
			return;
		}
		if (target.getRelative(0, 1, 0).getType().equals(Material.AIR)&& target.getRelative(0, 2, 0).getType().equals(Material.AIR)) {
			player.teleport(loc);
		}
		else {

			loc.setY(player.getWorld().getHighestBlockYAt(loc));
			player.teleport(loc);
		}
	}
}
