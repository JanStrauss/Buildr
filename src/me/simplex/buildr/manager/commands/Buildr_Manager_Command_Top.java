package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Top extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Top(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("top")) {
			if (args.length != 0) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.top")) {
				this.cmd_top(sender);
			}
			else {
				sendToSender(sender, MsgType.ERROR, "You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_top(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_TOP")) {
			return;
		}
		Player player = (Player)sender;
		if (player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getWorld().getHighestBlockYAt(player.getLocation()), player.getLocation().getZ(),player.getLocation().getYaw(),player.getLocation().getPitch()))) 
		{
			sendToSender(sender, MsgType.INFO, "Ported to top at height " + player.getLocation().getBlockY());
		}
		else {
			sendToSender(sender, MsgType.WARNING, "Something went wrong..");
		}
	}
}
