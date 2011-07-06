package me.simplex.buildr;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Buildr_Wallbuilder {
	private Buildr plugin;
	private Player wallcreater;
	private Block position1,position2;
	
	private HashMap<Block, Material> buildWallX(Block pos1,Block pos2,Material wallmaterial, boolean aironly){
		HashMap<Block, Material> undo = new HashMap<Block, Material>();
		int fixed_X = pos1.getX();
		int starty = calcStartPoint(pos1.getY(), pos2.getY());
		int startz = calcStartPoint(pos1.getZ(), pos2.getZ());
		
		int y = starty;
		int z = startz;
		
		for (int i = 0; i < calcWidth(pos1.getX(), pos2.getX()); i++) {			//Y
			for (int j = 0; j < calcWidth(pos1.getZ(), pos2.getZ()); j++) {		//z
				
				Block actionBlock = pos1.getWorld().getBlockAt(fixed_X, y, z);
				
				if (aironly) {
					if (actionBlock.getType().equals(Material.AIR)) {
						undo.put(actionBlock, actionBlock.getType());
						actionBlock.setType(wallmaterial);
					}
				}
				else {
					undo.put(actionBlock, actionBlock.getType());
					actionBlock.setType(wallmaterial);
				}

				z++;
			}
			y++;
			z=startz;
		}
		return undo;
	}
	
	private HashMap<Block, Material> buildWallY(Block pos1,Block pos2,Material wallmaterial, boolean aironly){
		HashMap<Block, Material> undo = new HashMap<Block, Material>();
		int fixed_Y = pos1.getY();
		int startx = calcStartPoint(pos1.getX(), pos2.getX());
		int startz = calcStartPoint(pos1.getZ(), pos2.getZ());
		
		int x = startx;
		int z = startz;
		
		for (int i = 0; i < calcWidth(pos1.getX(), pos2.getX()); i++) {			//x
			for (int j = 0; j < calcWidth(pos1.getZ(), pos2.getZ()); j++) {		//z
				
				Block actionBlock = pos1.getWorld().getBlockAt(x, fixed_Y, z);
				
				if (aironly) {
					if (actionBlock.getType().equals(Material.AIR)) {
						undo.put(actionBlock, actionBlock.getType());
						actionBlock.setType(wallmaterial);
					}
				}
				else {
					undo.put(actionBlock, actionBlock.getType());
					actionBlock.setType(wallmaterial);
				}

				z++;
			}
			x++;
			z=startz;
		}
		return undo;
	}
	
	private HashMap<Block, Material> buildWallZ(Block pos1,Block pos2,Material wallmaterial, boolean aironly){
		HashMap<Block, Material> undo = new HashMap<Block, Material>();
		int fixed_Z = pos1.getZ();
		int startx = calcStartPoint(pos1.getX(), pos2.getX());
		int starty = calcStartPoint(pos1.getY(), pos2.getY());
		
		int x = startx;
		int y = starty;
		
		for (int i = 0; i < calcWidth(pos1.getX(), pos2.getX()); i++) {			//x
			for (int j = 0; j < calcWidth(pos1.getZ(), pos2.getZ()); j++) {		//y
				
				Block actionBlock = pos1.getWorld().getBlockAt(x, y, fixed_Z);
				
				if (aironly) {
					if (actionBlock.getType().equals(Material.AIR)) {
						undo.put(actionBlock, actionBlock.getType());
						actionBlock.setType(wallmaterial);
					}
				}
				else {
					undo.put(actionBlock, actionBlock.getType());
					actionBlock.setType(wallmaterial);
				}

				y++;
			}
			x++;
			y=starty;
		}
		return undo;
	}
	
	private int calcStartPoint(int coordinate1, int coordinate2){
		if (coordinate1<=coordinate2) {
			return coordinate1;
		}
		else {
			return coordinate2;
		}
	}
	
	private int calcWidth(int coordinate1, int coordinate2){
		return Math.abs(coordinate1-coordinate2);
	}
}
