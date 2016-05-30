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
import java.util.Map;
import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.BlockLocation;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;
import me.simplex.buildr.util.Cuboid;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Attachable;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.Ladder;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;


/**
 *
 * @author pwasson
 */
public class CloneBuilderTask extends Buildr_Runnable_Builder_Super {
    private final Block position3;
    private final int rotation;
    private final boolean canBreakBedrock;


    public CloneBuilderTask(Buildr plugin,
            Player player,
            Block position1,
            Block position2,
            Block position3,
            int inRotation) {
        this.plugin = plugin;
        this.player = player;
        this.position1 = position1;
        this.position2 = position2;
        this.position3 = position3;
        this.rotation = inRotation;
        this.canBreakBedrock = plugin.checkPermission(player, "buildr.feature.break_bedrock");
    }


    @Override
    public void run() {
        int height = Math.abs(position1.getY() - position2.getY()) + 1;
        int width = Math.abs(position1.getX() - position2.getX()) + 1;
        int depth = Math.abs(position1.getZ() - position2.getZ()) + 1;

        Cuboid source = new Cuboid(position1.getLocation(), position2.getLocation());
        /* if we're rotating, we have to adjust the destination coordinates accordingly, i.e. swap
         width and depth if angle is 90 or 270 */
        boolean quarterRot = (rotation % 180 != 0);
        int destWidth = quarterRot ? depth : width;
        int destDepth = quarterRot ? width : depth;
        BlockLocation loc4 = new BlockLocation(position3.getLocation().getBlockX() + destWidth - 1,
                position3.getLocation().getBlockY() + height - 1,
                position3.getLocation().getBlockZ() + destDepth - 1);
        Cuboid dest = new Cuboid(new BlockLocation(position3.getLocation()), loc4);

        Map<Block, Buildr_Container_UndoBlock> undoBlocks = new HashMap<Block, Buildr_Container_UndoBlock>();
        World theWorld = position1.getWorld();
// FIXME doors still drop as items. Don't know why yet.
        /*
        We need to do this in two passes. If we clone an attachable block, such as a torch or ladder,
        before the block it's attached to, it will drop as an item as soon as it's placed. So clone the
        non-attachable blocks in the first pass (to provide surfaces), then only the attachable
        blocks in the second pass.
        */
        cloneLoop(source, dest, theWorld, undoBlocks, false);
        cloneLoop(source, dest, theWorld, undoBlocks, true);

        plugin.getUndoList().addToStack(undoBlocks, player);
        player.sendMessage(String.format("Done! Placed %d blocks", undoBlocks.size()));
        plugin.log(String.format("%s cloned a cuboid: %d blocks affected.", player.getName(),
                undoBlocks.size()));
    }


    private void cloneLoop(Cuboid source,
            Cuboid dest,
            World theWorld,
            Map<Block, Buildr_Container_UndoBlock> undoBlocks,
            boolean copyAttachables) {
        for (int srcX = source.getLowCorner().getX(); srcX <= source.getHighCorner().getX(); ++srcX) {
            for (int srcZ = source.getLowCorner().getZ(); srcZ <= source.getHighCorner().getZ(); ++srcZ) {
                BlockLocation destLoc = calcDest(source, dest, srcX, srcZ);

                int yOffset = dest.getLowCorner().getY() - source.getLowCorner().getY();
                for (int srcY = source.getLowCorner().getY(); srcY <= source.getHighCorner().getY(); ++srcY) {
                    int destY = srcY + yOffset;
                    Block srcBlockHandle = theWorld.getBlockAt(srcX, srcY, srcZ);

                    boolean isAttachable = Attachable.class.isAssignableFrom(srcBlockHandle.getType().getData());
                    if (copyAttachables != isAttachable) continue;
                    
                    /* TODO if we are filtering by material or only cloning non-air blocks, this is where we'd
                    check and skip this iteration if it doesn't pass the filter.
                    */

                    Block destBlockHandle = theWorld.getBlockAt(destLoc.getX(), destY, destLoc.getZ());
                    cloneBlock(srcBlockHandle, destBlockHandle, undoBlocks);
                }
            }
        }
    }


