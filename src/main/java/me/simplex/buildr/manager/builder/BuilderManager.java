/*
 * Copyright 2012-2015 s1mpl3x
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

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * A BuilderManager is responsible for accepting user input events from the plugin, checking for valid
 * coordinates, and spawning the building task once all required coordinates have been added.
 * @author s1mpl3x
 */
public interface BuilderManager {
    /**
     * @return the Player that started this build.
     */
	public Player getBuildingCreator();

    /**
     * Actually begin building. Will do nothing if all the required coordinates have not
     * been added or if any are invalid.
     */
	public void startBuild();

    /**
     * @return whether or not all the required coordinates have been added.
     */
	public boolean gotAllCoordinates();

    /**
     * add the next coordinate. Will do nothing if no more coordinates are expected.
     * @param position the coordinate to add.
     */
	public void addCoordinate(Block position);

    /**
     * examines all the coordinates add <i>so far</i> to determine whether they are valid.
     * This does not require that all coordinates have been added yet, just that none of
     * them conflict so far.
     * @return whether coordinates added so far are valid.
     */
	public boolean checkCoordinates();
    
    /**
     * @return the name of the structure being built, e.g. "Wall", "Sphere", etc.
     */
	public String getBuildingName();

    /**
     * @return a message to display to the player that says we are starting to build.
     * This is completely abstract since some Builders might not be building a structure,
     * as such, and thus the message will not be a consistent pattern for all Builders.
     */
	public String getBuildingMessage();

    /**
     * @return a message to display to the user saying that the coordinate just added has
     * been accepted. This might be more specific than just "position 1", etc.
     */
    public String getLastPositionMessage();

    /**
     * @return a message to display to the user saying that they should place the next
     * coordinate. Again, this may be more specific than just "position 2", etc.
     */
    public String getNextPositionMessage();

    /**
     * @return a message to display to the user describing why the last coordinate added
     * was invalid. This should be just the reason; a colorized "ERROR" will be prefixed.
     */
	public String getCoordinateCheckFailed();
}