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
import me.simplex.buildr.runnable.builder.CloneBuilderTask;
import me.simplex.buildr.util.BlockLocation;
import me.simplex.buildr.util.Cuboid;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;


/**
 *
 * @author pwasson
 */
public class CloneBuilderManager extends AbstractBuilderManager {
    private static final String COORD_FAIL_MESSAGE_TOO_BIG = "The volume of the area to be cloned must be no more than 32768 blocks. Cloning stopped.";
    private static final String COORD_FAIL_MESSAGE_OVERLAP = "The source and destination areas overlap, which would cause indeterminate results. Cloning stopped.";

    private final int rotationAngle;

    private CoordFail coordReason = null;

    public CloneBuilderManager(
            Player inPlayer,
            Buildr inPlugin,
            int inRotationAngle) {
        super("Clone", inPlugin, inPlayer, null, (byte)0, null);
        this.rotationAngle = inRotationAngle;
    }

/*
    player.getEyeLocation().getYaw() returns a float that specifies which way the player is looking
    (on the horizontal plane, not up or down, which is getPitch()). It would be better if we extended
    the destination from the tapped corner toward the direction the player is looking when they tap,
    instead of always extending south-east from the tapped block.
    */
    @Override
    public String getNextPositionMessage() {
        String s;
        switch (positions.size()) {
            case 0:
                s = "Right-click with a stick on the first corner of the cuboid to clone.";
                break;
            case 1:
                s = "Now right-click (again with a stick) on the second corner of the cuboid to clone.";
                break;
            default:
                s = "Now right-click (again with a stick) on the block that will be the lowest north-west corner of the clone.";
        }
        return s;
    }


    @Override
    public String getLastPositionMessage() {
        String s;
        switch (positions.size()) {
            case 0:
                s = "";
                break;
            case 1:
                s = String.format("Got the first corner of your clone source at %s.", 
                        getCoordForChat(positions.get(0)));
                break;
            case 2:
                s = String.format("Got the second corner of your clone source at %s.", 
                        getCoordForChat(positions.get(1)));
                break;
            default:
                s = String.format("Got the corner of your clone destination at %s.", 
                        getCoordForChat(positions.get(2)));
        }
        return s;
    }


    @Override
    public boolean gotAllCoordinates() {
		return (positions.size() >= 3);
    }


    private enum CoordFail {
        TOO_BIG,
        OVERLAP
    }

    @Override
    public boolean checkCoordinates() {
        Block position1;
        Block position2;
        Block position3;

        // if we have the source coordinates but the cuboid volume is bigger than 32K blocks, that's a fail.
        if (positions.size() >= 2) {
            position1 = getPosition(1);
            position2 = getPosition(2);
            int height = Math.abs(position1.getY() - position2.getY()) + 1;
            int width = Math.abs(position1.getX() - position2.getX()) + 1;
            int depth = Math.abs(position1.getZ() - position2.getZ()) + 1;
            long volume = (long)height * (long)width * (long)depth;
            if (volume > 32768) {
                coordReason = CoordFail.TOO_BIG;
                return false;
            }

            /* if we have the destination and it overlaps with the source, that's a fail. (We don't support
                    the built-in command's "force" option. */
            if (positions.size() >= 3) {
                Cuboid source = new Cuboid(position1.getLocation(), position2.getLocation());
                position3 = getPosition(3);
                /* if we're rotating, we have to adjust the destination coordinates accordingly, i.e. swap
                        width and depth if angle is 90 or 270 */
                boolean quarterRot = (rotationAngle == 90 || rotationAngle == 270);
                int destWidth = quarterRot ? depth : width;
                int destDepth = quarterRot ? width : depth;
                BlockLocation loc4 = new BlockLocation(position3.getLocation().getBlockX() + destWidth - 1,
                        position3.getLocation().getBlockY() + height - 1,
                        position3.getLocation().getBlockZ() + destDepth - 1);
                Cuboid dest = new Cuboid(new BlockLocation(position3.getLocation()), loc4);
                if (source.intersects(dest)) {
                    coordReason = CoordFail.OVERLAP;
                    return false;
                }
                // FIXME remove debugging log messages
//plugin.getLogger().info(String.format("Source: [%d, %d, %d] - [%d, %d, %d]",
//        source.getLowCorner().getX(), source.getLowCorner().getY(), source.getLowCorner().getZ(),
//        source.getHighCorner().getX(), source.getHighCorner().getY(), source.getHighCorner().getZ()));
//plugin.getLogger().info(String.format("Dest: [%d, %d, %d] - [%d, %d, %d]",
//        dest.getLowCorner().getX(), dest.getLowCorner().getY(), dest.getLowCorner().getZ(),
//        dest.getHighCorner().getX(), dest.getHighCorner().getY(), dest.getHighCorner().getZ()));
            }
        }

        return true;
    }


    @Override
    public String getCoordinateCheckFailed() {
        if (coordReason != null)
            switch (coordReason) {
                case TOO_BIG:
                    return COORD_FAIL_MESSAGE_TOO_BIG;
                case OVERLAP:
                    return COORD_FAIL_MESSAGE_OVERLAP;
            }
        return "<internal error>";
    }


    @Override
    public void startBuild() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                new CloneBuilderTask(plugin, creator, getPosition(1), getPosition(2), getPosition(3),
                        rotationAngle));
    }
}
