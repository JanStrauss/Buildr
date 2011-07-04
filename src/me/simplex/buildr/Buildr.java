package me.simplex.buildr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

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
	  public Buildr_EntityListener EntityListener;
	  public Logger log = Logger.getLogger("Minecraft");
	  private String prefix;
	  private String version;
	  private String pluginDirectory;
	  private PluginManager pm;
	  
	  //logic
	  private ArrayList<World> worldbuildmode;
	  private ArrayList<Player> playerbuildmode;

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onEnable() {
		//init
		 pm = getServer().getPluginManager();
		 EntityListener = new Buildr_EntityListener(this);
		 version = getDescription().getVersion();
		 prefix = "[Buildr] ";
		 
		 
		 worldbuildmode =  new ArrayList<World>();
		 playerbuildmode = new ArrayList<Player>();
		 
		 //load
		 log("Buildr v"+version+" loading..");
		 setupPermissions();
		 
		 //register Listener
		 //TODO
		 pm.registerEvent(Type.ENTITY_DAMAGE, EntityListener, Event.Priority.Normal, this);
		 pm.registerEvent(Type.ENTITY_TARGET, EntityListener, Event.Priority.Normal, this);
		 pm.registerEvent(Type.ITEM_SPAWN, EntityListener, Event.Priority.Normal, this);
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

}
