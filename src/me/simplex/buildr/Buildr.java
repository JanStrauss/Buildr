package me.simplex.buildr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

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
	  public Logger log = Logger.getLogger("Minecraft");
	  
	  private String prefix;
	  private String version;
	  
	  private Buildr_EntityListener entityListener;
	  private Buildr_PlayerListener playerListener;
	  private Buildr_WeatherListener weatherListener;

	  private Thread thread;
	  private Buildr_TimeHandleThread timeHandler;
	  //private String pluginDirectory;
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
		 entityListener = new Buildr_EntityListener(this);
		 playerListener = new Buildr_PlayerListener(this);
		 weatherListener = new Buildr_WeatherListener();
		 version = getDescription().getVersion();
		 prefix = "[Buildr] ";
		 
		 worldbuildmode =  new ArrayList<World>();
		 playerbuildmode = new ArrayList<Player>();
		 
		 //load
		 log("Buildr v"+version+" loading..");
		 setupPermissions();
		 
		 //register Listener
		 //TODO
		 pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this); // Godmode: no dmg
		 pm.registerEvent(Type.ENTITY_TARGET, entityListener, Event.Priority.Normal, this); // Godmode: no aggro
		 pm.registerEvent(Type.ITEM_SPAWN, entityListener, Event.Priority.Normal, this); // No Blockdrops
		 pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this); // Instant Blockbreak
		 pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Normal, this); // No Pickups
		 pm.registerEvent(Type.WEATHER_CHANGE, weatherListener, Event.Priority.Normal, this); // Always Sun
		 
		 // TimeThread
		 timeHandler = new Buildr_TimeHandleThread(this);
		 thread = new Thread(timeHandler,prefix+"Time Handler");
	     thread.start();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,String label, String[] args) {
		// TODO Auto-generated method stub
		return super.onCommand(sender, command, label, args);
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
	
	protected void log(String msg){
		log.info(prefix+msg);
	}
	
	protected boolean checkWorldBuildMode(World world){
		return worldbuildmode.contains(world);
	}
	protected boolean checkPlayerBuildMode(Player player){
		return playerbuildmode.contains(player);
	}
	
	protected boolean checkPlayerItemInHandIsPickaxe(Player player){
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
}
