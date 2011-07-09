package me.simplex.buildr.manager;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;
import me.simplex.buildr.util.Buildr_Type_Wool;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Buildr_Manager_Commands {
	Buildr plugin;
	
	/**
	 * 
	 * @param plugin
	 */
	public Buildr_Manager_Commands(Buildr plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * 
	 * @param sender
	 */
	public void cmd_globalbuild(CommandSender sender,World world){
		if (!plugin.getConfigValue("GLOBALBUILD_ENABLE")) {
			return;
		}
		if (plugin.getWorldBuildMode().contains(world)) {
			plugin.leaveGlobalbuildmode(world);
			sender.sendMessage("Globalbuildmode disabled");
			for (Player inhab : world.getPlayers()) {
				inhab.sendMessage(ChatColor.AQUA+((Player)sender).getName()+" disabled the Globalbuildmode on the world you are currently in");
			}
			plugin.log("Globalbuildmode disabled in World "+world.getName());
		}
		else {
			plugin.enterGlobalbuildmode(world);
			sender.sendMessage("Globalbuildmode enabled");
			for (Player inhab : world.getPlayers()) {
				inhab.sendMessage(ChatColor.AQUA+((Player)sender).getName()+" ensabled the Globalbuildmode on the world you are currently in");
			}
			plugin.log("Globalbuildmode enabled in World "+world.getName());
		}
	}
	
	/**
	 * 
	 * @param sender
	 */
	public void cmd_build(CommandSender sender){
		if (!plugin.getConfigValue("BUILDMODE_ENABLE")) {
			return;
		}
		if (plugin.getPlayerBuildMode().contains((Player)sender)) {
			
			sender.sendMessage(ChatColor.BLUE+"Buildmode disabled");
			plugin.leaveBuildmode((Player)sender);
		}
		else {
			
			sender.sendMessage(ChatColor.BLUE+"Buildmode enabled");
			plugin.enterBuildmode((Player)sender);
		}
	}
	
	/**
	 * 
	 * @param sender
	 */
	public void cmd_top(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_TOP")) {
			return;
		}
		Player player = (Player)sender;
		if (player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getWorld().getHighestBlockYAt(player.getLocation()), player.getLocation().getZ(),player.getLocation().getYaw(),player.getLocation().getPitch()))) 
		{
			player.sendMessage("Ported to Top.");
		}
		else {
			player.sendMessage("Something went wrong");
		}
	}
	
	/**
	 * 
	 * @param sender
	 * @param material
	 * @param height
	 * @param size
	 */
	public void cmd_airfloor(CommandSender sender, int material, int height, int size){
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
					UnDoList.put(block, new Buildr_Container_UndoBlock(block.getType(), block.getData()));
					player.getWorld().getBlockAt(x, blockheight, z).setTypeId(material);
					z++;
				}
				z=zstart;
				x++;
			}
		}
		sender.sendMessage("Block(s) placed");
		plugin.getUndoList().addToStack(UnDoList, player);
		plugin.log(player.getName()+" used /airfloor: "+UnDoList.size()+" blocks affected");
	}
	/**
	 * 
	 * @param sender
	 */
	public void cmd_undo(CommandSender sender){		
		if (!plugin.getConfigValue("FEATURE_AIRFLOOR") && !plugin.getConfigValue("FEATURE_WALLBUILDER")) {
		return;
		}
		Player player = (Player)sender;
		HashMap<Block, Buildr_Container_UndoBlock> undos = plugin.getUndoList().getAndDeleteFromStack(player);
		if (undos != null) {
			for (Block block : undos.keySet()) {
				block.setType(undos.get(block).getMaterial());
				block.setData(undos.get(block).getMaterialData());
			}
			plugin.log(player.getName()+" used /bu: "+undos.size()+" blocks affected");
			sender.sendMessage(undos.size()+" blocks restored");
		}
		else {
			sender.sendMessage("Nothing to undo");
		}
	}
	/**
	 * 
	 * @param sender
	 * @param material
	 * @param amount
	 */
	public void cmd_give(CommandSender sender, String material, int amount, String player) {
		if (!plugin.getConfigValue("FEATURE_GIVE")) {
			return;
		}
		Material give_mat = null;
		try {
			give_mat = Material.getMaterial(Integer.parseInt(material));
		} catch (NumberFormatException e) {
			give_mat = Material.matchMaterial(material);
		}
		if (give_mat == null) {
			sender.sendMessage(ChatColor.RED+"No such Item found");
			return;
		}
		if (amount ==-1 || amount == 0) {
			amount = 64;
		}
		Player giveto = ((Player)sender);
		if (player !=null) {
			giveto = sender.getServer().getPlayer(player);
		}
		if (giveto == null) {
			sender.sendMessage(ChatColor.RED+"Player not found");
			return;
		}
		if (give_mat.getId() == 0) {
			sender.sendMessage(ChatColor.RED+"You can't give air");
			return;
		}
		giveto.getInventory().addItem(new ItemStack(give_mat,amount));
		sender.sendMessage("Gave "+giveto.getName()+" a stack of "+amount+" "+give_mat.toString()+"(ID: "+give_mat.getId()+")");
	}
	/**
	 * 
	 * @param sender
	 * @param args
	 */
	public void cmd_wool(CommandSender sender, String args) {
		if (!plugin.getConfigValue("FEATURE_WOOL")) {
			return;
		}
		String upcase= args.toUpperCase();
		Buildr_Type_Wool woolcolor;
		try {
			woolcolor = Enum.valueOf(Buildr_Type_Wool.class, upcase);
		} catch (IllegalArgumentException e) {
			sender.sendMessage("No such color");
			return;
		}
		((Player)sender).getInventory().addItem(woolcolor.giveStack());
		String ret = "Gave yourself a stack of "+woolcolor.toString().toLowerCase()+" wool";
		sender.sendMessage(ret);
	}
	/**
	 * 
	 * @param sender
	 * @param material
	 * @param aironly
	 */
	public void cmd_wall(CommandSender sender, Material material, boolean aironly) {
		if (!plugin.getConfigValue("FEATURE_WALLBUILDER")) {
			return;
		}
		if (plugin.checkPlayerHasStartedWall((Player)sender)) {
			plugin.removeStartedWall((Player)sender);
			sender.sendMessage(ChatColor.YELLOW+"previous started Wall dismissed.");
		}
		plugin.getStartedWalls().add(new Buildr_Manager_Wallbuilder((Player)sender, material, aironly, plugin));
		String buildinfo ="Started new Wall. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") Aironly: "+ChatColor.BLUE+aironly;
		sender.sendMessage(buildinfo);
		sender.sendMessage("Rightclick on block 1 while holding a stick to continue");
	}

	/**
	 * 
	 * @param sender
	 */
	public void cmd_clrInv(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_CLEAR_INVENTORY")) {
			return;
		}
		((Player)sender).getInventory().clear();
		sender.sendMessage("Inventory cleared");
	}

	/**
	 * 
	 * @param sender
	 */
	public void cmd_location(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_LOCATION")) {
			return;
		}
		Block block = ((Player)sender).getLocation().getBlock();
		String pos = "["+ChatColor.BLUE+block.getX()+ChatColor.WHITE+", "+ChatColor.BLUE+(block.getY()-1)+ChatColor.WHITE+", "+ChatColor.BLUE+block.getZ()+ChatColor.WHITE+"]";
		sender.sendMessage("Coordinates of the block beneath you: "+pos);
		
	}

	public void cmd_allowbuild(CommandSender sender) {
		if (!plugin.getConfigValue("BUILDMODE_REQUIRE_ALLOW")) {
			return;
		}
		World world = ((Player)sender).getWorld();
		if (plugin.checkWorldHasBuildmodeUnlocked(world)) {
			plugin.getWorldBuildmodeAllowed().remove(world);
			for (Player player : world.getPlayers()) {
				if (plugin.checkPlayerBuildMode(player)) {
					plugin.leaveBuildmode(player);
				}
				player.sendMessage(((Player)sender).getName()+" locked the Buildmode in this world");
			}
		}
		else {
			plugin.getWorldBuildmodeAllowed().add(world);
			for (Player player : world.getPlayers()) {
				player.sendMessage(((Player)sender).getName()+" unlocked the Buildmode in this world");
			}
		}
	}
}