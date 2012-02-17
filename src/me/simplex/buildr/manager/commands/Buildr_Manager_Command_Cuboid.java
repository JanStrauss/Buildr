package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.manager.builder.Buildr_Manager_Builder_Cuboid;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;
import me.simplex.buildr.util.Buildr_Type_Wool;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Cuboid extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Cuboid(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("cuboid")) {
			if (args.length < 1 || args.length > 3) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.cuboid")) {
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
							sendToSender(sender, MsgType.ERROR, "No such wool");
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
								sendToSender(sender, MsgType.ERROR, "Wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sendToSender(sender, MsgType.ERROR, "invalid blocktype");
						return true;
					}
					if (args.length==1) {
							this.cmd_cuboid(sender, material, mat_data, false, false, null);
							return true;
					}
					else if (args.length == 2) {
						if (args[1].startsWith("r")) {
							Material rep = parseMaterial(args[1].substring(1));
							this.cmd_cuboid(sender, material, mat_data, true,false, rep);
							return true;
						}
						else if(args[1].equalsIgnoreCase("h") || args[1].equalsIgnoreCase("hollow")) {
							this.cmd_cuboid(sender, material, mat_data, false, true, null);
							return true;
						}
						else {
							return false;
						}
					}
					else if (args.length == 3) {
						Material rep = parseMaterial(args[1].substring(1));
						if (!args[1].startsWith("r")) {
							return false;
						}
						if (!args[2].equalsIgnoreCase("h") || !args[2].equalsIgnoreCase("hollow")) {
							return false;
						}
						this.cmd_cuboid(sender, material, mat_data, true, true, rep);
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
				sendToSender(sender, MsgType.ERROR, "You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_cuboid(CommandSender sender, Material material, byte material_data, boolean replace,boolean hollow, Material replace_mat) {
		if (!plugin.getConfigValue("FEATURE_BUILDER_CUBOID")) {
			return;
		}
		if (plugin.checkPlayerHasStartedBuilding((Player)sender)) {
			plugin.removeStartedBuilding((Player)sender);
			sendToSender(sender, MsgType.WARNING, "previous started building aborted");
		}
		String replace_info ="";
		if (replace) {
			replace_info = "Replace: "+ChatColor.BLUE+replace_mat;
		}
		plugin.getStartedBuildings().add(new Buildr_Manager_Builder_Cuboid((Player)sender, material, replace, replace_mat, hollow, plugin,material_data));
		String buildinfo = "Started new Cuboid. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") "+replace_info+ChatColor.WHITE+" Hollow: "+ChatColor.BLUE+hollow;
		sendToSender(sender, MsgType.INFO, buildinfo);
		sendToSender(sender, MsgType.INFO, "Rightclick on block 1 while holding a stick to continue");
	}
}
