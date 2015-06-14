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
package me.simplex.buildr.manager.builder;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.builder.Buildr_Runnable_Builder_Cylinder;
import me.simplex.buildr.util.Buildr_Interface_Building;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_Builder_Cylinder extends AbstractBuilderManager {
	private final boolean hollow;

    public Buildr_Manager_Builder_Cylinder(
            Player inPlayer,
            Material inBuildMaterial,
            Material inReplaceMaterial,
            boolean hollow,
            Buildr inPlugin,
            byte inBuildMaterialData) {
        super("Cylinder", inPlugin, inPlayer, inBuildMaterial, inBuildMaterialData, inReplaceMaterial);
        this.hollow = hollow;
    }


	@Override
	public String getCoordinateCheckFailed() {
		return "Herp Derp";
	}

	   @Override
    public void startBuild() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                new Buildr_Runnable_Builder_Cylinder(position1, position2, material, replace, replace_mat,
                        hollow, plugin, creator, material_data));
    }
}
