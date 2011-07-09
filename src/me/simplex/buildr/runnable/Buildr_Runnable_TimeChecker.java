package me.simplex.buildr.runnable;

import me.simplex.buildr.Buildr;

import org.bukkit.World;

public class Buildr_Runnable_TimeChecker implements Runnable {
	Buildr plugin;
	boolean alive;



	public Buildr_Runnable_TimeChecker(Buildr plugin) {
		this.plugin = plugin;
		this.alive=true;
	}


	@Override
	public void run() {
		while(isAlive()){
			for (World world : plugin.getWorldBuildMode()) {
				if (plugin.getConfigValue("GLOBALBUILD_TIME")) {
					if (world.getTime()>=10000) {
						world.setTime(0);
						plugin.log("time reset");
					}
				}
			}
		}
	}
	
	public boolean isAlive() {
		return alive;
	}


	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
