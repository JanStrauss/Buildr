package me.simplex.buildr.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface Buildr_Interface_Building {
	public Player getBuildingcreater();
	public void startBuild();
	public boolean isCoordinate1Placed();
	public void addCoordinate1(Block position1);
	public void addCoordinate2(Block position2);
	public boolean checkCoordinates();
	public String getBuildingName();
	public String getCoordinateCheckFailed();
}