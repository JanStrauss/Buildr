package me.simplex.buildr.runnable;

import java.util.ArrayList;
import java.util.HashMap;

import me.simplex.buildr.Buildr;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Buildr_TreeFeller implements Runnable {
	
	private ArrayList<Block> logs;
	private ArrayList<Block> leaves;
	private ArrayList<Block> checked;
	
	private HashMap<Block, Material> undo;
	
	private Block baseblock;
	private Buildr plugin;
	private Player player;
	

	public Buildr_TreeFeller(Block baseblock,  Buildr plugin, Player player) {
		this.baseblock = baseblock;
		this.plugin = plugin;
		this.player = player;
		
		this.logs = new ArrayList<Block>();
		this.leaves = new ArrayList<Block>();
		this.checked = new ArrayList<Block>();
		
		this.undo = new HashMap<Block, Material>();
	}



	@Override
	public void run() {
		checkBlock(baseblock);
		
		for (Block blk : logs) {
			undo.put(blk, Material.LOG);
			blk.setType(Material.AIR);
		}
		if (plugin.getConfigValue("TREECUTTER_CUT_LEAVES")) {
			for (Block blk : leaves) {
				undo.put(blk, Material.LEAVES);
				blk.setType(Material.AIR);
			}
		}
		plugin.getUndoList().addToStack(undo, player);
		player.sendMessage("Felt Tree. Blocks changed: "+undo.size());
	}
	
	private void checkBlock(Block base){
		if (checked.contains(base)) {
			return;
		}
		checked.add(base);
		if (base.getType() == Material.LOG) {
			addIfNotInListLog(base);
			for (int i = 0; i < 26; i++) {
				checkBlock(giveNeighborBlockForLog(base, i));
			}
		}
		if (!plugin.getConfigValue("TREECUTTER_CUT_LEAVES")) {
			return;
		}
		else if (base.getType() == Material.LEAVES) {
			addIfNotInListLeaves(base);
			for (int i = 0; i < 10; i++) {
				checkBlock(giveNeighborBlockForLeaves(base, i));
			}
		}
	}
	
	private void addIfNotInListLog(Block block){
		if (block.getType() == Material.LOG) {
			if (!logs.contains(block)) {
				logs.add(block);
			}
		}
	}
	
	private void addIfNotInListLeaves(Block block){
		if(block.getType() == Material.LEAVES){
			if (!leaves.contains(block)) {
				leaves.add(block);
			}
		}
	}
	
	private Block giveNeighborBlockForLog(Block toProcess, int id){
		Block ret=null;
		int x = 0;
		int y = 0;
		int z = 0;
		
		switch (id) {
		
		//height+1
		case  0: x--	; y++	; z--;	; break;
		case  1:  		; y++	; z--;	; break;
		case  2: x++	; y++	; z--;	; break;
		case  3: x--	; y++	;		; break;
		case  4:  		; y++	;		; break;
		case  5: x++	; y++	;		; break;
		case  6: x--	; y++	; z++;	; break;
		case  7:  		; y++	; z++;	; break;
		case  8: x++	; y++	; z++;	; break;
		
		//height-1
		case  9: x--	; y--	; z--;	; break;
		case 10:  		; y--	; z--;	; break;
		case 11: x++	; y--	; z--;	; break;
		case 12: x--	; y--	;		; break;
		case 13:  		; y--	;		; break;
		case 14: x++	; y--	;		; break;
		case 15: x--	; y--	; z++;	; break;
		case 16:  		; y--	; z++;	; break;
		case 17: x++	; y--	; z++;	; break;
		
		//height0
		case 18: x--	;    	; z--;	; break;
		case 19:  		;    	; z--;	; break;
		case 20: x++	;    	; z--;	; break;
		case 21: x--	;    	;		; break;
		case 22: x++	;    	;		; break;
		case 23: x--	;    	; z++;	; break;
		case 24:  		;    	; z++;	; break;
		case 25: x++	;    	; z++;	; break;
		}
		
		ret = toProcess.getWorld().getBlockAt(x, y, z);
		ret = toProcess.getRelative(x, y, z);
		return ret;
	}
	
	private Block giveNeighborBlockForLeaves(Block toProcess, int id){
		Block ret=null;
		switch (id) {
		case  0: ret =  toProcess.getRelative(BlockFace.NORTH); break;
		case  1: ret =  toProcess.getRelative(BlockFace.NORTH_EAST); break;
		case  2: ret =  toProcess.getRelative(BlockFace.EAST); break;
		case  3: ret =  toProcess.getRelative(BlockFace.SOUTH_EAST); break;
		case  4: ret =  toProcess.getRelative(BlockFace.SOUTH); break;
		case  5: ret =  toProcess.getRelative(BlockFace.SOUTH_WEST); break;
		case  6: ret =  toProcess.getRelative(BlockFace.WEST); break;
		case  7: ret =  toProcess.getRelative(BlockFace.NORTH_WEST); break;
		case  8: ret =  toProcess.getRelative(BlockFace.UP); break;
		case  9: ret =  toProcess.getRelative(BlockFace.DOWN); break;
		}
		return ret;
	}

}
