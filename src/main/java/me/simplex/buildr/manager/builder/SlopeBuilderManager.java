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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;


/**
 *
 * @author pwasson
 */
public class SlopeBuilderManager extends AbstractBuilderManager {
    private static final String COORD_FAIL_MESSAGE_GENERIC = "The slope must be 45 degrees, and if lengths along X and Z are equal, an orientation hint must be provided. Slope building stopped.";
    private static final String COORD_FAIL_MESSAGE_NO_HEIGHT = "A slope must be more than one block high. Slope building stopped.";
    private static final String COORD_FAIL_MESSAGE_HINT_REQUIRED = "The orientation is arbitrary; an orientation hint (north, east, south, west) must be provided. Slope building stopped.";
    private static final String COORD_FAIL_MESSAGE_BAD_HINT = "The orientation hint provided is not valid for the chosen positions. Slope building stopped.";
    private static final String COORD_FAIL_MESSAGE_ANGLE = "The slope must be 45 degrees, i.e. the width OR depth must equal the height. Slope building stopped.";

    private final BlockFace orientationHint;

    private BlockFace decidedOrientation = null;
    private CoordFail coordReason = null;

    public SlopeBuilderManager(
            Player inPlayer,
            Material inBuildMaterial,
            Material inReplaceMaterial,
            Buildr inPlugin,
            byte inBuildMaterialData,
            BlockFace inOrientationHint) {
        super("Slope", inPlugin, inPlayer, inBuildMaterial, inBuildMaterialData, inReplaceMaterial);
        this.orientationHint = inOrientationHint;
    }


    private enum CoordFail {
        NO_HEIGHT,
        HINT_REQUIRED,
        BAD_HINT,
        ANGLE
    }

    @Override
    public boolean checkCoordinates() {
        // the slope must be a 45 degree angle, i.e. deltaY == deltaX or deltaY == deltaZ.
        // Also, if deltaY == deltaX == deltaZ, orientation is arbitrary, so an orientation hint
        // must have been provided and must be valid for the given coordinates.
        Block position1 = getPosition(1);
        Block position2 = getPosition(2);
        int deltaY = Math.abs(position1.getY() - position2.getY());
        int deltaX = Math.abs(position1.getX() - position2.getX());
        int deltaZ = Math.abs(position1.getZ() - position2.getZ());

        if (deltaY == 0) {
            coordReason = CoordFail.NO_HEIGHT;
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
            if (null == orientationHint) {
                coordReason = CoordFail.HINT_REQUIRED;
                return false;
            }
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
            else {
                coordReason = CoordFail.BAD_HINT;
                return false;
            }
        } else if (deltaY == deltaX) {// slope faces EAST or WEST
            decidedOrientation = (highPos.getX() < lowPos.getX()) ? BlockFace.EAST : BlockFace.WEST;
        } else if (deltaY == deltaZ) {
            decidedOrientation = (highPos.getZ() < lowPos.getZ()) ? BlockFace.SOUTH : BlockFace.NORTH;
        } else {
            // not 45 degrees.
            coordReason = CoordFail.ANGLE;
            return false;
        }
        return true;
    }


    @Override
    public String getCoordinateCheckFailed() {
        if (coordReason != null)
            switch (coordReason) {
                case NO_HEIGHT:
                    return COORD_FAIL_MESSAGE_NO_HEIGHT;
                case HINT_REQUIRED:
                    return COORD_FAIL_MESSAGE_HINT_REQUIRED;
                case BAD_HINT:
                    return COORD_FAIL_MESSAGE_BAD_HINT;
                case ANGLE:
                    return COORD_FAIL_MESSAGE_ANGLE;
            }
        return COORD_FAIL_MESSAGE_GENERIC;
    }


    @Override
    public void startBuild() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                new SlopeBuilderTask(plugin, creator, getPosition(1), getPosition(2), decidedOrientation,
                        material, material_data, replace_mat));
    }
}
