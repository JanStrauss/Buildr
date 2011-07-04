package me.simplex.buildr;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Buildr extends JavaPlugin {
	  public static PermissionHandler permissionHandler;
	  public Logger log = Logger.getLogger("Minecraft");
	  private String prefix;
	  private String version;
	  private String pluginDirectory;
	  private PluginManager pm;

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onEnable() {
		//init
		 pm = getServer().getPluginManager();
		 version = getDescription().getVersion();
		 prefix = "[Buildr] ";
		 log("Buildr v"+version+" loading..");
		 setupPermissions();
		 
		 //register Listener
		 //TODO
		 
		
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

}
