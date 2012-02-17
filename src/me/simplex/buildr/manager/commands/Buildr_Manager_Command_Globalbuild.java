package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;

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
					sendToSender(sender, MsgType.ERROR, "There is no world with this name");
				}
				return true;
			}
			else {
				sendToSender(sender, MsgType.ERROR, "You dont have the permission to perform this action");
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
				SendToPlayer(inhab, MsgType.INFO, ((Player)sender).getName()+" disabled the Globalbuildmode on the world you are currently in");
			}
			plugin.log("Globalbuildmode disabled in World "+world.getName());
		}
		else {
			sender.sendMessage(ChatColor.YELLOW+"Globalbuildmode enabled");
			for (Player inhab : world.getPlayers()) {
				SendToPlayer(inhab, MsgType.INFO, ((Player)sender).getName()+" ensabled the Globalbuildmode on the world you are currently in");
			}
			plugin.enterGlobalbuildmode(world);
			plugin.log("Globalbuildmode enabled in World "+world.getName());
		}
	}

}
