package me.simplex.buildr.util;

import java.util.ArrayList;
import java.util.Random;

import me.simplex.buildr.Buildr;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class Buildr_BlockToDropConverter {
	private Random rnd = new Random();
	private Buildr plugin;
	
	public Buildr_BlockToDropConverter(Buildr plugin) {
		this.plugin = plugin;
	}
	/**
	 * Fakes natural drops for a given block
	 * 
	 * @param block the Block to build drops for
	 * @return ArrayList<ItemStack> of all the Drops
	 */
	public ArrayList<ItemStack> convert(Block block){
			ArrayList<ItemStack> ret= new ArrayList<ItemStack>();
			switch (block.getTypeId()) {
			case  0: ; break;
			case  1: ret.add(new ItemStack(4, 1)); break;
			case  2: ret.add(new ItemStack(3, 1)); break;
			case  7: ; break;
			case  8: ; break;
			case  9: ; break;
			case 10: ; break;
			case 11: ; break;
			case 13: ret.add(new ItemStack(13, 1));
					 addOnRndChance(318, 1, 0.1, ret); break;
			case 16: ret.add(new ItemStack(263, 1)); break;
			case 17: ret.add(new ItemStack(17, 1,(short)0,block.getData())); break;
			case 18: addOnRndChance(6, 1, 0.05, ret);break;
			case 20: ; break;
			case 21: ret.add(new ItemStack(351,genRndMinMax(4, 8))); break;
			case 26: ret.add(new ItemStack(355, 1)); break;
			case 35: ret.add(new ItemStack(35,1,(short)0,block.getData())); break;
			case 43: ret.add(new ItemStack(44,1,(short)0,block.getData())); break;
			case 44: ret.add(new ItemStack(44,1,(short)0,block.getData())); break;
			case 47: ; break;
			case 51: ; break;
			case 52: ; break;
			case 53: checkStairsDropSettings(block.getTypeId(), ret); break; //Woodenstairs
			case 55: ret.add(new ItemStack(331,1)); break;
			case 56: ret.add(new ItemStack(264,1)); break;
			case 59: ret.add(new ItemStack(295,1)); break;
			case 60: ret.add(new ItemStack(3,1)); break;
			case 62: ret.add(new ItemStack(61,1)); break;
			case 63: ret.add(new ItemStack(323,1)); break;
			case 64: ret.add(new ItemStack(324,1)); break;
			case 67: checkStairsDropSettings(block.getTypeId(), ret); break; //Cobblestonestairs
			case 68: ret.add(new ItemStack(323,1)); break;
			case 71: ret.add(new ItemStack(330,1)); break;
			case 73: ret.add(new ItemStack(331,genRndMinMax(4, 6))); break;
			case 74: ret.add(new ItemStack(331,genRndMinMax(4, 6))); break;
			case 75: ret.add(new ItemStack(76,1)); break;
			case 78: ; break;
			case 79: ; break;
			case 82: ret.add(new ItemStack(337,4)); break;
			case 83: ret.add(new ItemStack(338,1)); break;
			case 89: ret.add(new ItemStack(348,1)); break;
			case 90: ; break;
			case 93: ret.add(new ItemStack(356,1)); break;
			case 94: ret.add(new ItemStack(356,1)); break;
			default: ret.add(new ItemStack(block.getTypeId(),1)); break;
			}
			return ret;
	}
	
	private  void addOnRndChance(int type,int amount,double chance, ArrayList<ItemStack> ret){
		if (rnd.nextDouble()>= 1.0-chance) {
			ret.add(new ItemStack(type, 1));
		}
	}
	private int genRndMinMax(int min,int max){
		return rnd.nextInt(max)+min;
	}
	
	private void checkStairsDropSettings(int type, ArrayList<ItemStack> ret){
		if (plugin.getConfigValue("BUILDMODE_STAIRMODE")) {
			ret.add(new ItemStack(type,1));
			return;
		}
		if (type == 67) {
			ret.add(new ItemStack(4,1));
		}
		else {
			ret.add(new ItemStack(5,1));
		}
	}
}
