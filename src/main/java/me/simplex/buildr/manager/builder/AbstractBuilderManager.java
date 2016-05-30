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

import java.util.ArrayList;
import java.util.List;
import me.simplex.buildr.Buildr;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


/**
 * Implements some plumbing common to builder managers so subclasses don&rsquo;t
 * need to be so verbose.
 * @author pwasson
 */
abstract class AbstractBuilderManager implements BuilderManager {
	protected final Buildr plugin;
	protected final Player creator;
    protected final String buildingName;
	protected final Material material;
	protected final byte material_data;
	protected final boolean replace;
	protected final Material replace_mat;
	protected final List<Block> positions = new ArrayList<Block>();
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


    /**
     * get the Block position with the specified index.
     * @param n the index of the position to get (1-based).
     * @return the Block specifying the requested position.
     */
    protected Block getPosition(int n) {
        if (n < 1 || n > positions.size()) {
            throw new IllegalArgumentException("invalid position index");
        } else {
            return positions.get(n-1);
        }
    }

    
    @Override
    public Player getBuildingCreator() {
        return creator;
    }


    @Override
    public boolean gotAllCoordinates() {
		return (positions.size() > 1);
    }


    @Override
    public void addCoordinate(Block position) {
		this.positions.add(position);
    }


	@Override
	public boolean checkCoordinates() {
		return true;
	}


    @Override
    public String getBuildingName() {
        return buildingName;
    }


    @Override
    public String getBuildingMessage() {
        return String.format("Positions OK, building %s...", getBuildingName());
    }

    
    protected String getCoordForChat(Block pos) {
        return String.format("[%s%s%s, %s%s%s, %s%s%s]",
                    ChatColor.BLUE, pos.getX(), ChatColor.WHITE,
                    ChatColor.BLUE, pos.getY(), ChatColor.WHITE,
                    ChatColor.BLUE, pos.getZ(), ChatColor.WHITE);
    }


    @Override
    public String getLastPositionMessage() {
        String s;
        if (positions.isEmpty()) {
            return "invalid";
        } else {
            Block pos = positions.get(positions.size() - 1);
            return String.format("Got positon %d of your %s at %s",
                    positions.size(), getBuildingName(), getCoordForChat(pos));
        }
    }


    /**
     * {@inheritDoc}
     * This base version assumes there are only two required positions and the second is described
     * as &ldquo;block 2&rdquo;.
     */
    @Override
    public String getNextPositionMessage() {
        return "Now right-click on block 2 (again with a stick) to continue";
    }
}
