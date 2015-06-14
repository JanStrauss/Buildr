/*
 * Original work Copyright 2012 s1mpl3x
 * Modified work Copyright 2015 pdwasson
 * 
 * This file is part of Buildr.
 * 
 * Buildr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Buildr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Buildr  If not, see <http://www.gnu.org/licenses/>.
 */
package me.simplex.buildr.manager.commands;

import me.simplex.buildr.util.MaterialAndData;
import me.simplex.buildr.Buildr;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

public abstract class Buildr_Manager_Command_Super implements CommandExecutor {
	protected Buildr plugin;
	
	public Buildr_Manager_Command_Super(Buildr plugin) {
		this.plugin = plugin;
	}
	
	protected boolean checkSenderIsPlayer(CommandSender s){
		return (s instanceof Player); //disable console 
	}
	
	public enum MsgType {INFO, WARNING, ERROR};
	
	public static void sendTo(Player player, MsgType type, String msg){
		player.sendMessage(BuildMsg(type, msg));
	}
	
	public static void sendTo(CommandSender sender, MsgType type, String msg){
		sender.sendMessage(BuildMsg(type, msg));
	}
	
	private static String BuildMsg(MsgType type, String msg){
		String PREFIX = "[Buildr] ";
		ChatColor MsgColor = ChatColor.WHITE;

		switch (type) {
			case INFO: 	  MsgColor = ChatColor.WHITE;  break;
			case WARNING: MsgColor = ChatColor.YELLOW; break;
			case ERROR:   MsgColor = ChatColor.RED;    break;
			default: break;
		}
		
		return ChatColor.BLUE + PREFIX + MsgColor + msg;
	}


	protected Material parseMaterial(String mat_re){
		int id = -1;
		try {
			id = Integer.parseInt(mat_re);
		} 
		catch (NumberFormatException e) {
			try {
				id = Material.matchMaterial(mat_re).getId();
			} 
			catch (NullPointerException e2) {
			}
		}
		if (id != -1 && Material.getMaterial(id).isBlock()) {
			return Material.getMaterial(id);
		}
		else {
			return null;
		}
	}


    protected MaterialAndData parseMaterialAndData(String param) throws BadFormatException {
        Material material;
        MaterialData data = null;

        if (param.toUpperCase().startsWith("WOOL:")) {
            material = Material.WOOL;
            try {
                org.bukkit.DyeColor woolcolor = Enum.valueOf(org.bukkit.DyeColor.class, param.toUpperCase().
                        substring(5));
                org.bukkit.material.Wool woolData = new org.bukkit.material.Wool();
                woolData.setColor(woolcolor);
                data = woolData;
            } catch (IllegalArgumentException e) {
                throw new BadMaterialException("Invalid wool color");
            }
        } else {
            String blockIDParam = param;
            String dataIDParam = null;
            if (param.contains(":")) {
                String[] parts = param.split(":");
                blockIDParam = parts[0];
                if (parts.length == 2)
                    dataIDParam = parts[1];
                else if (parts.length > 2) {
                    throw new BadMaterialException("Bad material format");
                }
            }
            material = Material.matchMaterial(blockIDParam);// tries by both int id and name
            if (null == material) {
                throw new BadMaterialException("Unknown material name or ID");
            }
            if (null != dataIDParam && !dataIDParam.isEmpty()) {
                try {
                    data = material.getNewData(Byte.parseByte(dataIDParam));
                    // TODO ideally validate mat_data is a valid data value for the specified block type.
                } catch (NumberFormatException e) {
                    // TODO ideally try to match by name since we know the block ID.
                    throw new BadMaterialException("Invalid subtype data ID");
                }
            }
        }

        return new MaterialAndData(material, data);
    }
    
    
    protected static class CommonArguments {
        private final MaterialAndData buildMaterial;
        private final MaterialAndData replaceMaterial;
        private final boolean hollow;

        public CommonArguments(MaterialAndData inBuildMaterial,
                MaterialAndData inReplaceMaterial) {
            this(inBuildMaterial, inReplaceMaterial, false);
        }

        public CommonArguments(MaterialAndData inBuildMaterial,
                MaterialAndData inReplaceMaterial,
                boolean inHollow) {
            this.buildMaterial = inBuildMaterial;
            this.replaceMaterial = inReplaceMaterial;
            this.hollow = inHollow;
        }

        public MaterialAndData getBuildMaterial() {
            return buildMaterial;
        }

        public MaterialAndData getReplaceMaterial() {
            return replaceMaterial;
        }

        public boolean isHollow() {
            return hollow;
        }
    }
    
    
    protected CommonArguments parseCommonArguments(String[] args,
            boolean supportHollow) throws BadFormatException {
        MaterialAndData buildMaterial;
        MaterialAndData replaceMaterial = null;
        boolean hollow = false;

        buildMaterial = parseMaterialAndData(args[0]);
        if (!buildMaterial.getMaterial().isBlock())
            throw new BadMaterialException("invalid building blocktype");

        if (args.length == 2) {
            if (args[1].startsWith("r")) {
                replaceMaterial = parseMaterialAndData(args[1].substring(1));
            } else if (supportHollow && args[1].equalsIgnoreCase("h") || args[1].equalsIgnoreCase("hollow")) {
                hollow = true;
            } else {
                throw new BadFormatException("Invalid argument: " + args[1]);
            }
        } else if (supportHollow && args.length == 3) {
            if (args[1].startsWith("r"))
                replaceMaterial = parseMaterialAndData(args[1].substring(1));
            else
                throw new BadFormatException("Invalid argument: " + args[1]);
            if (args[2].equalsIgnoreCase("h") || args[2].equalsIgnoreCase("hollow"))
                hollow = true;
            else
                throw new BadFormatException("Too many arguments");
        } else if (args.length > 3) {
            throw new BadFormatException("Wrong number of arguments");
        }

        if (null != replaceMaterial && !replaceMaterial.getMaterial().isBlock())
            throw new BadMaterialException("invalid replace blocktype");

        return new CommonArguments(buildMaterial, replaceMaterial, hollow);
    }
}