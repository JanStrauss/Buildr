package me.simplex.buildr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Buildr_InventoryManager {
	Buildr plugin;

	public Buildr_InventoryManager(Buildr plugin) {
		this.plugin = plugin;
	}
	
	public void switchInventory(Player player){
		ItemStack[] currInv = loadCurrentInventory(player);
		ItemStack[] newInv = loadBackupInventory(player);
		
		saveBackupInventory(player, currInv);
		
		changeCurrentInventory(player, newInv);
	}

	private ItemStack[] loadCurrentInventory(Player player){
		ItemStack[] stacks = player.getInventory().getContents();
		ItemStack[] fullinv = new ItemStack[stacks.length+4];
		
		//System.out.println("Stacks: "+stacks.length);
		for (int i = 0; i < stacks.length; i++) {
			fullinv[i] = stacks[i];
		}
		fullinv[stacks.length+1] = player.getInventory().getHelmet();
		fullinv[stacks.length+2] = player.getInventory().getChestplate();
		fullinv[stacks.length+3] = player.getInventory().getLeggings();
		fullinv[stacks.length+4] = player.getInventory().getBoots();
		
		return fullinv;
	}
	
	private void changeCurrentInventory(Player player, ItemStack[] newInv){
		for (int i = 0; i < newInv.length-4; i++) {
			player.getInventory().setItem(i, newInv[i]);
		}
		player.getInventory().setHelmet(newInv[newInv.length-3]);
		player.getInventory().setChestplate(newInv[newInv.length-2]);
		player.getInventory().setLeggings(newInv[newInv.length-1]);
		player.getInventory().setBoots(newInv[newInv.length]);
	}
	
	
	/**
	 * @param invtype true= normal, false=build
	 * @return fullinv with Armor
	 */
	private ItemStack[] loadBackupInventory(Player player){
		ObjectInputStream objctInStrm;
		ItemStack[] input=null;
		try {
			objctInStrm = new ObjectInputStream(new FileInputStream(plugin.getPluginDirectory()+File.separator+"inv_backups"+File.separator+player+".inv"));
			input = (ItemStack[])objctInStrm.readObject();
			objctInStrm.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (input != null) {
			return input;
		}
		else {
			return null;
		}

	}
	
	private void saveBackupInventory(Player player,ItemStack[] inv){
		try {
			ObjectOutputStream objctOutStrm = new ObjectOutputStream(new FileOutputStream(plugin.getPluginDirectory()+File.separator+"inv_backups"+File.separator+player+".inv"));
			objctOutStrm.writeObject(inv);
			objctOutStrm.flush();
			objctOutStrm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