    /**
     * calculates the destination X and Z coordinates based on the source and destination cuboids, the
     * source X, source Z, and rotation angle. (The destination Y is always set to 0.)
     * @param src
     * @param dest
     * @param srcX
     * @param srcZ
     * @return
     */
    private BlockLocation calcDest(Cuboid src, Cuboid dest, int srcX, int srcZ) {
        int offsetX = srcX - src.getLowCorner().getX();
        int offsetZ = srcZ - src.getLowCorner().getZ();
        int destX, destZ;

        switch (rotation) {
            case 90:
            case -270:
                destX = dest.getHighCorner().getX() - offsetZ;
                destZ = dest.getLowCorner().getZ() + offsetX;
                break;
            case 180:
            case -180:
                destX = dest.getHighCorner().getX() - offsetX;
                destZ = dest.getHighCorner().getZ() - offsetZ;
                break;
            case 270:
            case -90:
                destX = dest.getLowCorner().getX() + offsetZ;
                destZ = dest.getHighCorner().getZ() - offsetX;
                break;
            default:
                // assume no rotation
                destX = dest.getLowCorner().getX() + offsetX;
                destZ = dest.getLowCorner().getZ() + offsetZ;
                break;
        }
//plugin.getLogger().info(String.format(
//        "X %d -> %d, Z %d - %d", srcX, destX, srcZ, destZ));
        return new BlockLocation(destX, 0, destZ);
    }

    
    protected void cloneBlock(Block srcBlockHandle,
            Block destBlockHandle,
            Map<Block, Buildr_Container_UndoBlock> undo) {
        if (canBuild(player, destBlockHandle)) {
            if (canBreakBedrock || !destBlockHandle.getType().equals(Material.BEDROCK)) {
                undo.put(destBlockHandle, new Buildr_Container_UndoBlock(destBlockHandle.getType(),
                         destBlockHandle.getData()));
                Material mat = srcBlockHandle.getType();
                byte blockData = srcBlockHandle.getData();
                if (0 != rotation && Directional.class.isAssignableFrom(mat.getData())) {
                    MaterialData newData = mat.getNewData(blockData);
                    Directional newDataAsDirectional = (Directional)newData;
                    BlockFace originalFacing = getActualFacing(newDataAsDirectional);
//plugin.getLogger().info(String.format("%s Before: %s", mat.name(), originalFacing));

                    BlockFace newFacing = getRotatedFacing(originalFacing);

//plugin.getLogger().info(String.format("\tRotated: %s", newFacing));
                    setFacingCorrectly(newData, newDataAsDirectional, newFacing);
//plugin.getLogger().info(String.format("\tSet Data: %s", getActualFacing(newDataAsDirectional)));
                    blockData = newData.getData();
//plugin.getLogger().info(String.format("\tAfter: %s", getActualFacing((Directional)mat.getNewData(destBlockHandle.getData()))));
                }

                destBlockHandle.setTypeIdAndData(mat.getId(), blockData, true);
                BlockState srcState = srcBlockHandle.getState();
                BlockState destState = destBlockHandle.getState();
                boolean stateChanged = false;

                /* copy inventory of items with inventories: Beacon, Brewing Stand, Chest, Dispenser, Dropper,
                Furnace, Hopper
                        */
                if (srcState instanceof InventoryHolder) {
                    ((InventoryHolder)destState).getInventory().
                            setContents(((InventoryHolder)srcState).getInventory().getContents());
                    stateChanged = true;
                }
                if (srcState instanceof CommandBlock) {
                    ((CommandBlock)destState).setName(((CommandBlock)srcState).getName());
                    ((CommandBlock)destState).setCommand(((CommandBlock)srcState).getCommand());
                    stateChanged = true;
                }
                if (srcState instanceof Sign) {
                    for (int i = 0; i <= 3; ++i)
                        ((Sign)destState).setLine(i, ((Sign)srcState).getLine(i));
                    stateChanged = true;
                }
                
                // TODO lots of other specific BlockState subclasses to copy...

                if (stateChanged) destState.update();
            }
        }
    }


