package me.simplex.buildr.manager.builder;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.builder.Buildr_Runnable_Builder_Wall;
import me.simplex.buildr.util.Buildr_Interface_Building;
import me.simplex.buildr.util.Buildr_Type_Wall;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_Builder_Wall implements Buildr_Interface_Building {
	private Player wallcreater;
	private Block position1,position2;
	private Buildr_Type_Wall type;
	private Material material;
	private boolean aironly;
	private boolean coordinate1placed = false;
	private Buildr plugin;
	
	public Buildr_Manager_Builder_Wall(Player player, Material material, boolean aironly, Buildr plugin) {
		this.wallcreater=player;
		this.material = material;
		this.aironly = aironly;
		this.plugin = plugin;
	}
	
	public void addCoordinate1(Block position1){
		this.position1 = position1;
		coordinate1placed = true;
	}
	
	@Override
	public boolean checkCoordinates(){
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
	
	@Override
	public void startBuild(){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Builder_Wall(position1,position2,type,material,aironly,plugin,wallcreater));
	}
	
	/**
	 * @return the wallcreater
	 */
	@Override
	public Player getWallcreater() {
		return wallcreater;
	}
	/**
	 * 
	 * @return is coordinate1placed
	 */
	public boolean isCoordinate1Placed(){
		return coordinate1placed;
	}

	@Override
	public void addCoordinate2(Block position2) {
		this.position2=position2;
	}


	@Override
	public String getBuildingName() {
		return "Wall";
	}
	
	@Override
	public String getCoordinateCheckFailed() {
		return "Atleast one dimension of the blocks must be the same. Wallbuild stopped.";
	}

}
