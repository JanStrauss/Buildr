package me.simplex.buildr;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Commands {
	Buildr plugin;
	
	public Buildr_Commands(Buildr plugin) {
		this.plugin = plugin;
	}
	
	protected void cmd_globalbuild(CommandSender sender,World world){
		if (plugin.getWorldbuildmode().contains(world)) {
			plugin.leaveGlobalbuildmode(world);
			sender.sendMessage("Globalbuildmode disabled");
			for (Player inhab : world.getPlayers()) {
				inhab.sendMessage(ChatColor.AQUA+((Player)sender).getName()+" disabled the Globalbuildmode on the world you are currently in");
			}
			plugin.log("Globalbuildmode disabled in World "+world.getName());
		}
		else {
			plugin.enterGlobalbuildmode(world);
			sender.sendMessage("Globalbuildmode enabled");
			for (Player inhab : world.getPlayers()) {
				inhab.sendMessage(ChatColor.AQUA+((Player)sender).getName()+" ensabled the Globalbuildmode on the world you are currently in");
			}
			plugin.log("Globalbuildmode enabled in World "+world.getName());
		}
	}
	
	protected void cmd_build(CommandSender sender){
		if (plugin.getPlayerbuildmode().contains((Player)sender)) {
			
			sender.sendMessage(ChatColor.BLUE+"Buildmode disabled");
			plugin.leaveBuildmode((Player)sender);
		}
		else {
			
			sender.sendMessage(ChatColor.BLUE+"Buildmode enabled");
			plugin.enterBuildmode((Player)sender);
		}
	}
}