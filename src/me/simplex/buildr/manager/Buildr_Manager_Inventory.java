package me.simplex.buildr.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.runnable.Buildr_Runnable_InventorySaver;
import me.simplex.buildr.runnable.Buildr_Runnable_StateFileUpdater;
import me.simplex.buildr.util.Buildr_Container_ItemStackSave;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Buildr_Manager_Inventory {
	Buildr plugin;

	public Buildr_Manager_Inventory(Buildr plugin) {
		this.plugin = plugin;
	}
	
	public void switchInventory(Player player){
		String path = plugin.getPluginDirectory()+File.separator+"inv_data"+File.separator+player.getName()+".inv";
		checkBackupFile(player);
		
		ItemStack[] currInv = loadCurrentInventory(player);
		ItemStack[] newInv = loadBackupInventory(player, path);
		
		saveBackupInventory(player, currInv, path);
			
		changeCurrentInventory(player, newInv);
		
	}

	private ItemStack[] loadCurrentInventory(Player player){
		return  player.getInventory().getContents();
	}
	
	private void changeCurrentInventory(Player player, ItemStack[] newInv){
		for (int i = 0; i < player.getInventory().getSize(); i++) {
			player.getInventory().setItem(i, newInv[i]);
		}
	}
	
	
	/**
	 * @param invtype true= normal, false=build
	 * @return fullinv with Armor
	 */

	private ItemStack[] loadBackupInventory(Player player, String path){
		ObjectInputStream objctInStrm;
		Buildr_Container_ItemStackSave[] FileContainer =null;

		try {
			objctInStrm = new ObjectInputStream(new FileInputStream(path));
			FileContainer = (Buildr_Container_ItemStackSave[])objctInStrm.readObject();
			objctInStrm.close();
		} catch (FileNotFoundException e) {
			System.out.println("####################################################################");
			System.out.println("FileNotFound");
		} catch (IOException e) {
			System.out.println("####################################################################");
			System.out.println("IOExc");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("####################################################################");
			System.out.println("ClassNotFound");
			e.printStackTrace();
		}
		ItemStack[] inventory = new ItemStack[FileContainer.length];
		for (int i = 0; i < FileContainer.length; i++) {
			if (FileContainer[i]!=null) {
				inventory[i] = new ItemStack(FileContainer[i].getType(), FileContainer[i].getAmount(), FileContainer[i].getDurability(), FileContainer[i].getMaterial_data());
			}
		}
		return inventory;
	}
	
	private Thread saveBackupInventory(Player player,ItemStack[] inv, String path){
		Thread thread = new Thread(new Buildr_Runnable_InventorySaver(player, inv, path));
		thread.start();
		return thread;
	}
	
	public boolean startupCheck(){
		return new File(plugin.getPluginDirectory()+File.separator+"inv_data").mkdir();
	}
	
	private void checkBackupFile(Player player) {
		if (new File(plugin.getPluginDirectory()+File.separator+"inv_data"+File.separator+player.getName()+".inv").exists()) {
			//plugin.log("InvBackup for "+player.getName()+" found");
			return;
		}
		createNewBuildInv(player);
		//plugin.log("InvBackup for "+player.getName()+" created");
	}

	private void createNewBuildInv(Player player) {
		ItemStack[] newinv = new ItemStack[40];
		
		newinv[0]  = new ItemStack(278, 1); //Diamond Pickaxe
		newinv[1]  = new ItemStack(279, 1); //Diamond axe
		newinv[2]  = new ItemStack(280, 1); //Stick
		newinv[3]  = new ItemStack(50,64); //Torch
		
		newinv[5]  = new ItemStack(20, 64); //Stone
		newinv[6]  = new ItemStack(1, 64); //Stone
		newinv[7]  = new ItemStack(4, 64); //Cobblestone
		newinv[8]  = new ItemStack(5, 64); //Planks
		
		newinv[9]  = new ItemStack(53, 64); //Woodenstairs
		newinv[10] = new ItemStack(67, 64); //Cobblestonestairs
		
		String path = plugin.getPluginDirectory()+File.separator+"inv_data"+File.separator+player.getName()+".inv";
		try {
			player.sendMessage("No buildmode inventory found, creating new default..");
			saveBackupInventory(player, newinv, path).join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	public void updateInventoryStateFile(ArrayList<String> toProcess) {
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Buildr_Runnable_StateFileUpdater(new File(plugin.getPluginDirectory()+File.separator+"inv_data"+File.separator+"InventoryState.dat"), toProcess,plugin));
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> loadInventoryStateFile(){
		ArrayList<String> toHandleName = new ArrayList<String>();
		try {
			ObjectInputStream objctInStrm = new ObjectInputStream(new FileInputStream(plugin.getPluginDirectory()+File.separator+"inv_data"+File.separator+"InventoryState.dat"));
			toHandleName = (ArrayList<String>)objctInStrm.readObject();
			objctInStrm.close();
		}catch (Exception e) {
			plugin.log("Failed to load InventoryStateFile");
			e.printStackTrace();
		}
		return toHandleName;
	}
	
	public boolean checkInventoryStateFile(){
		return new File(plugin.getPluginDirectory()+File.separator+"inv_data"+File.separator+"InventoryState.dat").exists();
	}

	

}
