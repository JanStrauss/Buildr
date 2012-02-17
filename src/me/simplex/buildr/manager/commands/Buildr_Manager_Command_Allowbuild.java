package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Allowbuild extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Allowbuild(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		 if (command.getName().equalsIgnoreCase("allowbuild")) {
				if (plugin.checkPermission((Player)sender, "buildr.cmd.allowbuild")) {
					if (args.length!=0) {
						return false;
					}
					this.cmd_allowbuild(sender);
				}
				else {
					sendTo(sender, MsgType.ERROR, "You dont have the permission to perform this action");
				}
				return true;
			}
		return false;
	}
	
	public void cmd_allowbuild(CommandSender sender) {
		if (!plugin.getConfigValue("BUILDMODE_REQUIRE_ALLOW")) {
			return;
		}
		World world = ((Player)sender).getWorld();
		if (plugin.checkWorldHasBuildmodeUnlocked(world)) {
			plugin.getWorldBuildmodeAllowed().remove(world);
			for (Player player : world.getPlayers()) {
				if (plugin.checkPlayerBuildMode(player)) {
					plugin.leaveBuildmode(player);
				}
				sendTo(player, MsgType.INFO, ((Player)sender).getName()+" locked the Buildmode in this world");
			}
		}
		else {
			plugin.getWorldBuildmodeAllowed().add(world);
			for (Player player : world.getPlayers()) {
				sendTo(player, MsgType.INFO, ((Player)sender).getName()+" unlocked the Buildmode in this world");
			}
		}
	}
}
