package me.simplex.buildr;

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
			plugin.getWorldbuildmode().remove(world);
			sender.sendMessage("Globalbuildmode enabled");
			for (Player inhab : world.getPlayers()) {
				inhab.sendMessage(sender+" enabled the Globalbuildmode on the world you are currently in");
			}
		}
		else {
			plugin.getWorldbuildmode().add(world);
			sender.sendMessage("Globalbuildmode disabled");
			for (Player inhab : world.getPlayers()) {
				inhab.sendMessage(sender+" disabled the Globalbuildmode on the world you are currently in");
			}
		}
	}
	
	protected void cmd_build(CommandSender sender){
		if (plugin.getPlayerbuildmode().contains((Player)sender)) {
			plugin.getPlayerbuildmode().remove((Player)sender);
			sender.sendMessage("Buildmode enabled");
			plugin.enterBuildmode((Player)sender);
		}
		else {
			plugin.getPlayerbuildmode().add((Player)sender);
			sender.sendMessage("Buildmode disabled");
			plugin.leaveBuildmode((Player)sender);
		}
	}

}