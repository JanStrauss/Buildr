/*
 * Copyright 2012 s1mpl3x
 * Copyright 2015 pdwasson
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
import me.simplex.buildr.manager.builder.SphereBuilderManager;
import me.simplex.buildr.util.Buildr_Type_Wool;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Sphere extends StdArgBuilderCommand {

	public Buildr_Manager_Command_Sphere(Buildr plugin) {
		super(plugin, "sphere", "buildr.cmd.sphere");
	}


    @Override
    public void doBuildCommand(CommandSender sender,
            Material material,
            byte material_data,
            boolean hollow,
            Material replace_mat) {
        if (!plugin.getConfigValue("FEATURE_BUILDER_SPHERE")) {
            return;
        }
        if (plugin.checkPlayerHasStartedBuilding((Player) sender)) {
            plugin.removeStartedBuilding((Player) sender);
            sendTo(sender, MsgType.WARNING, "previous started building aborted");
        }
        String replace_info = "";
        if (null != replace_mat) {
            replace_info = "Replace: " + ChatColor.BLUE + replace_mat;
        }
        plugin.getStartedBuildings().add(new SphereBuilderManager((Player) sender, material,
                replace_mat, hollow, false, plugin, material_data));
        String buildinfo = "Started new Sphere. Info: Blocktype: " + ChatColor.BLUE + material.toString() + ChatColor.WHITE + " (ID:" + ChatColor.BLUE + material.
                getId() + ChatColor.WHITE + ") " + replace_info + ChatColor.WHITE + " Hollow: " + ChatColor.BLUE + hollow;

        sendTo(sender, MsgType.INFO, buildinfo);
        sendTo(sender, MsgType.INFO,
                "Rightclick on the center of your sphere while holding a stick to continue");
    }
}
