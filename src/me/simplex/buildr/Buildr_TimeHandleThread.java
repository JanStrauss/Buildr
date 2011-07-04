package me.simplex.buildr;

import org.bukkit.World;

public class Buildr_TimeHandleThread implements Runnable {
	Buildr plugin;
	boolean alive;



	public Buildr_TimeHandleThread(Buildr plugin) {
		this.plugin = plugin;
	}


	@Override
	public void run() {
		while(isAlive()){
			try {
				Thread.sleep(30000); //sleep 30sec
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		for (World world : plugin.getWorldbuildmode()) {
			if (world.getTime()>=1000) {
				world.setTime(0);
				plugin.log("time reset");
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
