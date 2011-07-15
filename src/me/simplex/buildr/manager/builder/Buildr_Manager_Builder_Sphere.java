package me.simplex.buildr.manager.builder;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.builder.Buildr_Runnable_Builder_Sphere;
import me.simplex.buildr.util.Buildr_Interface_Building;

public class Buildr_Manager_Builder_Sphere implements Buildr_Interface_Building {
	private Player wallcreater;
	private Block position1,position2;
	private Material material;
	private boolean aironly;
	private boolean hollow;
	private boolean coordinate1placed;
	private boolean halfcube;
	private Buildr plugin;
	private byte material_data;

	public Buildr_Manager_Builder_Sphere(Player wallcreater, Material material,boolean aironly, boolean hollow,boolean halfcube, Buildr plugin, byte material_data) {
		this.wallcreater = wallcreater;
		this.material = material;
		this.aironly = aironly;
		this.hollow = hollow;
		this.plugin = plugin;
		this.halfcube = halfcube;
		this.material_data = material_data;
	}

	@Override
	public void addCoordinate1(Block position1) {
		this.position1 = position1;
		this.coordinate1placed = true;
	}

	@Override
	public void addCoordinate2(Block position2) {
		this.position2 = position2;
	}

	@Override
	public boolean checkCoordinates() {
		return true;
	}

	@Override
	public String getBuildingName() {
		return "Sphere";
	}

	@Override
	public String getCoordinateCheckFailed() {
		return "Herp Derp";
	}

	@Override
	public Player getWallcreater() {
		return wallcreater;
	}

	@Override
	public boolean isCoordinate1Placed() {
		return coordinate1placed;
	}

	@Override
	public void startBuild() {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Builder_Sphere(position1, position2, material, aironly, hollow, halfcube, plugin, wallcreater,material_data));
	}
}
