package me.simplex.buildr.util;

import me.simplex.buildr.Buildr;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Buildr_Manager_Command_Super implements CommandExecutor {
	protected Buildr plugin;
	
	public Buildr_Manager_Command_Super(Buildr plugin) {
		this.plugin = plugin;
	}
	
	protected boolean checkSenderIsPlayer(CommandSender s){
		if (s instanceof Player) { //disable console 
			return true;
		}
		return false;
	}
}