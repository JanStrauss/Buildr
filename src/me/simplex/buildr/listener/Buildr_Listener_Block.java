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
package me.simplex.buildr.listener;

import me.simplex.buildr.Buildr;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class Buildr_Listener_Block implements Listener {
	Buildr plugin;
	
	public Buildr_Listener_Block(Buildr buildr) {
		this.plugin = buildr;
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (plugin.getConfigValue("BUILDMODE_UNLIMITED_ITEMSTACK")) {
			if (plugin.checkPlayerBuildMode(event.getPlayer())) {
				//ItemStack give = event.getItemInHand();
				ItemStack give = event.getPlayer().getItemInHand();
				give.setAmount(64);
				//System.out.println("dmg: "+give.getDurability());
				//System.out.println("type: "+give.getType().toString());
				
				event.getPlayer().setItemInHand(give);
			}
		}
//		System.out.println("BlockPlace");
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		
	}
}