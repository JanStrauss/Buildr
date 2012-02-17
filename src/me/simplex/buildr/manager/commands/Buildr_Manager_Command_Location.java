package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Location extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Location(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("location")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.location")) {
				if (args.length!=0) {
					return false;
				}
				this.cmd_location(sender);
			}
			else {
				sendTo(sender, MsgType.ERROR, "You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_location(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_LOCATION")) {
			return;
		}
		Block block = ((Player)sender).getLocation().getBlock();
		String pos = "["+ChatColor.BLUE+block.getX()+ChatColor.WHITE+", "+ChatColor.BLUE+(block.getY()-1)+ChatColor.WHITE+", "+ChatColor.BLUE+block.getZ()+ChatColor.WHITE+"]";
		sendTo(sender, MsgType.INFO, "Coordinates of the block beneath you: " + pos);
		
	}
}
