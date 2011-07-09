package me.simplex.buildr;

import me.simplex.buildr.runnable.Buildr_Wallbuilder;
import me.simplex.buildr.util.Buildr_WallType;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_WallManager {
	private Player wallcreater;
	private Block position1,position2;
	private Buildr_WallType type;
	private Material material;
	private boolean aironly;
	private boolean coordinate1placed = false;
	private Buildr plugin;
	
	public Buildr_WallManager(Player player, Material material, boolean aironly, Buildr plugin) {
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
		Buildr_WallType wallType = checkWallType();
		if (wallType==null) {
			return false;
		}
		else {
			this.type = wallType;
			return true;
		}
	}
	
	private Buildr_WallType checkWallType(){
		if (position1.getX() == position2.getX()) {
			return Buildr_WallType.WALL_X;
		}
		if (position1.getY() == position2.getY()) {
			return Buildr_WallType.WALL_Y;
		}
		if (position1.getZ() == position2.getZ()) {
			return Buildr_WallType.WALL_Z;
		}
		else {
			return null;
		}
	}
	
	public void startBuild(){
		new Thread(new Buildr_Wallbuilder(position1,position2,type,material,aironly,plugin,wallcreater)).start();
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