    /**
     * Some block types appear to lie about their facing.<table>
     * <tr><td>Stairs</td><td>getFacing() returns opposite of what it&rsquo;s set to.</td></tr>
     * <tr><td>Torch</td><td> Facing and attached face are opposite; getFacing() is accurate.</td></tr>
     * <tr><td>Ladder</td><td> Facing and AttachedFace are the same, but SimpleAttachableMaterialData will
     * flip the result when you call getFacing() so it lies.</td></tr>
     * <tr><td>Button</td><td> same as Torch</td></tr>
     * <tr><td>Lever</td><td> same as Torch?</td></tr>
     * <tr><td>Trapdoor</td><td> same as Torch</td></tr>
     * <tr><td>TripwireHook</td><td> same as Torch</td></tr>
     * </table>
     * Basically, this ask the Block for its facing, and if it is one that lies, the result is flipped.
     * @param handle the block whose facing you want to know.
     * @return the true facing of the block.
     */
    private BlockFace getActualFacing(Directional dirData) {
        BlockFace facing = dirData.getFacing();
        if (Stairs.class.isAssignableFrom(dirData.getClass())
                || Ladder.class.isAssignableFrom(dirData.getClass())) {
            facing = facing.getOppositeFace();
        }
        return facing;
    }


    private BlockFace getRotatedFacing(BlockFace inFacing) {
        BlockFace facing = inFacing;
        int angle = this.rotation;
        while (angle > 0) {
            switch (facing) {
                case NORTH:
                    facing = BlockFace.EAST;
                    break;
                case EAST:
                    facing = BlockFace.SOUTH;
                    break;
                case SOUTH:
                    facing = BlockFace.WEST;
                    break;
                case WEST:
                    facing = BlockFace.NORTH;
                    break;
                case NORTH_EAST:
                    facing = BlockFace.SOUTH_EAST;
                    break;
                case NORTH_WEST:
                    facing = BlockFace.NORTH_EAST;
                    break;
                case SOUTH_EAST:
                    facing = BlockFace.SOUTH_WEST;
                    break;
                case SOUTH_WEST:
                    facing = BlockFace.NORTH_WEST;
                    break;
                case WEST_NORTH_WEST:
                    facing = BlockFace.NORTH_NORTH_EAST;
                    break;
                case NORTH_NORTH_WEST:
                    facing = BlockFace.EAST_NORTH_EAST;
                    break;
                case NORTH_NORTH_EAST:
                    facing = BlockFace.EAST_SOUTH_EAST;
                    break;
                case EAST_NORTH_EAST:
                    facing = BlockFace.SOUTH_SOUTH_EAST;
                    break;
                case EAST_SOUTH_EAST:
                    facing = BlockFace.SOUTH_SOUTH_WEST;
                    break;
                case SOUTH_SOUTH_EAST:
                    facing = BlockFace.WEST_SOUTH_WEST;
                    break;
                case SOUTH_SOUTH_WEST:
                    facing = BlockFace.WEST_NORTH_WEST;
                    break;
                case WEST_SOUTH_WEST:
                    facing = BlockFace.NORTH_NORTH_WEST;
                    break;
            }
            angle -= 90;
        }
        return facing;
    }


    /**
     * attempts to work around any bugs in any Directional block type&rsquo;s
     * {@link Directional#setFacingDirection(org.bukkit.block.BlockFace) setFacingDirection()}.
     * @param data
     * @param dataAsDirectional
     * @param newFacing
     */
    private void setFacingCorrectly(MaterialData data,
            Directional dataAsDirectional,
            BlockFace newFacing) {
        if (Door.class.isAssignableFrom(data.getClass())) {
            int faceVal = (((newFacing == BlockFace.WEST) ? 0x00 : ((newFacing == BlockFace.NORTH) ? 0x01 : ((newFacing == BlockFace.EAST) ? 0x02 : 0x03))));
            int cleanData = data.getData() & 0x03;
            int newData = cleanData | faceVal;
            data.setData((byte)newData);
        } else {
            dataAsDirectional.setFacingDirection(newFacing);
        }
    }
}
