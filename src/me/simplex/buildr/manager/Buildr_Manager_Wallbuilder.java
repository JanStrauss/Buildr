package me.simplex.buildr.manager;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.Buildr_Runnable_Wallbuilder;
import me.simplex.buildr.util.Buildr_Type_Wall;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_Wallbuilder {
	private Player wallcreater;
	private Block position1,position2;
	private Buildr_Type_Wall type;
	private Material material;
	private boolean aironly;
	private boolean coordinate1placed = false;
	private Buildr plugin;
	
	public Buildr_Manager_Wallbuilder(Player player, Material material, boolean aironly, Buildr plugin) {
		this.wallcreater=player;
		this.material = material;
		this.aironly = aironly;
		this.plugin = plugin;
	}
	
	public void addCoordinate1(Block position1){
		this.position1 = position1;
		coordinate1placed = true;
	}
	
	public boolean checkCoordinates(Block position2){
		this.position2=position2;
		Buildr_Type_Wall wallType = checkWallType();
		if (wallType==null) {
			return false;
		}
		else {
			this.type = wallType;
			return true;
		}
	}
	
	private Buildr_Type_Wall checkWallType(){
		if (position1.getX() == position2.getX()) {
			return Buildr_Type_Wall.WALL_X;
		}
		if (position1.getY() == position2.getY()) {
			return Buildr_Type_Wall.WALL_Y;
		}
		if (position1.getZ() == position2.getZ()) {
			return Buildr_Type_Wall.WALL_Z;
		}
		else {
			return null;
		}
	}
	
	public void startBuild(){
		new Thread(new Buildr_Runnable_Wallbuilder(position1,position2,type,material,aironly,plugin,wallcreater)).start();
	}
	
	/**
	 * @return the wallcreater
	 */
	public Player getWallcreater() {
		return wallcreater;
	}
	/**
	 * 
	 * @return is coordinate1placed
	 */
	public boolean isCoordinate1placed(){
		return coordinate1placed;
	}

}
