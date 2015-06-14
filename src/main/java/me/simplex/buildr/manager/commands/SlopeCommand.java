/*
 * Copyright 2015 s1mpl3x
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

public class SlopeCommand extends AbstractBuilderCommand {

    public SlopeCommand(Buildr plugin) {
        super(plugin, "slope", "buildr.cmd.slope", 3);
    }


    @Override
    public boolean onCommandSelf(CommandSender sender,
            Command command,
            String label,
            String[] args) {
        MaterialAndData buildMaterial;
        MaterialAndData replaceMaterial = null;
        BlockFace orientationHint = null;
        try {
            buildMaterial = parseMaterialAndData(args[0]);

            if (!buildMaterial.getMaterial().isBlock()) {
                sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                        "invalid building blocktype");
                return true;
            }

            // might have orientation hint but no replace material, so look for that.
            if (args.length == 2 && !args[1].startsWith("r")) {
                orientationHint = parseOrientationHint(args[1]);
                // will fail if arg1 is replace material, and that's OK.
            }

            if (args.length >= 2 && null == orientationHint
                    && args[1].startsWith("r")) {
                replaceMaterial = parseMaterialAndData(args[1].substring(1));
                if (!replaceMaterial.getMaterial().isBlock()) {
                    sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                            "invalid replace blocktype");
                    return true;
                }
            }

            if (3 == args.length) {// optional orientation hint
                if (null != orientationHint) {
                    // already got orientationHint!
                    sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                            "too many arguments");
                    return true;
                } else {
                    orientationHint = parseOrientationHint(args[2]);
                    if (null == orientationHint) {
                        sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                                "Invalid orientation hint");
                        return true;
                    }
                }
            }

            cmd_slope(sender, buildMaterial, replaceMaterial, orientationHint);
            return true;
        } catch (BadFormatException formatX) {
            sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                    formatX.getMessage());
            return true;
        }
    }


    private BlockFace parseOrientationHint(String arg) {
        String orientationParam = arg.toUpperCase();
        if ("NORTH".equals(orientationParam))
            return BlockFace.NORTH;
        else if ("EAST".equals(orientationParam))
            return BlockFace.EAST;
        else if ("SOUTH".equals(orientationParam))
            return BlockFace.SOUTH;
        else if ("WEST".equals(orientationParam))
            return BlockFace.WEST;
        else {
            return null;
        }
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

        plugin.getStartedBuildings().add(
                new SlopeBuilderManager((Player) sender,
                        buildMaterial.getMaterial(),
                        (null == replace_mat) ? null : replace_mat.getMaterial(),
                        plugin,
                        buildMaterial.getDataIDorZero(),
                        orientationHint));

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
