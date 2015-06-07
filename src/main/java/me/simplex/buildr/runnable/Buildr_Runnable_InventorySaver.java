/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Buildr.
 * 
 * Buildr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Buildr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Buildr  If not, see <http://www.gnu.org/licenses/>.
 */
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
	
