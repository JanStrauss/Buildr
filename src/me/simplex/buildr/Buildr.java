package me.simplex.buildr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import me.simplex.buildr.listener.Buildr_Listener_Block;
import me.simplex.buildr.listener.Buildr_Listener_Entity;
import me.simplex.buildr.listener.Buildr_Listener_Player;
import me.simplex.buildr.listener.Buildr_Listener_Weather;
import me.simplex.buildr.manager.Buildr_Manager_Commands;
import me.simplex.buildr.manager.Buildr_Manager_Configuration;
import me.simplex.buildr.manager.Buildr_Manager_Inventory;
import me.simplex.buildr.manager.Buildr_Manager_UndoStack;
import me.simplex.buildr.runnable.Buildr_Runnable_TimeChecker;
import me.simplex.buildr.util.Buildr_Interface_Building;

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
	  
	  private Buildr_Listener_Entity entityListener;
	  private Buildr_Listener_Player playerListener;
	  private Buildr_Listener_Weather weatherListener;
	  private Buildr_Listener_Block blockListener;
	  
	  private Buildr_Manager_Commands cmdHandler;
	  private Buildr_Manager_Inventory invManager;
	  private Buildr_Manager_Configuration cfgManager;
	  private Buildr_Manager_UndoStack unDoStack;

	  private String pluginDirectory;
	  private PluginManager pm;
	  
	  //logic
	  private ArrayList<World> worldBuildMode;
	  private ArrayList<World> worldBuildAllowed;
	  private ArrayList<Player> playerBuildMode;
	  private ArrayList<String> toProcessPlayers;
	  private ArrayList<Buildr_Interface_Building> startedBuildings;
	  
	  private LinkedList<Player> playerCuttingTree;


	@Override
	public void onDisable() {
		importantLog("Buildr v"+version+" stopped.");
	}

	@Override
	public void onEnable() {
		//init
		pm = getServer().getPluginManager();
		
		pluginDirectory 	=  "plugins/Buildr";
		version 			= getDescription().getVersion();
		prefix 				= "[Buildr] ";
		 
		cmdHandler 			= new Buildr_Manager_Commands(this);
		invManager 			= new Buildr_Manager_Inventory(this);
		cfgManager 			= new Buildr_Manager_Configuration(this);
		unDoStack 			= new Buildr_Manager_UndoStack();
		 
		entityListener 		= new Buildr_Listener_Entity(this);
		playerListener 		= new Buildr_Listener_Player(this);
		weatherListener 	= new Buildr_Listener_Weather(this);
		blockListener 		= new Buildr_Listener_Block(this);
		 
		worldBuildMode 		= new ArrayList<World>();
		worldBuildAllowed 	= new ArrayList<World>();
		playerBuildMode 	= new ArrayList<Player>();
		toProcessPlayers 	= new ArrayList<String>();
		startedBuildings 	= new ArrayList<Buildr_Interface_Building>();
		playerCuttingTree 	= new LinkedList<Player>();
		 
		//load settings
		importantLog("Buildr v"+version+" loading..");
			
		if (cfgManager.checkDirectory()) {
			importantLog("created Buildr directory");
		}
		if (!cfgManager.checkConfigFile()) {
			cfgManager.createSettings();
			importantLog("created Buildr Configfile settings.cfg");
		}
		cfgManager.loadSettings();
		importantLog("loaded settings.cfg");

		if (getConfigValue("GENERAL_DISPLAY_SETTINGS_ON_LOAD")) {
			for (String cfg : cfgManager.getSettings().keySet()) {
				importantLog("KEY: "+cfg+" VALUE: "+getConfigValue(cfg));
			}
		}
		if (invManager.startupCheck()) {
			importantLog("created Inventory directory");
		}
		
			setupPermissions();
			
		// check for InventoryStateFile
		if (invManager.checkInventoryStateFile()) {
			importantLog("loading InventoyStateFile..");
			toProcessPlayers.addAll(invManager.loadInventoryStateFile());
			importantLog("found "+toProcessPlayers.size()+" builder(s) to treat on login");
		}

		//register Listener
		pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this); 
		pm.registerEvent(Type.ENTITY_TARGET, entityListener, Event.Priority.Normal, this); 
		pm.registerEvent(Type.ITEM_SPAWN, entityListener, Event.Priority.Normal, this); 
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Normal, this); 
		pm.registerEvent(Type.WEATHER_CHANGE, weatherListener, Event.Priority.Normal, this); 
		pm.registerEvent(Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_TELEPORT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_PORTAL, playerListener, Event.Priority.Normal, this);
		log("Listener registered");
		 
		// TimeThread 
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Buildr_Runnable_TimeChecker(this), 20*30, 20*30);

		log("started TimeThread");
		importantLog("Buildr v"+version+" loaded");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		if (!(sender instanceof Player)) { //disable console 
			return true;
		}
		return cmdHandler.handleCommand(sender, command, args);
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
		if (getConfigValue("GENERAL_DETAILED_LOG")) {
			log.info(prefix+msg);
		}
	}
	
	public void importantLog(String msg){
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
	
	public boolean checkPlayerHasStartedBuilding(Player player){
		for (Buildr_Interface_Building wallbuilder : startedBuildings) {
			if (wallbuilder.getWallcreater() == player) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkWorldHasBuildmodeUnlocked(World world){
		for (World allowed : worldBuildAllowed) {
			if (allowed.equals(world)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkTreecuterFireOnLeaves(Block block) {
		if (block.getType()== Material.LEAVES && getConfigValue("TREECUTTER_ACTIVATE_ON_LEAVES")) {
			return true;
		}
		return false;
	}
	
	public Buildr_Interface_Building giveBuilderManager(Player player){
		for (Buildr_Interface_Building wallbuilder : startedBuildings) {
			if (wallbuilder.getWallcreater() == player) {
				return wallbuilder;
			}
		}
		return null;
	}
	
	public void removeStartedBuilding(Player player){
		for (Buildr_Interface_Building wallbuilder : startedBuildings) {
			if (wallbuilder.getWallcreater() == player) {
				startedBuildings.remove(wallbuilder);
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
		return false;
	}

	public boolean checkPlayerItemInHandIsAxe(Player player) {
		if (player.getItemInHand().getType() == Material.DIAMOND_AXE ||
				player.getItemInHand().getType() == Material.IRON_AXE ||
				player.getItemInHand().getType() == Material.STONE_AXE ||
				player.getItemInHand().getType() == Material.WOOD_AXE) {
				return true;
		}
		return false;
	}
	
	public boolean checkPlayerItemInHandIsStick(Player player) {
		if (player.getItemInHand().getType() == Material.STICK){
				return true;
			}
			else {
				return false;
			}
	}
	
	public void playerClickedBuildingBlock(Player player, Block clickedBlock) {
		if (!checkPlayerHasStartedBuilding(player)) {
			return;
		}
		
		Buildr_Interface_Building wallbuilder = giveBuilderManager(player);
		if (!wallbuilder.isCoordinate1Placed()) {
			wallbuilder.addCoordinate1(clickedBlock);
			player.sendMessage("Got positon 1 of your "+wallbuilder.getBuildingName()+" at ["+ChatColor.BLUE+clickedBlock.getX()+ChatColor.WHITE+", "+ChatColor.BLUE+clickedBlock.getY()+ChatColor.WHITE+", "+ChatColor.BLUE+clickedBlock.getZ()+ChatColor.WHITE+"]");
			player.sendMessage("Now rightclick on block 2 (again with a stick) to continue");
		}
		else {
		player.sendMessage("Got positon 2 of your"+wallbuilder.getBuildingName()+" at ["+ChatColor.BLUE+clickedBlock.getX()+ChatColor.WHITE+", "+ChatColor.BLUE+clickedBlock.getY()+ChatColor.WHITE+", "+ChatColor.BLUE+clickedBlock.getZ()+ChatColor.WHITE+"]");
		wallbuilder.addCoordinate2(clickedBlock);
		if (wallbuilder.checkCoordinates()) {
				player.sendMessage("Positions OK, build "+wallbuilder.getBuildingName()+"..");
				wallbuilder.startBuild();
				removeStartedBuilding(player);
			}
			else {
				removeStartedBuilding(player);
				player.sendMessage(ChatColor.RED+"ERROR: "+ChatColor.WHITE+wallbuilder.getCoordinateCheckFailed());
			}
		}
	}
	
	public void enterBuildmode(Player player) {
			if (getConfigValue("BUILDMODE_REQUIRE_ALLOW")) {
				if (!worldBuildAllowed.contains(player.getWorld())) {
					player.sendMessage("The buildmode is currently locked in the world you're in");
					return;
				}
			}

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
	
	public ArrayList<World> getWorldBuildmodeAllowed() {
		return worldBuildAllowed;
	}


	public ArrayList<Player> getPlayerBuildMode() {
		return playerBuildMode;
	}

	public String getPluginDirectory() {
		return pluginDirectory;
	}

	public Buildr_Manager_UndoStack getUndoList() {
		return unDoStack;
	}
	
	public LinkedList<Player> getPlayerCuttingTree() {
		return playerCuttingTree;
	}

	/**
	 * @return the startedWalls
	 */
	public ArrayList<Buildr_Interface_Building> getStartedBuildings() {
		return startedBuildings;
	}
}
