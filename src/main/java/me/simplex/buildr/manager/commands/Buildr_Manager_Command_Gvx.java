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

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Buildr_Manager_Command_Gvx extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Gvx(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("gvx")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.gvx")) {
				if (args.length == 0 ||args.length > 2) {
					return false;
				}
				if (args.length == 1) {
					this.cmd_givex(sender,args[0],-1,null);
				}
				else if (args.length==2) {
					int amount = -1;
					try {
						amount = Integer.parseInt(args[1]);
						if (amount >64) {
							amount = 64;
						}
					} catch (NumberFormatException e) {
						sendTo(sender, MsgType.ERROR, "Wrong format");
						return true;
					}
					this.cmd_givex(sender,args[0],amount,null);
				}
			}
			else {
				sendTo(sender, MsgType.ERROR, "You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_givex(CommandSender sender, String material, int amount, String player) {
		if (!plugin.getConfigValue("FEATURE_GIVEX")) {
			return;
		}
		String[]raw = material.split(":");
		String mat = raw[0];
		String data = raw[1];
		Material give_mat = null;
		byte give_data;
		try {
			give_mat = Material.getMaterial(Integer.parseInt(mat));
		} catch (NumberFormatException e) {
			give_mat = Material.matchMaterial(mat);
		}
		try {
			give_data = Byte.parseByte(data);
		} catch (NumberFormatException e) {
			give_data = 0;
		}
		if (give_mat == null) {
			sendTo(sender, MsgType.ERROR, "No such item found");
			return;
		}
		if (amount ==-1 || amount == 0) {
			amount = 64;
		}
		Player giveto = ((Player)sender);

		if (give_mat.getId() == 0) {
			sendTo(sender, MsgType.ERROR, "You can't give air");
			return;
		}
		giveto.getInventory().addItem(new ItemStack(give_mat, amount, give_data, give_data));
		sender.sendMessage("Gave "+giveto.getName()+" a stack of "+amount+" "+give_mat.toString()+"(ID: "+give_mat.getId()+")");
	}
}
