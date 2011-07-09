package me.simplex.buildr.runnable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import me.simplex.buildr.util.Buildr_Container_ItemStackSave;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Buildr_Runnable_InventorySaver implements Runnable{
	Player player;
	ItemStack[] inventory;
	String path;
	
	public Buildr_Runnable_InventorySaver(Player player, ItemStack[] inventory, String path) {
		this.player = player;
		this.inventory = inventory;
		this.path = path;
	}

	@Override
	public void run() {
		Buildr_Container_ItemStackSave[] FileContainer = new Buildr_Container_ItemStackSave[inventory.length];
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i]!= null) {
				byte savedata = 0;
				if (inventory[i].getData() != null) {
					savedata = inventory[i].getData().getData();
				}
				FileContainer[i] = new Buildr_Container_ItemStackSave(inventory[i].getTypeId(), inventory[i].getAmount(), inventory[i].getDurability(), savedata);
			}
		}
		
		try {
			ObjectOutputStream objctOutStrm = new ObjectOutputStream(new FileOutputStream(path));
			objctOutStrm.writeObject(FileContainer);
			objctOutStrm.flush();
			objctOutStrm.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR#############################################################");
		}
		
	}
}
	
