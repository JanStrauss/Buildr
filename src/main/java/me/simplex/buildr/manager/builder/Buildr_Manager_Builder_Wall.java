/*
 * Copyright 2012 s1mpl3x
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
import me.simplex.buildr.util.Buildr_Interface_Building;
import me.simplex.buildr.util.Buildr_Type_Wall;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_Builder_Wall implements Buildr_Interface_Building {
	private Player creater;
	private Block position1,position2;
	private Buildr_Type_Wall type;
	private Material material;
	private Material replace_mat;
	private boolean replace;
	private boolean coordinate1placed = false;
	private Buildr plugin;
	private byte material_data;
	
	public Buildr_Manager_Builder_Wall(Player player, Material material, boolean replace,Material replace_mat, Buildr plugin, byte material_data) {
		this.creater=player;
		this.material = material;
		this.replace = replace;
		this.plugin = plugin;
		this.material_data = material_data;
		this.replace_mat = replace_mat;
	}
	
	public void addCoordinate1(Block position1){
		this.position1 = position1;
		coordinate1placed = true;
	}
	
	@Override
	public boolean checkCoordinates(){
		Buildr_Type_Wall wallType = checkWallType();
		if (wallType==null) {
			return false;
		}
		else {
			this.type = wallType;
			return true;
		}
	}
	
	private Buildr_Type_Wall checkWallType(){
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
	public void startBuild(){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Builder_Wall(position1,position2,type,material,replace,replace_mat,plugin,creater,material_data));
	}
	
	/**
	 * @return the wallcreater
	 */
	@Override
	public Player getBuildingcreater() {
		return creater;
	}
	/**
	 * 
	 * @return is coordinate1placed
	 */
	public boolean isCoordinate1Placed(){
		return coordinate1placed;
	}

	@Override
	public void addCoordinate2(Block position2) {
		this.position2=position2;
	}


	@Override
	public String getBuildingName() {
		return "Wall";
	}
	
	@Override
	public String getCoordinateCheckFailed() {
		return "Atleast one dimension of the blocks must be the same. Wallbuild stopped.";
	}

}
