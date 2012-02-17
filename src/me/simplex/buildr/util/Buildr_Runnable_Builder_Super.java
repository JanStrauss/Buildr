package me.simplex.buildr.util;

import java.util.HashMap;

import me.simplex.buildr.Buildr;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public abstract class Buildr_Runnable_Builder_Super implements Runnable {
	protected Block position1, position2;
	protected Material material;
	protected boolean aironly;
	protected boolean hollow;
	protected Buildr plugin;
	protected Player player;
	protected byte material_data;
	protected Material replace_mat;

	@Override
	public abstract void run();
	
	protected void changeBlock(Block block_handle, HashMap<Block, Buildr_Container_UndoBlock> undo){
		undo.put(block_handle, new Buildr_Container_UndoBlock(block_handle.getType(), block_handle.getData()));
		if (canBuild(player, block_handle)) {
			if (!plugin.checkPermission(player, "buildr.feature.break_bedrock")) {
				if (!block_handle.getType().equals(Material.BEDROCK)) {
					block_handle.setType(material);
					block_handle.setData(material_data);
				}
			}
			else {
				block_handle.setType(material);
				block_handle.setData(material_data);
			}
		}
	}
	
	protected boolean canBuild(Player player, Block block){
		BlockState state = block.getState();
		BlockPlaceEvent event = new BlockPlaceEvent(block, state, block, player.getItemInHand(), player, true);
		plugin.getServer().getPluginManager().callEvent(event);
		return !event.isCancelled();
	}
	
	protected int calcStartPoint(int coordinate1, int coordinate2){
		return Math.min(coordinate1, coordinate2);
	}
	
	protected int calcEndPoint(int coordinate1, int coordinate2){
		return Math.max(coordinate1, coordinate2);
	}
	
	protected boolean checkBlockIsOutside(Block block){
		if (block.getX() == position1.getX() ||
			block.getX() == position2.getX() ||
			block.getY() == position1.getY() ||
			block.getY() == position2.getY() ||
			block.getZ() == position1.getZ() ||
			block.getZ() == position2.getZ()) {
			return true;
		}
		else {
			return false;
		}
	}
}
