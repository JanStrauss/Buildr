/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Buildr.
 * 
 * Buildr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Buildr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Buildr  If not, see <http://www.gnu.org/licenses/>.
 */
package me.simplex.buildr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

import me.simplex.buildr.listener.Buildr_Listener_Block;
import me.simplex.buildr.listener.Buildr_Listener_Entity;
import me.simplex.buildr.listener.Buildr_Listener_Player;
import me.simplex.buildr.listener.Buildr_Listener_Weather;
import me.simplex.buildr.listener.Buildr_Listener_World;
import me.simplex.buildr.manager.Buildr_Manager_Configuration;
import me.simplex.buildr.manager.Buildr_Manager_UndoStack;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Airfloor;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Allowbuild;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Build;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_ClearInv;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Cuboid;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Cylinder;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Globalbuild;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Gv;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Gvx;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Halfsphere;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Jump;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Location;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Sphere;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Top;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Undo;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Wall;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Wallx;
import me.simplex.buildr.manager.commands.Buildr_Manager_Command_Wool;
import me.simplex.buildr.runnable.Buildr_Runnable_TimeChecker;
import me.simplex.buildr.util.Buildr_Interface_Building;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Buildr extends JavaPlugin {
	
	  //tech
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
	  
	  private Buildr_Manager_Configuration cfgManager;
	  private Buildr_Manager_UndoStack unDoStack;
	  
	  private Buildr_Manager_Command_Airfloor cmdAirfloor;
	  private Buildr_Manager_Command_Allowbuild cmdAllowbuild;
	  private Buildr_Manager_Command_Build cmdBuild;
	  private Buildr_Manager_Command_ClearInv cmdClearInv;
	  private Buildr_Manager_Command_Cuboid cmdCuboid;
	  private Buildr_Manager_Command_Cylinder cmdCylinder;
	  private Buildr_Manager_Command_Gv cmdGive;
	  private Buildr_Manager_Command_Gvx cmdGivex;
	  private Buildr_Manager_Command_Globalbuild cmdGlobalbuild;
	  private Buildr_Manager_Command_Halfsphere cmdHalfsphere;
	  private Buildr_Manager_Command_Jump cmdJump;
	  private Buildr_Manager_Command_Location cmdLocation;
	  private Buildr_Manager_Command_Top cmdTop;
	  private Buildr_Manager_Command_Undo cmdUndo;
	  private Buildr_Manager_Command_Sphere cmdSphere;
	  private Buildr_Manager_Command_Wall cmdWall;
	  private Buildr_Manager_Command_Wallx cmdWallx;
	  private Buildr_Manager_Command_Wool cmdWool;

	  private String pluginDirectory;
	  
	  private PluginManager pm;
	  
	  //logic
	  private ArrayList<World> worldBuildMode;
	  private ArrayList<World> worldBuildAllowed;
	  private ArrayList<Player> playerBuildMode;
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
		version_cfg			= "0.7.1";
		prefix 				= "[Buildr] ";
		
		cfgManager 			= new Buildr_Manager_Configuration(this);
		unDoStack 			= new Buildr_Manager_UndoStack();

		entityListener 		= new Buildr_Listener_Entity(this);
		playerListener 		= new Buildr_Listener_Player(this);
		weatherListener 	= new Buildr_Listener_Weather(this);
		blockListener 		= new Buildr_Listener_Block(this);
		worldListener		= new Buildr_Listener_World(this);
		
		cmdAirfloor 		= new Buildr_Manager_Command_Airfloor(this);
		cmdAllowbuild 		= new Buildr_Manager_Command_Allowbuild(this);
		cmdBuild 			= new Buildr_Manager_Command_Build(this);
		cmdClearInv 		= new Buildr_Manager_Command_ClearInv(this);
		cmdCuboid 			= new Buildr_Manager_Command_Cuboid(this);
		cmdCylinder 		= new Buildr_Manager_Command_Cylinder(this);
		cmdGive 			= new Buildr_Manager_Command_Gv(this);
		cmdGivex 			= new Buildr_Manager_Command_Gvx(this);
		cmdGlobalbuild 		= new Buildr_Manager_Command_Globalbuild(this);
		cmdHalfsphere 		= new Buildr_Manager_Command_Halfsphere(this);
		cmdJump 			= new Buildr_Manager_Command_Jump(this);
		cmdLocation 		= new Buildr_Manager_Command_Location(this);
		cmdTop 				= new Buildr_Manager_Command_Top(this);
		cmdUndo 			= new Buildr_Manager_Command_Undo(this);
		cmdSphere 			= new Buildr_Manager_Command_Sphere(this);
		cmdWall 			= new Buildr_Manager_Command_Wall(this);
		cmdWallx 			= new Buildr_Manager_Command_Wallx(this);
		cmdWool 			= new Buildr_Manager_Command_Wool(this);

		worldBuildMode 		= new ArrayList<World>();
		worldBuildAllowed 	= new ArrayList<World>();
		playerBuildMode 	= new ArrayList<Player>();
		startedBuildings 	= new ArrayList<Buildr_Interface_Building>();
		playerCuttingTree 	= new LinkedList<Player>();
	
		if (cfgManager.checkDirectory()) {
			importantLog("created Buildr directory..");
		}
		if (!cfgManager.checkConfigFile()) {
			cfgManager.createSettings();
			importantLog("created configuration file..");
		}
		cfgManager.loadSettings();
		cfgManager.checkVersion();
		importantLog("loaded settings..");

		//print settings to console
		if (getConfigValue("GENERAL_DISPLAY_SETTINGS_ON_LOAD")) {
			for (String cfg : cfgManager.getSettings().keySet()) {
				importantLog("KEY: "+cfg+" VALUE: "+getConfigValue(cfg));
			}
		}
		
		//permissions
		setupPermissions();
		
		//register Listener
		pm.registerEvents(worldListener, this);
		pm.registerEvents(weatherListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(playerListener, this);
		pm.registerEvents(blockListener, this);
		
		log("Listener registered..");
		
		//set command executors
		getCommand("airfloor").setExecutor(cmdAirfloor);
		getCommand("allowbuild").setExecutor(cmdAllowbuild);
		getCommand("build").setExecutor(cmdBuild);
		getCommand("clearinv").setExecutor(cmdClearInv);
		getCommand("cuboid").setExecutor(cmdCuboid);
		getCommand("cylinder").setExecutor(cmdCylinder);
		getCommand("gv").setExecutor(cmdGive);
		getCommand("gvx").setExecutor(cmdGivex);
		getCommand("globalbuild").setExecutor(cmdGlobalbuild);
		getCommand("halfsphere").setExecutor(cmdHalfsphere);
		getCommand("jump").setExecutor(cmdJump);
		getCommand("location").setExecutor(cmdLocation);
		getCommand("sphere").setExecutor(cmdSphere);
		getCommand("top").setExecutor(cmdTop);
		getCommand("undo").setExecutor(cmdUndo);
		getCommand("wall").setExecutor(cmdWall);
		getCommand("wallx").setExecutor(cmdWallx);
		getCommand("wool").setExecutor(cmdWool);
		log("command executors set..");
		
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
	
	private void setupPermissions() {
		log("using bukkitperms");
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
		if (!getConfigValue("GENERAL_USE_PERMISSIONS")) {
			return player.isOp();
		}
		return player.hasPermission(node);
	}
	
	public boolean checkWorldBuildMode(World world){
		return worldBuildMode.contains(world);
	}
	public boolean checkPlayerBuildMode(Player player){
		return playerBuildMode.contains(player);
	}
	
	public void handlePlayerOnLogin(Player player){
		//System.out.println("BM_STAY_AFTR_LGOUT"+getConfigValue("BUILDMODE_STAY_AFTER_LOGOUT"));
		if (!getConfigValue("BUILDMODE_STAY_AFTER_LOGOUT") && checkPlayerBuildMode(player)) {
			importantLog("Treated "+player.getName()+". Inventory restored.");
			leaveBuildmode(player);
			//System.out.println("leave bm");
		}
		if (getConfigValue("GLOBALBUILD_FORCE_BUILDMODE") && checkWorldBuildMode(player.getWorld())) {
			enterBuildmode(player);
			//System.out.println("enter bm");
		}
	}
	
	public boolean checkPlayerHasStartedBuilding(Player player){
		for (Buildr_Interface_Building wallbuilder : startedBuildings) {
			if (wallbuilder.getBuildingcreater() == player) {
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
		for (Buildr_Interface_Building builder : startedBuildings) {
			if (builder.getBuildingcreater() == player) {
				return builder;
			}
		}
		return null;
	}
	
	public void removeStartedBuilding(Player player){
		for (Buildr_Interface_Building wallbuilder : startedBuildings) {
			if (wallbuilder.getBuildingcreater() == player) {
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
		return false;
	}
	
	public boolean playerClickedBuildingBlock(Player player, Block clickedBlock) {
		if (!checkPlayerHasStartedBuilding(player)) {
			return false;
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
		return true;
	}
	
	public void enterBuildmode(Player player) {
			if (getConfigValue("BUILDMODE_REQUIRE_ALLOW")) {
				if (!worldBuildAllowed.contains(player.getWorld())) {
					player.sendMessage("The buildmode is currently locked in the world you're in");
					return;
				}
			}
			
		if (playerBuildMode.contains(player)) {
			return;
		}
		
		
		if (getConfigValue("BUILDMODE_TOGGLE_GAMEMODE")) {
			player.setGameMode(GameMode.CREATIVE);
		}
		
		getPlayerBuildMode().add(player);
	}
	
	public void leaveBuildmode(Player player) {
		if (getConfigValue("BUILDMODE_TOGGLE_GAMEMODE")) {
			player.setGameMode(GameMode.SURVIVAL);
		}
		getPlayerBuildMode().remove(player);
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
