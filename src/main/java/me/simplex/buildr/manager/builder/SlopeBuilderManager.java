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

import java.util.HashSet;
import java.util.Set;
import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.builder.SlopeBuilderTask;
import me.simplex.buildr.util.Buildr_Interface_Building;
import me.simplex.buildr.util.Buildr_Type_Wall;
import me.simplex.buildr.util.MaterialAndData;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;


/**
 *
 * @author pwasson
 */
public class SlopeBuilderManager implements Buildr_Interface_Building {
	private final Buildr plugin;
	private final Player creator;
	private final MaterialAndData buildMaterial;
	private final MaterialAndData replaceMaterial;
    private final BlockFace orientationHint;

    private Block position1,position2;
	private Buildr_Type_Wall type;
	private boolean coordinate1placed = false;
    private BlockFace decidedOrientation = null;

    public SlopeBuilderManager(Buildr inPlugin,
            Player inPlayer,
            MaterialAndData inBuildMaterial,
            MaterialAndData inReplaceMaterial,
            BlockFace inOrientationHint) {
        this.plugin = inPlugin;
        this.creator = inPlayer;
        this.buildMaterial = inBuildMaterial;
        this.replaceMaterial = inReplaceMaterial;
        this.orientationHint = inOrientationHint;
    }


    @Override
    public void addCoordinate1(Block position1) {
        this.position1 = position1;
        coordinate1placed = true;
    }


    @Override
    public boolean checkCoordinates() {
        // the slope must be a 45 degree angle, i.e. deltaY == deltaX or deltaY == deltaZ.
        // Also, if deltaY == deltaX == deltaZ, orientation is arbitrary, so an orientation hint
        // must have been provided and must be valid for the given coordinates.
        int deltaY = Math.abs(position1.getY() - position2.getY());
        int deltaX = Math.abs(position1.getX() - position2.getX());
        int deltaZ = Math.abs(position1.getZ() - position2.getZ());
// TODO set a reason code when we return false that can be used by getCoordinateCheckFailed()
        if (deltaY == 0) {
            return false;
        }
        
        Block highPos, lowPos;
        if (position1.getY() > position2.getY()) {
            highPos = position1;
            lowPos = position2;
        } else {
            highPos = position2;
            lowPos = position1;
        }

        if (deltaY == deltaX && deltaY == deltaZ) {// deal with arbitrary
            // TODO we could also maybe use player's orientation as a hint if orientationHint is not provided.
            if (null == orientationHint)
                return false;
            Set<BlockFace> options = new HashSet<BlockFace>();
            options.add(BlockFace.NORTH);
            options.add(BlockFace.EAST);
            options.add(BlockFace.SOUTH);
            options.add(BlockFace.WEST);
            if (highPos.getX() < lowPos.getX())
                options.remove(BlockFace.WEST);
            else if (highPos.getX() > lowPos.getX())
                options.remove(BlockFace.EAST);
            if (highPos.getZ() < lowPos.getZ())
                options.remove(BlockFace.NORTH);
            else if (highPos.getZ() > lowPos.getZ())
                options.remove(BlockFace.SOUTH);
            if (options.contains(orientationHint))
                decidedOrientation = orientationHint;
            else
                return false;
        } else if (deltaY == deltaX) {// slope faces EAST or WEST
            decidedOrientation = (highPos.getX() < lowPos.getX()) ? BlockFace.EAST : BlockFace.WEST;
        } else if (deltaY == deltaZ) {
            decidedOrientation = (highPos.getZ() < lowPos.getZ()) ? BlockFace.SOUTH : BlockFace.NORTH;
        } else {
            // not 45 degrees.
            return false;
        }
        return true;
    }


    @Override
    public void startBuild() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                new SlopeBuilderTask(plugin, creator, position1, position2, decidedOrientation, 
                        buildMaterial, replaceMaterial));
    }


	@Override
	public Player getBuildingcreater() {
		return creator;
	}


    @Override
	public boolean isCoordinate1Placed(){
		return coordinate1placed;
	}


	@Override
	public void addCoordinate2(Block position2) {
		this.position2=position2;
	}


	@Override
	public String getBuildingName() {
		return "Slope";
	}


	@Override
	public String getCoordinateCheckFailed() {
		return "The slope must be 45 degrees, and if lengths along X and Z are equal, an orientation hint must be provided. Slope building stopped.";
	}
}
