package me.simplex.buildr.manager.commands;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;
import me.simplex.buildr.util.Buildr_Manager_Command_Super;
import me.simplex.buildr.util.Buildr_Type_Wool;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Buildr_Manager_Command_Airfloor extends Buildr_Manager_Command_Super {

	public Buildr_Manager_Command_Airfloor(Buildr plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("airfloor")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.airfloor")) {
				if (args.length==0) {
					return false;
				}
				try {
					int material=0,height=0,size=0;
					if (args.length > 3 && args.length < 2) {
						return false;
					}
					if (args.length == 3) {
						size = Math.abs(Integer.parseInt(args[2]));
						if (size > 100) {
							size = 100;
						}
					}
					height = Math.abs(Integer.parseInt(args[1]));
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						material = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase().substring(5));
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sendToSender(sender, MsgType.ERROR, "No such wool");
							return true;
						}
					}
					else {
						try {
							material = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								material = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {

								sendToSender(sender, MsgType.ERROR, "wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(material)== null) {
						sendToSender(sender, MsgType.ERROR, "invalid material");
						return true;
					}
					this.cmd_airfloor(sender, material, mat_data, height, size);
					} 
				catch (NumberFormatException e) {
					sendToSender(sender, MsgType.WARNING, "Wrong format, usage: /airfloor <material> <height> <size>");
				}
			}
			else {
				sendToSender(sender, MsgType.ERROR, "You dont have the permission to perform this action");
			}
			return true;
		}
		return false;
	}
	
	public void cmd_airfloor(CommandSender sender, int material, byte mat_data, int height, int size){
		if (!plugin.getConfigValue("FEATURE_AIRFLOOR")) {
			return;
		}
		Player player = (Player)sender;
		int blockheight = player.getLocation().getBlockY()+height-1;
		Location location = new Location(player.getWorld(), player.getLocation().getBlockX(), blockheight, player.getLocation().getBlockZ());
		HashMap<Block, Buildr_Container_UndoBlock> UnDoList = new HashMap<Block, Buildr_Container_UndoBlock>();		
		if (blockheight > 128) {
			blockheight = 128;
		}
		if (size == 0 || size == 1) {
			Block block = player.getWorld().getBlockAt(location);
			UnDoList.put(block,new Buildr_Container_UndoBlock(block.getType(), block.getData()));
			block.setTypeId(material);
		}
		else {
			int offset;
			
			if ((size % 2) == 0) {offset = size/2;}
			else {offset = (size-1)/2;}
			
			int xstart= location.getBlockX()-offset;
			int zstart= location.getBlockZ()-offset;
			
			int x= xstart;
			int z= zstart;
			
			for (int i = 0; i < size; i++) { //x
				for (int j = 0; j < size; j++) {//z
					Block block = player.getWorld().getBlockAt(x, blockheight, z);
					if (!plugin.checkPermission(player, "buildr.feature.break_bedrock")) {
						if (!block.getType().equals(Material.BEDROCK)) {
							UnDoList.put(block, new Buildr_Container_UndoBlock(block.getType(), block.getData()));
							player.getWorld().getBlockAt(x, blockheight, z).setTypeId(material);
							player.getWorld().getBlockAt(x, blockheight, z).setData(mat_data);
						}
					}
					else {
						UnDoList.put(block, new Buildr_Container_UndoBlock(block.getType(), block.getData()));
						player.getWorld().getBlockAt(x, blockheight, z).setTypeId(material);
						player.getWorld().getBlockAt(x, blockheight, z).setData(mat_data);
					}
					z++;
				}
				z=zstart;
				x++;
			}
		}
		sendToSender(sender, MsgType.INFO, "Block(s) placed");
		plugin.getUndoList().addToStack(UnDoList, player);
		plugin.log(player.getName()+" used /airfloor: "+UnDoList.size()+" blocks affected");
	}
}
