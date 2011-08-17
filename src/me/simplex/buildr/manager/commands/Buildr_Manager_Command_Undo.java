package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.Buildr_Runnable_Undo;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Undo extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Undo(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("undo")) {
			if (args.length != 0) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.undo")) {
				this.cmd_undo(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_undo(CommandSender sender){		
		if (!plugin.getConfigValue("FEATURE_AIRFLOOR") && 
			!plugin.getConfigValue("FEATURE_WALLBUILDER") && 
			!plugin.getConfigValue("FEATURE_WALLXBUILDER") && 
			!plugin.getConfigValue("BUILDMODE_TREECUTTER")) {
			return;
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Undo((Player)sender, plugin));
	}
}
