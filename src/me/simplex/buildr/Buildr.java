package me.simplex.buildr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import me.simplex.buildr.listener.Buildr_BlockListener;
import me.simplex.buildr.listener.Buildr_EntityListener;
import me.simplex.buildr.listener.Buildr_PlayerListener;
import me.simplex.buildr.listener.Buildr_WeatherListener;
import me.simplex.buildr.util.Buildr_UnDoStack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Buildr extends JavaPlugin {
	
	  //tech
	  public static PermissionHandler permissionHandler;
	  private Logger log = Logger.getLogger("Minecraft");
	  
	  private String prefix;
	  private String version;
	  
	  private Buildr_EntityListener entityListener;
	  private Buildr_PlayerListener playerListener;
	  private Buildr_WeatherListener weatherListener;
	  private Buildr_BlockListener blockListener;
	  
	  private Buildr_Commands cmdHandler;
	  private Buildr_InventoryManager invManager;
	  private Buildr_ConfigurationManager cfgManager;
	  private Buildr_UnDoStack unDoStack;

	  private Thread thread;
	  private Buildr_TimeThread timeHandler;
	  private String pluginDirectory;
	  private PluginManager pm;
	  
	  //logic
	  private ArrayList<World> worldBuildMode;
	  private ArrayList<Player> playerBuildMode;
	  private ArrayList<String> toProcessPlayers;
	  private ArrayList<Buildr_Wallbuilder> startedWalls;


	@Override
	public void onDisable() {
		log("Shutdown..");
		timeHandler.setAlive(false);
		log("Buildr v"+version+" stopped.");
	}

	@Override
	public void onEnable() {
		//init
		pm = getServer().getPluginManager();
		
		pluginDirectory 	=  "plugins/Buildr";
		version 			= getDescription().getVersion();
		prefix 				= "[Buildr] ";
		 
		cmdHandler 			= new Buildr_Commands(this);
		invManager 			= new Buildr_InventoryManager(this);
		cfgManager 			= new Buildr_ConfigurationManager(this);
		unDoStack 			= new Buildr_UnDoStack();
		 
		entityListener 		= new Buildr_EntityListener(this);
		playerListener 		= new Buildr_PlayerListener(this);
		weatherListener 	= new Buildr_WeatherListener(this);
		blockListener 		= new Buildr_BlockListener(this);
		 
		worldBuildMode 		= new ArrayList<World>();
		playerBuildMode 	= new ArrayList<Player>();
		toProcessPlayers 	= new ArrayList<String>();
		startedWalls 		= new ArrayList<Buildr_Wallbuilder>();
		 
		//load settings
		log("Buildr v"+version+" loading..");
			
		if (cfgManager.checkDirectory()) {
			log("created Buildr directory");
		}
		if (!cfgManager.checkConfigFile()) {
			cfgManager.createSettings();
			log("created Buildr Configfile settings.cfg");
		}
		cfgManager.loadSettings();
		log("loaded settings.cfg");

		if (getConfigValue("GENERAL_DISPLAY_SETTINGS_ON_LOAD")) {
			for (String cfg : cfgManager.getSettings().keySet()) {
				log("KEY: "+cfg+" VALUE: "+getConfigValue(cfg));
			}
		}
		if (invManager.startupCheck()) {
			log("created Inventory directory");
		}
		
			setupPermissions();
			
		// check for InventoryStateFile
		if (invManager.checkInventoryStateFile()) {
			log("loading InventoyStateFile..");
			toProcessPlayers.addAll(invManager.loadInventoryStateFile());
			log("found "+playerBuildMode.size()+" builder(s) to treat on login");
		}

		//register Listener
		pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this); // Godmode: no dmg
		pm.registerEvent(Type.ENTITY_TARGET, entityListener, Event.Priority.Normal, this); // Godmode: no aggro
		pm.registerEvent(Type.ITEM_SPAWN, entityListener, Event.Priority.Normal, this); // No Blockdrops
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this); // Instant Blockbreak
		pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Normal, this); // No Pickups
		pm.registerEvent(Type.WEATHER_CHANGE, weatherListener, Event.Priority.Normal, this); // Always Sun
		pm.registerEvent(Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this); // Unlimited Stacks
		pm.registerEvent(Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		
		log("Listener registered");


		 
		// TimeThread 
		timeHandler = new Buildr_TimeThread(this);
		thread = new Thread(timeHandler,prefix+"Time Handler");
		thread.start();

		log("started TimeThread");
		log("Buildr v"+version+" loaded");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		if (!(sender instanceof Player)) { //disable console 
			return true;
		}
		
		//GLOBALBUILD
		if (command.getName().equalsIgnoreCase("globalbuild")) {
			if (checkPermission((Player)sender, "buildr.cmd.globalbuild")) {
				World world;
				if (args.length > 0) {
					world = getServer().getWorld(args[0]);
				}
				else {
					world = ((Player)sender).getWorld();
				}
				if (world != null) {
					cmdHandler.cmd_globalbuild(sender, world);
				}
				else {
					sender.sendMessage(ChatColor.RED+"There is no world with this name");
				}
				return true;
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
				return true;
			}
			
		}
		
		//BUILD
		else if (command.getName().equalsIgnoreCase("build")) {
			if (checkPermission((Player)sender, "buildr.cmd.build")) {
				cmdHandler.cmd_build(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//TOP
		else if (command.getName().equalsIgnoreCase("top")) {
			if (checkPermission((Player)sender, "buildr.cmd.top")) {
				cmdHandler.cmd_top(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		// AIRFLOOR
		else if (command.getName().equalsIgnoreCase("airfloor")) {
			if (checkPermission((Player)sender, "buildr.cmd.airfloor")) {
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
					try {
						material = Math.abs(Integer.parseInt(args[0]));
					} catch (NumberFormatException e) {
						try {
							material = Material.matchMaterial(args[0]).getId();
						} catch (NullPointerException e2) {
							sender.sendMessage(ChatColor.RED+"wrong format");
							return true;
						}
					}
					if (Material.getMaterial(material)== null) {
						sender.sendMessage("Invalid Material");
						return true;
					}
					cmdHandler.cmd_airfloor(sender, material, height, size);
					} 
				catch (NumberFormatException e) {
					sender.sendMessage("Wrong format, usage: /airfloor <material> <height> <size>");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//UNDO
		else if (command.getName().equalsIgnoreCase("undo")) {
			if (checkPermission((Player)sender, "buildr.cmd.undo")) {
				cmdHandler.cmd_undo(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//GIVE
		else if (command.getName().equalsIgnoreCase("give")) {
			if (checkPermission((Player)sender, "buildr.cmd.give")) {
				if (args.length==0) {
					return false;
				}
				if (args.length==1) {
					cmdHandler.cmd_give(sender,args[0],-1,null);
				}
				else if (args.length==2) {
					int amount = -1;
					try {
						amount = Integer.parseInt(args[1]);
						if (amount >64) {
							amount = 64;
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED+"wrong format");
						return true;
					}
					cmdHandler.cmd_give(sender,args[0],amount,null);
				}
				else if(args.length==3) {
					int amount = -1;
					try {
						amount = Integer.parseInt(args[2]);
						if (amount >64) {
							amount = 64;
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED+"wrong format");
						return true;
					}
					cmdHandler.cmd_give(sender,args[1],amount,args[0]);
				}
				
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//WOOL
		else if (command.getName().equalsIgnoreCase("wool")) {
			if (checkPermission((Player)sender, "buildr.cmd.wool")) {
				if (args.length!=1) {
					return false;
				}
				cmdHandler.cmd_wool(sender, args[0]);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//ClearInv
		else if (command.getName().equalsIgnoreCase("clearinv")) {
			if (checkPermission((Player)sender, "buildr.cmd.clearinv")) {
				if (args.length!=0) {
					return false;
				}
				cmdHandler.cmd_clrInv(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//Wall
		else if (command.getName().equalsIgnoreCase("wall")) {
			System.out.println(args.length);
			if (checkPermission((Player)sender, "buildr.cmd.wall")) {
				if (args.length >=1 && args.length <=2) {
					Material material;
					int id;
					try {
						id = Integer.parseInt(args[0]);
					} catch (NumberFormatException e) {
						try {
							id = Material.matchMaterial(args[0]).getId();
						} catch (NullPointerException e2) {
							sender.sendMessage(ChatColor.RED+"wrong format");
							return true;
						}

					}
					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sender.sendMessage(ChatColor.RED+"unvalid blocktype");
						return true;
					}
					if (args.length==1) {
							cmdHandler.cmd_wall(sender, material, false);
							return true;
					}
					else if (args.length==2) {
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							cmdHandler.cmd_wall(sender, material, true);
							return true;
						}
						else {
							return false;
						}
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//ELSE
		else {
			return false;
		}
	}
	
	private void setupPermissions() {
	    if (permissionHandler != null) {
	        return;
	    }
	    
	    Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
	    
	    if (permissionsPlugin == null) {
	        log("Permission system not detected, defaulting to OP");
	        return;
	    }
		if (!getConfigValue("GENERAL_USE_PERMISSIONS")) {
	        log("Permission system disabled, using OP");
	        return;
		}
	    
	    permissionHandler = ((Permissions) permissionsPlugin).getHandler();
	    log("Found and will use plugin "+((Permissions)permissionsPlugin).getDescription().getFullName());
	}
	
	public void log(String msg){
		log.info(prefix+msg);
	}
	
	public boolean checkPermission(Player player, String node){
		if (permissionHandler!=null) {
			return permissionHandler.has(player, node);
		}
		else {
			return player.isOp();
		}
	}
	
	public boolean checkWorldBuildMode(World world){
		return worldBuildMode.contains(world);
	}
	public boolean checkPlayerBuildMode(Player player){
		return playerBuildMode.contains(player);
	}
	
	public void checkPlayerIsToProcess(Player player){
		for (String name : toProcessPlayers) {
			if (name.equals(player.getName())) {
				toProcessPlayers.remove(name);
				if (!playerBuildMode.contains(player)) {
					playerBuildMode.add(player);
				}
				return;
			}
		}
	}
	
	public boolean checkPlayerHasStartedWall(Player player){
		for (Buildr_Wallbuilder wallbuilder : startedWalls) {
			if (wallbuilder.getWallcreater() == player) {
				return true;
			}
		}
		return false;
	}
	
	public Buildr_Wallbuilder giveWallbuilder(Player player){
		for (Buildr_Wallbuilder wallbuilder : startedWalls) {
			if (wallbuilder.getWallcreater() == player) {
				return wallbuilder;
			}
		}
		return null;
	}
	
	public void removeStartedWall(Player player){
		for (Buildr_Wallbuilder wallbuilder : startedWalls) {
			if (wallbuilder.getWallcreater() == player) {
				startedWalls.remove(wallbuilder);
				return;
			}
		}
	}
	
	public boolean checkPlayerItemInHandIsPickaxe(Player player){
		if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE ||
			player.getItemInHand().getType() == Material.IRON_PICKAXE ||
			player.getItemInHand().getType() == Material.STONE_PICKAXE ||
			player.getItemInHand().getType() == Material.WOOD_PICKAXE) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean checkPlayerItemInHandIsStick(Player player) {
		if (player.getItemInHand().getType() == Material.STICK){
				return true;
			}
			else {
				return false;
			}
	}
	
	public void playerClickedWallBlock(Player player, Block clickedBlock) {
		if (!checkPlayerHasStartedWall(player)) {
			return;
		}
		Buildr_Wallbuilder wallbuilder = giveWallbuilder(player);
		if (!wallbuilder.isCoordinate1placed()) {
			wallbuilder.addCoordinate1(clickedBlock);
			player.sendMessage("Got positon 1 of your wall at ["+ChatColor.BLUE+clickedBlock.getX()+ChatColor.WHITE+", "+ChatColor.BLUE+clickedBlock.getY()+ChatColor.WHITE+", "+ChatColor.BLUE+clickedBlock.getZ()+ChatColor.WHITE+"]");
			player.sendMessage("Now rightclick on block 2 (again with a stick) to continue");
		}
		else {
		player.sendMessage("Got positon 2 of your wall at ["+ChatColor.BLUE+clickedBlock.getX()+ChatColor.WHITE+", "+ChatColor.BLUE+clickedBlock.getY()+ChatColor.WHITE+", "+ChatColor.BLUE+clickedBlock.getZ()+ChatColor.WHITE+"]");
			if (wallbuilder.checkCoordinates(clickedBlock)) {
				player.sendMessage("Positions OK, build wall..");
				HashMap<Block, Material> undo = wallbuilder.startBuild();
				getUndoList().addToStack(undo, player);
				player.sendMessage("done! Placed "+undo.size()+" blocks");
				removeStartedWall(player);
			}
			else {
				removeStartedWall(player);
				player.sendMessage(ChatColor.RED+"ERROR: "+ChatColor.WHITE+"Atleast one dimension of the blocks must be the same. Wallbuild stopped.");
			}
		}
	}
	
	public void enterBuildmode(Player player) {
		getPlayerBuildMode().add(player);
		if (getConfigValue("BUILDMODE_INVENTORY")) {
			invManager.switchInventory(player);
			invManager.updateInventoryStateFile(playerBuildMode);
		}
		
	}
	
	public void leaveBuildmode(Player player) {
		getPlayerBuildMode().remove(player);
		if (getConfigValue("BUILDMODE_INVENTORY")) {
			invManager.switchInventory(player);
			if (playerBuildMode.isEmpty()) {
				invManager.updateInventoryStateFile(null);
			}
			else {
				invManager.updateInventoryStateFile(playerBuildMode);
			}
		}
	}
	
	public void enterGlobalbuildmode(World world) {
		getWorldBuildMode().add(world);
		
		if (getConfigValue("GLOBALBUILD_WEATHER")) {
			world.setStorm(false);
			world.setThundering(false);
		}

		if (getConfigValue("GLOBALBUILD_TIME")) {
			world.setTime(0);
		}

	}
	public void leaveGlobalbuildmode(World world) {
		getWorldBuildMode().remove(world);
	}
	
	//get+set 
	
	/**
	 * @return the settings HashMap of Buildr
	 */

	public boolean getConfigValue(String key){
		return cfgManager.getConfigValue(key);
	}
	
	public ArrayList<World> getWorldBuildMode() {
		return worldBuildMode;
	}


	public ArrayList<Player> getPlayerBuildMode() {
		return playerBuildMode;
	}

	public String getPluginDirectory() {
		return pluginDirectory;
	}

	public Buildr_UnDoStack getUndoList() {
		return unDoStack;
	}

	/**
	 * @return the startedWalls
	 */
	public ArrayList<Buildr_Wallbuilder> getStartedWalls() {
		return startedWalls;
	}




	

}
