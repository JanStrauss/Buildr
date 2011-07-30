package me.simplex.buildr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import me.simplex.buildr.listener.Buildr_Listener_Block;
import me.simplex.buildr.listener.Buildr_Listener_Entity;
import me.simplex.buildr.listener.Buildr_Listener_Player;
import me.simplex.buildr.listener.Buildr_Listener_Weather;
import me.simplex.buildr.listener.Buildr_Listener_World;
import me.simplex.buildr.manager.Buildr_Manager_Commands;
import me.simplex.buildr.manager.Buildr_Manager_Configuration;
import me.simplex.buildr.manager.Buildr_Manager_Inventory;
import me.simplex.buildr.manager.Buildr_Manager_UndoStack;
import me.simplex.buildr.runnable.Buildr_Runnable_TimeChecker;
import me.simplex.buildr.util.Buildr_Interface_Building;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
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
	  public Server server;
	  private Logger log = Logger.getLogger("Minecraft");
	  
	  private String prefix;
	  private String version;
	  private String version_cfg;
	  
	  private Buildr_Listener_Entity entityListener;
	  private Buildr_Listener_Player playerListener;
	  private Buildr_Listener_Weather weatherListener;
	  private Buildr_Listener_Block blockListener;
	  private Buildr_Listener_World worldListener;
	  
	  private Buildr_Manager_Commands cmdHandler;
	  private Buildr_Manager_Inventory invManager;
	  private Buildr_Manager_Configuration cfgManager;
	  private Buildr_Manager_UndoStack unDoStack;

	  private String pluginDirectory;
	  private PluginManager pm;
	  
	  private boolean bukkitperms;
	  
	  //logic
	  private ArrayList<World> worldBuildMode;
	  private ArrayList<World> worldBuildAllowed;
	  private ArrayList<Player> playerBuildMode;
	  private ArrayList<String> toProcessPlayers;
	  private ArrayList<Buildr_Interface_Building> startedBuildings;
	  
	  private LinkedList<Player> playerCuttingTree;


	@Override
	public void onDisable() {
		if (!toProcessPlayers.isEmpty()) {
			invManager.updateInventoryStateFile(buildInvStateList());
		}
		importantLog("Buildr v"+version+" stopped.");
	}

	@Override
	public void onEnable() {
		//init
		pm = getServer().getPluginManager();
		
		pluginDirectory 	=  "plugins/Buildr";
		version 			= getDescription().getVersion();
		version_cfg			= version;
		prefix 				= "[Buildr] ";
		
		cmdHandler 			= new Buildr_Manager_Commands(this);
		invManager 			= new Buildr_Manager_Inventory(this);
		cfgManager 			= new Buildr_Manager_Configuration(this);
		unDoStack 			= new Buildr_Manager_UndoStack();

		entityListener 		= new Buildr_Listener_Entity(this);
		playerListener 		= new Buildr_Listener_Player(this);
		weatherListener 	= new Buildr_Listener_Weather(this);
		blockListener 		= new Buildr_Listener_Block(this);
		worldListener		= new Buildr_Listener_World(this);

		worldBuildMode 		= new ArrayList<World>();
		worldBuildAllowed 	= new ArrayList<World>();
		playerBuildMode 	= new ArrayList<Player>();
		toProcessPlayers 	= new ArrayList<String>();
		startedBuildings 	= new ArrayList<Buildr_Interface_Building>();
		playerCuttingTree 	= new LinkedList<Player>();

		//load settings
		importantLog("Buildr v"+version+" loading..");

		Buildr_Manager_Configuration.getMsg("Random");
String VersionF1 = Buildr_Manager_Configuration.getMsg("Version");
String[] VersionF2 = VersionF1.split("}");
String VersionF = VersionF2[0];
if (VersionF.equalsIgnoreCase(version)) {
	
} else {
	
	File oldcon = new File("plugins/Buildr/settings.properties");
	File oldconr = new File("plugins/Buildr/settings_old.properties");
	File oldversion = new File("plugins/Buildr/version.properties");
	oldcon.renameTo(oldconr);
	oldversion.delete();
	Buildr_Manager_Configuration.getMsg("This is to load the getMsg method");
	

	
	
}
	
		if (cfgManager.checkDirectory()) {
			importantLog("created Buildr directory");
		}
		if (!cfgManager.checkConfigFile()) {
			cfgManager.createSettings();
			importantLog("created Buildr Configfile settings.cfg");
		}
		cfgManager.loadSettings();
		cfgManager.checkVersion();
		importantLog("loaded settings.cfg");

		//print settings to console
		if (getConfigValue("GENERAL_DISPLAY_SETTINGS_ON_LOAD")) {
			for (String cfg : cfgManager.getSettings().keySet()) {
				importantLog("KEY: "+cfg+" VALUE: "+getConfigValue(cfg));
			}
		}
		
		//check Inventory directory
		if (invManager.startupCheck()) {
			importantLog("created Inventory directory");
		}

		//permissions
		setupPermissions();
			
		// check for InventoryStateFile
		if (invManager.checkInventoryStateFile()) {
			importantLog("loading InventoyStateFile..");
			toProcessPlayers.addAll(invManager.loadInventoryStateFile());
			importantLog("found "+toProcessPlayers.size()+" builder(s) to treat on login");
		}
		
		//check if /reload and if so remove players from toProcessPlayers list
		Player[] online = getServer().getOnlinePlayers();
		if (online.length > 0) {
			for (Player player : online) {
				handlePlayerOnLogin(player);
			}
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
		pm.registerEvent(Type.WORLD_LOAD, worldListener, Event.Priority.Normal, this);
		log("Listener registered");
		 
		// TimeThread 
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Buildr_Runnable_TimeChecker(this), 20*30, 20*30);
		
		// Check Worlds on Serverstart
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			@Override
			public void run() {
				for(World world : getServer().getWorlds()) {
					String worldString = world.getName();
					if(getConfigValue(worldString)) {
						enterGlobalbuildmode(world);
					}
				}
			}
		});

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
		if (!getConfigValue("GENERAL_USE_BUKKIT_PERMISSIONS")) {
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
		    bukkitperms = false;
		}
		else {
			bukkitperms = true;
			log("will use bukkits build-in permissions");
		}
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
		if (bukkitperms) {
			return player.hasPermission(node);
		}
		else {
			if (permissionHandler!=null) {
				return permissionHandler.has(player, node);
			}
			else {
				return player.isOp();
			}
		}
	}
	
	public boolean checkWorldBuildMode(World world){
		return worldBuildMode.contains(world);
	}
	public boolean checkPlayerBuildMode(Player player){
		return playerBuildMode.contains(player);
	}
	
	public void handlePlayerOnLogin(Player player){
		for (String name : toProcessPlayers) {
			if (name.equals(player.getName())) {
				toProcessPlayers.remove(name);
				if (!playerBuildMode.contains(player)) {
					playerBuildMode.add(player);
				}
				return;
			}
		}
		if (!getConfigValue("BUILDMODE_STAY_AFTER_LOGOUT") && checkPlayerBuildMode(player)) {
			importantLog("Treated "+player.getName()+". Inventory restored.");
			leaveBuildmode(player);
		}
		if (getConfigValue("GLOBALBUILD_FORCE_BUILDMODE") && checkWorldBuildMode(player.getWorld())) {
			enterBuildmode(player);
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
			
		if (getPlayerBuildMode().contains(player)) {
			return;
		}
		
		getPlayerBuildMode().add(player);
		if (getConfigValue("BUILDMODE_INVENTORY")) {
			invManager.switchInventory(player);
			invManager.updateInventoryStateFile(buildInvStateList());
		}
		
	}
	
	public void leaveBuildmode(Player player) {
		boolean wasIn = getPlayerBuildMode().remove(player);
		if (wasIn) {
			if (getConfigValue("BUILDMODE_INVENTORY")) {
				invManager.switchInventory(player);
				if (playerBuildMode.isEmpty()) {
					invManager.updateInventoryStateFile(null);
				}
				else {
					invManager.updateInventoryStateFile(buildInvStateList());
				}
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
		
		if (getConfigValue("GLOBALBUILD_FORCE_BUILDMODE")) {
			for (Player player : world.getPlayers()) {
				enterBuildmode(player);
				player.sendMessage(ChatColor.GOLD+"You have been forced to use buildmode.");
			}
		}

	}
	
	public void leaveGlobalbuildmode(World world) {
		getWorldBuildMode().remove(world);
	}
	
	private ArrayList<String> buildInvStateList(){
		ArrayList<String> toProcess = toProcessPlayers;
		for (Player player : playerBuildMode) {
			if (!toProcess.contains(player.getName())) {
				toProcess.add(player.getName());
			}
		}
		return toProcess;
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

	public ArrayList<Buildr_Interface_Building> getStartedBuildings() {
		return startedBuildings;
	}

	public String getCurrentConfig() {
		return version_cfg;
	}
	
	
}
