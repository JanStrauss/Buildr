/*
 * Copyright 2015 pwasson
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
import me.simplex.buildr.manager.builder.SlopeBuilderManager;
import static me.simplex.buildr.manager.commands.Buildr_Manager_Command_Super.sendTo;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SlopeCommand extends Buildr_Manager_Command_Super {

    public SlopeCommand(Buildr plugin) {
        super(plugin);
    }


    @Override
    public boolean onCommand(CommandSender sender,
            Command command,
            String label,
            String[] args) {
//TODO support orientation hint without replace material
        if (command.getName().equalsIgnoreCase("slope")) {
            if (args.length < 1 || args.length > 3) {
                return false;
            }
            if (plugin.checkPermission((Player) sender, "buildr.cmd.slope")) {
                MaterialAndData buildMaterial;
                MaterialAndData replaceMaterial = null;
                BlockFace orientationHint = null;
                try {// TODO debug parseMaterialAndData().
                    buildMaterial = parseMaterialAndData(args[0]);

                    if (!buildMaterial.getMaterial().isBlock()) {
                        sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                                "invalid building blocktype");
                        return true;
                    }
                    if (args.length >= 2) {
                        replaceMaterial = parseMaterialAndData(args[1]);
                        if (!replaceMaterial.getMaterial().isBlock()) {
                            sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                                    "invalid replace blocktype");
                            return true;
                        }
                    }
                } catch (BadFormatException formatX) {
                    sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                            formatX.getMessage());
                    return true;
                }

                if (3 == args.length) {// optional orientation hint
                    String orientationParam = args[2].toUpperCase();
                    if ("NORTH".equals(orientationParam))
                        orientationHint = BlockFace.NORTH;
                    else if ("EAST".equals(orientationParam))
                        orientationHint = BlockFace.EAST;
                    else if ("SOUTH".equals(orientationParam))
                        orientationHint = BlockFace.SOUTH;
                    else if ("WEST".equals(orientationParam))
                        orientationHint = BlockFace.WEST;
                    else {
                        sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                                "Invalid orientation hint");
                        return true;
                    }
                }

                cmd_slope(sender, buildMaterial, replaceMaterial, orientationHint);
                return true;
            } else {
                sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                        "You do not have permission to perform this action");
            }
            return true;
        }
        return false;
    }


    public void cmd_slope(CommandSender sender,
            MaterialAndData buildMaterial,
            MaterialAndData replace_mat,
            BlockFace orientationHint) {
        if (!plugin.getConfigValue("FEATURE_BUILDER_SLOPE")) {
            sendTo(sender, Buildr_Manager_Command_Super.MsgType.INFO, "feature not enabled");
            return;
        }
        if (plugin.checkPlayerHasStartedBuilding((Player) sender)) {
            plugin.removeStartedBuilding((Player) sender);
            sendTo(sender, Buildr_Manager_Command_Super.MsgType.WARNING, "previous started building aborted");
        }

        plugin.getStartedBuildings().add(new SlopeBuilderManager(plugin, (Player) sender, buildMaterial,
                replace_mat, orientationHint));

        StringBuilder sb = new StringBuilder("Started new Slope.");
        sb.append(String.format(" Info: Blocktype:%s%s%s", ChatColor.BLUE, buildMaterial.toString(),
                ChatColor.WHITE));
        sb.append(String.format(" (ID:%s%s%s)", ChatColor.BLUE, buildMaterial.getMaterial().getId(),
                ChatColor.WHITE));
        if (null != replace_mat) {
            sb.append(String.format(" Replace: Blocktype:%s%s%s", ChatColor.BLUE, replace_mat.toString(),
                    ChatColor.WHITE));
            sb.append(String.format(" (ID:%s%s%s)", ChatColor.BLUE, replace_mat.getMaterial().getId(),
                    ChatColor.WHITE));
        }
        if (null != orientationHint) {
            sb.append(String.format(" Facing hint:%s", orientationHint.toString()));
        }
        String buildinfo = sb.toString();

        sendTo(sender, Buildr_Manager_Command_Super.MsgType.INFO, buildinfo);
        sendTo(sender, Buildr_Manager_Command_Super.MsgType.INFO,
                "Rightclick on block 1 while holding a stick to begin");
    }
}
