/*
 * Copyright 2012 s1mpl3x
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
package me.simplex.buildr.util;

import org.bukkit.Location;


/**
 * Represents a 3-dimensional volume specified by two opposing corners, each represented as a
 * {@link org.bukkit.Location}. Coordinates are always interpreted as &ldquo;block&rdquo; coordinates; no
 * fractional blocks are supported. The coordinates are internally kept normalized, i.e. one corner
 * has the lowest coordinates and other has the highest coordinates. The volume <i>includes</i> both corners.
 * @author pwasson
 */
public class Cuboid {
    private BlockLocation lowCorner;
    private BlockLocation highCorner;


    public Cuboid(BlockLocation cornerOne,
            BlockLocation cornerTwo) {
        this.lowCorner = cornerOne;
        this.highCorner = cornerTwo;
        normalize();
    }


    public Cuboid(Location cornerOne,
            Location cornerTwo) {
        this(new BlockLocation(cornerOne),
                new BlockLocation(cornerTwo));
    }


    private void normalize() {
        int low = Math.min(lowCorner.getY(), highCorner.getY());
        int high = Math.max(lowCorner.getY(), highCorner.getY());
        int west = Math.min(lowCorner.getX(), highCorner.getX());
        int east = Math.max(lowCorner.getX(), highCorner.getX());
        int north = Math.min(lowCorner.getZ(), highCorner.getZ());
        int south = Math.max(lowCorner.getZ(), highCorner.getZ());

        lowCorner = new BlockLocation(west, low, north);
        highCorner = new BlockLocation(east, high, south);
    }


    public BlockLocation getLowCorner() {
        return lowCorner;
    }


    public BlockLocation getHighCorner() {
        return highCorner;
    }


    public long getVolume() {
        long width = Math.abs(highCorner.getX() - lowCorner.getX()) + 1;
        long height = Math.abs(highCorner.getY() - lowCorner.getY()) + 1;
        long depth = Math.abs(highCorner.getZ() - lowCorner.getZ()) + 1;
        return height * width * depth;
    }


    public boolean intersects(final Cuboid other) {
        // the volumes must overlap in all three dimensions to really intersect.
        return (overlaps(lowCorner.getX(), highCorner.getX(), other.lowCorner.getX(), other.highCorner.getX())
                && overlaps(lowCorner.getY(), highCorner.getY(), other.lowCorner.getY(), other.highCorner.getY())
                && overlaps(lowCorner.getZ(), highCorner.getZ(), other.lowCorner.getZ(), other.highCorner.getZ()));
    }


    private boolean overlaps(int range1Low, int range1High, int range2Low, int range2High) {
        return !(range1Low > range2High || range2Low > range1High);
    }
}
