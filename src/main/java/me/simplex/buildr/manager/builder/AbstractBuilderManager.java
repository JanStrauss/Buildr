/*
 * Copyright 2015 s1mpl3x
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
import me.simplex.buildr.util.Buildr_Interface_Building;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


/**
 * Implements some plumbing common to builder managers so subclasses don&rsquo;t
 * need to be so verbose.
 * @author pwasson
 */
abstract class AbstractBuilderManager implements Buildr_Interface_Building {
	protected final Buildr plugin;
	protected final Player creator;
    protected final String buildingName;
	protected final Material material;
	protected final byte material_data;
	protected final boolean replace;
	protected final Material replace_mat;
	protected Block position1,position2;
	protected boolean coordinate1placed = false;

    public AbstractBuilderManager(String inBuildingName,
            Buildr inPlugin,
            Player inPlayer,
            Material inBuildMaterial,
            byte inBuildMaterialData,
            Material inReplaceMaterial) {
        this.buildingName = inBuildingName;
        this.creator = inPlayer;
        this.plugin = inPlugin;
        this.material = inBuildMaterial;
        this.material_data = inBuildMaterialData;
        this.replace = (null != inReplaceMaterial);
        this.replace_mat = inReplaceMaterial;
    }

    
    @Override
    public Player getBuildingcreater() {
        return creator;
    }


    @Override
    public boolean isCoordinate1Placed() {
		return coordinate1placed;
    }


    @Override
    public void addCoordinate1(Block position1) {
		this.position1 = position1;
		coordinate1placed = true;
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
        return buildingName;
    }
}
