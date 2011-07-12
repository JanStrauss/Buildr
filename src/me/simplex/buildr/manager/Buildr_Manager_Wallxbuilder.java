package me.simplex.buildr.manager;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.Buildr_Runnable_Builder_Wallx;
import me.simplex.buildr.util.Buildr_Interface_Building;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Manager_Wallxbuilder implements Buildr_Interface_Building {
	private Player wallcreater;
	private Block position1,position2;
	private Material material;
	private boolean aironly;
	private boolean coordinate1placed = false;
	private Buildr plugin;
	
	public Buildr_Manager_Wallxbuilder(Player player, Material material, boolean aironly, Buildr plugin) {
		this.wallcreater=player;
		this.material = material;
		this.aironly = aironly;
		this.plugin = plugin;
	}
	
	public void addCoordinate1(Block position1){
		this.position1 = position1;
		coordinate1placed = true;
	}
	
	public void addCoordinate2(Block position2){
		this.position2 = position2;
	}
	
	@Override
	public void startBuild(){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Builder_Wallx(position1,position2,material,aironly,plugin,wallcreater));
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
	public boolean isCoordinate1Placed(){
		return coordinate1placed;
	}

	@Override
	public boolean checkCoordinates() {
		return true;
	}

	@Override
	public String getBuildingName() {
		return "Wallx";
	}
	
	@Override
	public String getCoordinateCheckFailed() {
		return null;
	}
}
