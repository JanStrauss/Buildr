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

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.builder.Buildr_Runnable_Builder_Sphere;

public class SphereBuilderManager extends AbstractBuilderManager {
	private final boolean hollow;
	private final boolean halfcube;


    public SphereBuilderManager(
            Player inPlayer,
            Material inBuildMaterial,
            Material inReplaceMaterial,
            boolean hollow,
            boolean halfcube,
            Buildr inPlugin,
            byte inBuildMaterialData) {
        super("Sphere", inPlugin, inPlayer, inBuildMaterial, inBuildMaterialData, inReplaceMaterial);
        this.hollow = hollow;
        this.halfcube = halfcube;
    }


    @Override
    public String getCoordinateCheckFailed() {
        return "Herp Derp";
    }


    @Override
    public void startBuild() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Builder_Sphere(
                getPosition(1), getPosition(2), material, replace, replace_mat, hollow, halfcube, plugin, creator,
                material_data));
    }
}
