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
