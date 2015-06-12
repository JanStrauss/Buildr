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
package me.simplex.buildr.runnable.builder;

import java.util.HashMap;
import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;
import static me.simplex.buildr.util.Buildr_Type_Wall.WALL_X;
import static me.simplex.buildr.util.Buildr_Type_Wall.WALL_Y;
import static me.simplex.buildr.util.Buildr_Type_Wall.WALL_Z;
import me.simplex.buildr.util.MaterialAndData;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import static org.omg.IOP.CodecPackage.TypeMismatchHelper.type;


/**
 *
 * @author pwasson
 */
public class SlopeBuilderTask extends Buildr_Runnable_Builder_Super {
    private final BlockFace orientation;
    private final MaterialAndData buildMaterial;
    private final MaterialAndData replaceMaterial;
    private final boolean replace;


    public SlopeBuilderTask(Buildr plugin,
            Player player,
            Block position1,
            Block position2,
            BlockFace orientation,
            MaterialAndData buildMaterial,
            MaterialAndData replaceMaterial) {
        this.plugin = plugin;
        this.player = player;
        this.position1 = position1;
        this.position2 = position2;
        this.orientation = orientation;
        this.buildMaterial = buildMaterial;
        this.replaceMaterial = replaceMaterial;
        this.replace = (null != replaceMaterial);
        this.material = buildMaterial.getMaterial();
        this.material_data = (null != buildMaterial.getData())? buildMaterial.getData().getData() : 0;
        this.replace_mat = (null != replaceMaterial) ? replaceMaterial.getMaterial() : null;
    }

    
    @Override
    public void run() {
        int low = Math.min(position1.getY(), position2.getY());
        int high = Math.max(position1.getY(), position2.getY());
        int west = Math.min(position1.getX(), position2.getX());
        int east = Math.max(position1.getX(), position2.getX());
        int north = Math.min(position1.getZ(), position2.getZ());
        int south = Math.max(position1.getZ(), position2.getZ());

        HashMap<Block, Buildr_Container_UndoBlock> undoBlocks = null;
        switch (orientation) {
            case NORTH:
                undoBlocks = buildSlopeNorthSouth(low, high, west, east, north, 1);
                break;
            case EAST:
                undoBlocks = buildSlopeEastWest(low, high, north, south, east, -1);
                break;
            case SOUTH:
                undoBlocks = buildSlopeNorthSouth(low, high, west, east, south, -1);
                break;
            case WEST:
                undoBlocks = buildSlopeEastWest(low, high, north, south, west, 1);
                break;
            default:
                break;
        }
        if (null != undoBlocks) {
            plugin.getUndoList().addToStack(undoBlocks, player);
            player.sendMessage(String.format("done! Placed %d blocks", undoBlocks.size()));
            plugin.log(String.format("%s built a slope: %d blocks asffected.", player.getName(), 
                    undoBlocks.size()));
        }
    }

    
    private HashMap<Block, Buildr_Container_UndoBlock> buildSlopeNorthSouth(int low,
            int high,
            int west,
            int east,
            int startZ,
            int deltaZ) {
		HashMap<Block, Buildr_Container_UndoBlock> undo = 
                new HashMap<Block, Buildr_Container_UndoBlock>();

        int z = startZ;
        for (int y = low; y <= high; ++y) {
            for (int x = west; x <= east; ++x) {
				Block block_handle = position1.getWorld().getBlockAt(x, y, z);

				if (!replace || block_handle.getType().equals(replace_mat)) {
                    changeBlock(block_handle, undo);
				}
            }
            z += deltaZ;
        }

        return undo;
    }


    private HashMap<Block, Buildr_Container_UndoBlock> buildSlopeEastWest(int low,
            int high,
            int north,
            int south,
            int startX,
            int deltaX) {
		HashMap<Block, Buildr_Container_UndoBlock> undo =
                new HashMap<Block, Buildr_Container_UndoBlock>();

        int x = startX;
        for (int y = low; y <= high; ++y) {
            for (int z = north; z <= south; ++z) {
				Block block_handle = position1.getWorld().getBlockAt(x, y, z);

				if (!replace || block_handle.getType().equals(replace_mat)) {
                    changeBlock(block_handle, undo);
				}
            }
            x += deltaX;
        }

        return undo;
    }
}
