package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_ClearInv extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_ClearInv(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("clearinv")) {
			if (args.length != 0) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.clearinv")) {
				if (args.length!=0) {
					return false;
				}
				this.cmd_clrInv(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_clrInv(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_CLEAR_INVENTORY")) {
			return;
		}
		((Player)sender).getInventory().clear();
		sender.sendMessage("Inventory cleared");
	}
}
