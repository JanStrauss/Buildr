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
import me.simplex.buildr.runnable.builder.Buildr_Runnable_Builder_Wallx;
import me.simplex.buildr.util.Buildr_Interface_Building;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_Builder_Wallx implements Buildr_Interface_Building {
	private Player wallcreater;
	private Block position1,position2;
	private Material material, replace_mat;
	private boolean replace;
	private boolean coordinate1placed = false;
	private Buildr plugin;
	private byte material_data;
	
	public Buildr_Manager_Builder_Wallx(Player player, Material material, boolean replace,Material replace_mat, Buildr plugin, byte material_data) {
		this.wallcreater=player;
		this.material = material;
		this.replace = replace;
		this.replace_mat = replace_mat;
		this.plugin = plugin;
		this.material_data = material_data;
	}
	
	public void addCoordinate1(Block position1){
		this.position1 = position1;
		coordinate1placed = true;
	}
	
	public void addCoordinate2(Block position2){
		this.position2 = position2;
	}
	
	@Override
	public void startBuild(){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Builder_Wallx(position1,position2,material,replace,replace_mat,plugin,wallcreater,material_data));
	}
	
	/**
	 * @return the wallcreater
	 */
	public Player getBuildingcreater() {
		return wallcreater;
	}
	/**
	 * 
	 * @return is coordinate1placed
	 */
	public boolean isCoordinate1Placed(){
		return coordinate1placed;
	}

	@Override
	public boolean checkCoordinates() {
		return true;
	}

	@Override
	public String getBuildingName() {
		return "Wallx";
	}
	
	@Override
	public String getCoordinateCheckFailed() {
		return null;
	}
}
