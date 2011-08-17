package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Jump extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Jump(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("jump")) {
			this.cmd_jump(sender);
			return true;
		}
		return false;
	}
	
	public void cmd_jump(CommandSender sender){
		Player player = (Player)sender;
		if (!plugin.getConfigValue("BUILDMODE_JUMP")){
			return;
		}
		if(!plugin.checkPermission(player, "buildr.feature.jump")){
			return;
		}
			
		Block target = player.getTargetBlock(null, 500);
		Location loc = target.getLocation();
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		loc.setY(loc.getY()+1);
		
		if (target == null || target.getType().equals(Material.AIR)) {
			player.sendMessage("No block in range");
			return;
		}
		if (target.getRelative(0, 1, 0).getType().equals(Material.AIR)&& target.getRelative(0, 2, 0).getType().equals(Material.AIR)) {
			player.teleport(loc);
		}
		else {

			loc.setY(player.getWorld().getHighestBlockYAt(loc));
			player.teleport(loc);
		}
	}
}
