package me.simplex.buildr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import me.simplex.buildr.listener.Buildr_BlockListener;
import me.simplex.buildr.listener.Buildr_EntityListener;
import me.simplex.buildr.listener.Buildr_PlayerListener;
import me.simplex.buildr.listener.Buildr_WeatherListener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
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
	  private Buildr_ConfigManager cfgManager;

	  private Thread thread;
	  private Buildr_TimeThread timeHandler;
	  private String pluginDirectory;
	  private PluginManager pm;
	  
	  //logic
	  private HashMap<String, Object> settings;
	  private ArrayList<World> worldbuildmode;
	  private ArrayList<Player> playerbuildmode;


	@Override
	public void onDisable() {
		timeHandler.setAlive(false);
	}

	@Override
	public void onEnable() {
		//init
		pm = getServer().getPluginManager();
		 
		cmdHandler =  new Buildr_Commands(this);
		invManager = new Buildr_InventoryManager(this);
		cfgManager = new Buildr_ConfigManager(this);
		 
		entityListener = new Buildr_EntityListener(this);
		playerListener = new Buildr_PlayerListener(this);
		weatherListener = new Buildr_WeatherListener(this);
		blockListener = new Buildr_BlockListener(this);
		 
		pluginDirectory =  "plugins/Buildr";
		version = getDescription().getVersion();
		prefix = "[Buildr] ";
		 
		worldbuildmode =  new ArrayList<World>();
		playerbuildmode = new ArrayList<Player>();
		 
		//load settings
		log("Buildr v"+version+" loading..");
		setupPermissions();
		
		if (cfgManager.checkDirectory()) {
			log("created Buildr directory");
		}
		if (!cfgManager.checkConfigFile()) {
			cfgManager.createSettings();
			log("created Buildr Configfile settings.cfg");
		}
		this.settings = cfgManager.loadSettings();
		log("loaded settings.cfg");
		if ((Boolean)settings.get("SPAM_ON_STARTUP")) {
			//TODO:
			log("printing detailed config:");
			log("...");
			log("...");
			log("...");
		}

		//register Listener
		pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this); // Godmode: no dmg
		pm.registerEvent(Type.ENTITY_TARGET, entityListener, Event.Priority.Normal, this); // Godmode: no aggro
		pm.registerEvent(Type.ITEM_SPAWN, entityListener, Event.Priority.Normal, this); // No Blockdrops
		pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this); // Instant Blockbreak
		pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Normal, this); // No Pickups
		pm.registerEvent(Type.WEATHER_CHANGE, weatherListener, Event.Priority.Normal, this); // Always Sun
		pm.registerEvent(Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this); // Unlimited Stacks
		if ((Boolean)settings.get("SPAM_ON_STARTUP")) {
			log("Listener registered");
		}

		 
		// TimeThread
		timeHandler = new Buildr_TimeThread(this);
		thread = new Thread(timeHandler,prefix+"Time Handler");
		thread.start();
		if ((Boolean)settings.get("SPAM_ON_STARTUP")) {
			log("started TimeThread");
		}
		log("Buildr v"+version+" loaded");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		if (!(sender instanceof Player)) { //disable console 
			return true;
		}
		
		if (command.getName().equals("globalbuild")) {
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
				sender.sendMessage(ChatColor.RED+"You dont have the Permission to perform this action");
				return true;
			}
			
		}
		else if (command.getName().equals("build")) {
			if (checkPermission((Player)sender, "buildr.cmd.build")) {
				cmdHandler.cmd_build(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the Permission to perform this action");
			}
			return true;
		}
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
		return worldbuildmode.contains(world);
	}
	public boolean checkPlayerBuildMode(Player player){
		return playerbuildmode.contains(player);
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
	
	
	public void enterBuildmode(Player player) {
		getPlayerbuildmode().add(player);
		invManager.switchInventory(player);
	}
	
	public void leaveBuildmode(Player player) {
		getPlayerbuildmode().remove(player);
		invManager.switchInventory(player);
	}
	
	public void enterGlobalbuildmode(World world) {
		getWorldbuildmode().add(world);
		world.setStorm(false);
		world.setThundering(false);
		world.setWeatherDuration(0);
		world.setTime(0);
	}
	public void leaveGlobalbuildmode(World world) {
		getWorldbuildmode().remove(world);
	}
	
	//get+set 
	
	/**
	 * @return the settings HashMap of Buildr
	 */
	public HashMap<String, Object> getSettings() {
		return settings;
	}

	public ArrayList<World> getWorldbuildmode() {
		return worldbuildmode;
	}

	public void setWorldbuildmode(ArrayList<World> worldbuildmode) {
		this.worldbuildmode = worldbuildmode;
	}

	public ArrayList<Player> getPlayerbuildmode() {
		return playerbuildmode;
	}

	public void setPlayerbuildmode(ArrayList<Player> playerbuildmode) {
		this.playerbuildmode = playerbuildmode;
	}

	public String getPluginDirectory() {
		return pluginDirectory;
	}
	

}
