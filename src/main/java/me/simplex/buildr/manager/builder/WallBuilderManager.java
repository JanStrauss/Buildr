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
import me.simplex.buildr.runnable.builder.Buildr_Runnable_Builder_Wall;
import me.simplex.buildr.util.Buildr_Type_Wall;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WallBuilderManager extends AbstractBuilderManager {
	private Buildr_Type_Wall type;
	
    public WallBuilderManager(
            Player inPlayer,
            Material inBuildMaterial,
            Material inReplaceMaterial,
            Buildr inPlugin,
            byte inBuildMaterialData) {
        super("Wall", inPlugin, inPlayer, inBuildMaterial, inBuildMaterialData, inReplaceMaterial);
    }


	@Override
	public boolean checkCoordinates(){
		Buildr_Type_Wall wallType = checkWallType();
		if (wallType==null) {
			return false;
		} else {
			this.type = wallType;
			return true;
		}
	}


	private Buildr_Type_Wall checkWallType() {
        Block position1 = getPosition(1);
        Block position2 = getPosition(2);

        if (position1.getX() == position2.getX()) {
			return Buildr_Type_Wall.WALL_X;
		}
		if (position1.getY() == position2.getY()) {
			return Buildr_Type_Wall.WALL_Y;
		}
		if (position1.getZ() == position2.getZ()) {
			return Buildr_Type_Wall.WALL_Z;
		}
		else {
			return null;
		}
	}


	@Override
	public String getCoordinateCheckFailed() {
		return "Atleast one dimension of the blocks must be the same. Wallbuild stopped.";
	}


	@Override
	public void startBuild(){
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Builder_Wall(
                getPosition(1), getPosition(2), type, material, replace, replace_mat, plugin, creator, material_data));
	}
}
