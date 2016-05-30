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

import me.simplex.buildr.Buildr;
import me.simplex.buildr.manager.builder.CloneBuilderManager;
import static me.simplex.buildr.manager.commands.Buildr_Manager_Command_Super.sendTo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CloneCommand extends AbstractBuilderCommand {
    public CloneCommand(Buildr plugin) {
        super(plugin, "bclone", "buildr.cmd.clone", 1, 0);
    }
/* TODO support options from built-in command:
    "masked" (do not copy air blocks)
    ? "filtered" (with material, only copy blocks of specified material)
    ? "move" (allow overlap of source and dest; blocks in source that are not occupied by dest afterward,
        but were copied, are replaced by air)
*/
    @Override
    public boolean onCommandSelf(CommandSender sender,
            Command command,
            String label,
            String[] args) {
        try {
            // the only argument we have is an optional rotation
            int rotationAngle = 0;

            if (args.length > 0) {
                String arg = args[0];
                if (null != arg) {
                    if (org.bukkit.util.StringUtil.startsWithIgnoreCase(arg, "R")) {
                        String angleString = arg.substring(1);
                        try {
                            rotationAngle = Integer.parseInt(angleString);
                            if ((rotationAngle % 90) != 0) {
                                sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                                        "invalid rotation angle; must be a multiple of 90.");
                                return true;
                            }
                            // normalize the rotation angle to > 0, < 360.
                            rotationAngle = rotationAngle % 360;
                            while (rotationAngle < 0)
                                rotationAngle += 360;
                        } catch (NumberFormatException numX) {
                            sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                                    "invalid rotation angle; must be a multiple of 90.");
                            return true;
                        }
                    } else {
                        sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                                "invalid argument; r<angle> expected, e.g. \"r90\".");
                        return true;
                    }
                }
            }

            cmd_clone(sender, rotationAngle);
            return true;
        } catch (BadFormatException formatX) {
            sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                    formatX.getMessage());
            return true;
        }
    }


    public void cmd_clone(CommandSender sender,
            int rotationAngle) {
        if (!plugin.getConfigValue("FEATURE_BUILDER_CLONE")) {
            sendTo(sender, Buildr_Manager_Command_Super.MsgType.INFO, "feature not enabled");
            return;
        }
        if (plugin.checkPlayerHasStartedBuilding((Player) sender)) {
            plugin.removeStartedBuilding((Player) sender);
            sendTo(sender, Buildr_Manager_Command_Super.MsgType.WARNING, "previous started building aborted");
        }

        plugin.getStartedBuildings().add(
                new CloneBuilderManager((Player) sender, plugin, rotationAngle));

        StringBuilder sb = new StringBuilder("Started new Clone");
        if (rotationAngle != 0) {
            sb.append(String.format(", rotating %d degrees", rotationAngle));
        }
        sb.append(".");
        String buildinfo = sb.toString();

        sendTo(sender, Buildr_Manager_Command_Super.MsgType.INFO, buildinfo);
        sendTo(sender, Buildr_Manager_Command_Super.MsgType.INFO,
                "Right-click on one corner of the cuboid to clone while holding a stick to begin");
    }
}
