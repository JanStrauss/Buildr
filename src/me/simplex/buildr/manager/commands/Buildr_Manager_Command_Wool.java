package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;
import me.simplex.buildr.util.Buildr_Type_Wool;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Wool extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Wool(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("wool")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.wool")) {
				if (args.length!=1) {
					return false;
				}
				this.cmd_wool(sender, args[0]);
			}
			else {
				sendToSender(sender, MsgType.ERROR, "You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_wool(CommandSender sender, String args) {
		if (!plugin.getConfigValue("FEATURE_WOOL")) {
			return;
		}
		String upcase= args.toUpperCase();
		Buildr_Type_Wool woolcolor;
		try {
			woolcolor = Enum.valueOf(Buildr_Type_Wool.class, upcase);
		} catch (IllegalArgumentException e) {
			sender.sendMessage("No such color");
			return;
		}
		((Player)sender).getInventory().addItem(woolcolor.giveStack());
		String ret = "Gave yourself a stack of "+woolcolor.toString().toLowerCase()+" wool";
		sendToSender(sender, MsgType.INFO, ret);
	}
}
