package me.simplex.buildr.util;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class Buildr_BlockToDropConv {

	public static ArrayList<ItemStack> convert(Block block){
		if (block.getTypeId() != 0) {
			ArrayList<ItemStack> ret= new ArrayList<ItemStack>();
			switch (block.getTypeId()) {
			case 1: ret.add(new ItemStack(4, 1)); break;
			case 2: ret.add(new ItemStack(3, 1)); break;
			
			default: ret.add(new ItemStack(block.getTypeId(), 1));break;
			}
			return ret;
		}
		else {
			return null;
		}
	}
}
