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
package me.simplex.buildr.util;

import me.simplex.buildr.Buildr;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Buildr_Manager_Command_Super implements CommandExecutor {
	protected Buildr plugin;
	
	public Buildr_Manager_Command_Super(Buildr plugin) {
		this.plugin = plugin;
	}
	
	protected boolean checkSenderIsPlayer(CommandSender s){
		return (s instanceof Player); //disable console 
	}
	
	public enum MsgType {INFO, WARNING, ERROR};
	
	public static void sendTo(Player player, MsgType type, String msg){
		player.sendMessage(BuildMsg(type, msg));
	}
	
	public static void sendTo(CommandSender sender, MsgType type, String msg){
		sender.sendMessage(BuildMsg(type, msg));
	}
	
	private static String BuildMsg(MsgType type, String msg){
		String PREFIX = "[Buildr] ";
		ChatColor MsgColor = ChatColor.WHITE;

		switch (type) {
			case INFO: 	  MsgColor = ChatColor.WHITE;  break;
			case WARNING: MsgColor = ChatColor.YELLOW; break;
			case ERROR:   MsgColor = ChatColor.RED;    break;
			default: break;
		}
		
		return ChatColor.BLUE + PREFIX + MsgColor + msg;
	}
	
	protected Material parseMaterial(String mat_re){
		int id = -1;
		try {
			id = Integer.parseInt(mat_re);
		} 
		catch (NumberFormatException e) {
			try {
				id = Material.matchMaterial(mat_re).getId();
			} 
			catch (NullPointerException e2) {
			}
		}
		if (id != -1 && Material.getMaterial(id).isBlock()) {
			return Material.getMaterial(id);
		}
		else {
			return null;
		}
	}
}