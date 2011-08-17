package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.manager.builder.Buildr_Manager_Builder_Sphere;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;
import me.simplex.buildr.util.Buildr_Type_Wool;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Sphere extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Sphere(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("sphere")) {
			if (args.length < 1 || args.length > 3) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.sphere")) {
				if (args.length >=1 && args.length <=3) {
					Material material;
					int id;
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						id = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase().substring(5));
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sender.sendMessage("No such wool");
							return true;
						}
					}
					else {
						try {
							id = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								id = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {
								sender.sendMessage(ChatColor.RED+"wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sender.sendMessage(ChatColor.RED+"unvalid blocktype");
						return true;
					}
					if (args.length==1) {
							this.cmd_sphere(sender, material, mat_data, false, false);
							return true;
					}
					else if (args.length == 2) {
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							this.cmd_sphere(sender, material, mat_data, true,false);
							return true;
						}
						else if(args[1].equalsIgnoreCase("h") || args[1].equalsIgnoreCase("hollow")) {
							this.cmd_sphere(sender, material, mat_data, false, true);
							return true;
						}
						else {
							return false;
						}
					}
					else if (args.length == 3) {
						boolean hollow = false;
						boolean aironly = false;
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							aironly = true;
						}
						if(args[2].equalsIgnoreCase("h") || args[2].equalsIgnoreCase("hollow")) {
							hollow = true;
						}
						this.cmd_sphere(sender, material, mat_data, aironly, hollow);
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_sphere(CommandSender sender, Material material, byte material_data, boolean aironly,boolean hollow) {
		if (!plugin.getConfigValue("FEATURE_BUILDER_SPHERE")) {
			return;
		}
		if (plugin.checkPlayerHasStartedBuilding((Player)sender)) {
			plugin.removeStartedBuilding((Player)sender);
			sender.sendMessage(ChatColor.YELLOW+"previous started building aborted.");
		}
		plugin.getStartedBuildings().add(new Buildr_Manager_Builder_Sphere((Player)sender, material, aironly, hollow, false, plugin,material_data));
		String buildinfo ="Started new Sphere. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") Aironly: "+ChatColor.BLUE+aironly+ChatColor.WHITE+" Hollow: "+ChatColor.BLUE+hollow;
		sender.sendMessage(buildinfo);
		sender.sendMessage("Rightclick on the center of your sphere while holding a stick to continue");
	}
}
