package me.simplex.buildr.runnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import me.simplex.buildr.Buildr;

import org.bukkit.entity.Player;

public class Buildr_Runnable_StateFileUpdater implements Runnable{
	private File statefile;
	private ArrayList<Player> builders;
	private Buildr plugin;

	
	public Buildr_Runnable_StateFileUpdater(File statefile, ArrayList<Player> builders, Buildr plugin) {
		this.statefile = statefile;
		this.builders = builders;
		this.plugin = plugin;
	}

	@Override
	public void run() {
		if (builders== null) {
			statefile.delete();
			return;
		}
		ArrayList<String> playernames= new ArrayList<String>();
		for (Player player : builders) {
			playernames.add(player.getName());
		}
		
		System.out.println("StateFile update. size:"+builders.size());
		
		try {
			ObjectOutputStream objctOutStrm = new ObjectOutputStream(new FileOutputStream(statefile));
			objctOutStrm.writeObject(playernames);
			objctOutStrm.flush();
			objctOutStrm.close();
		} catch (Exception e) {
			plugin.log("Failed to write to InventoryStateFile");
			e.printStackTrace();
		}
		
	}
}
