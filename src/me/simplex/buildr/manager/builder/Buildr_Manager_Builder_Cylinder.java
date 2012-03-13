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
import me.simplex.buildr.runnable.builder.Buildr_Runnable_Builder_Cylinder;
import me.simplex.buildr.util.Buildr_Interface_Building;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_Builder_Cylinder implements Buildr_Interface_Building {
	private Player wallcreater;
	private Block position1,position2;
	private Material material;
	private boolean replace;
	private boolean hollow;
	private boolean coordinate1placed;
	private Buildr plugin;
	private byte material_data;
	private Material replace_mat;

	public Buildr_Manager_Builder_Cylinder(Player wallcreater, Material material,boolean replace,Material replace_mat, boolean hollow, Buildr plugin, byte material_data) {
		this.wallcreater = wallcreater;
		this.material = material;
		this.replace = replace;
		this.hollow = hollow;
		this.plugin = plugin;
		this.material_data = material_data;
		this.replace_mat = replace_mat;
	}

	@Override
	public void addCoordinate1(Block position1) {
		this.position1 = position1;
		this.coordinate1placed = true;
	}

	@Override
	public void addCoordinate2(Block position2) {
		this.position2 = position2;
	}

	@Override
	public boolean checkCoordinates() {
		return true;
	}

	@Override
	public String getBuildingName() {
		return "Cylinder";
	}

	@Override
	public String getCoordinateCheckFailed() {
		return "Herp Derp";
	}

	@Override
	public Player getBuildingcreater() {
		return wallcreater;
	}

	@Override
	public boolean isCoordinate1Placed() {
		return coordinate1placed;
	}

	@Override
	public void startBuild() {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Builder_Cylinder(position1, position2, material, replace,replace_mat, hollow, plugin, wallcreater,material_data));
	}
}
