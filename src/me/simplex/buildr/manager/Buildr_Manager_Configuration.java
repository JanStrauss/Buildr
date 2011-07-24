package me.simplex.buildr.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import me.simplex.buildr.Buildr;

public class Buildr_Manager_Configuration {
	private Buildr plugin;
	public static File configfile;
	private HashMap<String, Boolean> settings;
	
	public Buildr_Manager_Configuration(Buildr buildr){
		this.plugin = buildr;
		Buildr_Manager_Configuration.configfile = new File(plugin.getPluginDirectory()+File.separator+"settings.properties");
		this.settings = new HashMap<String, Boolean>();
	}
	
	public boolean checkDirectory(){
		return new File(plugin.getPluginDirectory()).mkdir();
    }
	
	public boolean checkConfigFile(){
		return configfile.exists();
    }
	
	public void loadSettings(){
			try{
				FileInputStream inputStrm = new FileInputStream(configfile.getAbsoluteFile());
				InputStreamReader inptStrmRdr = new InputStreamReader(inputStrm);
				BufferedReader bffrdRdr = new BufferedReader(inptStrmRdr);
				while(true){
					String line = bffrdRdr.readLine();
					if(line==null){
						break;
					}
					if(!line.startsWith("#") && !line.isEmpty()){
						String linecont[] = line.split("=");
						if(linecont.length==2){
							String key = linecont[0];
							String value = linecont[1];
							settings.put(key, Boolean.valueOf(value));
						}
					}
				}
				bffrdRdr.close();
			} catch(IOException e){
				e.printStackTrace();
			}
	}
	
	
	public void createSettings(){
		try {
			FileWriter stream = new FileWriter(configfile);
			BufferedWriter writer = new BufferedWriter(stream);
			writer.write("############################################################");writer.newLine();
			writer.write("#                                                          #");writer.newLine();
			writer.write("#                    BUILDR CONFIGURATION                  #");writer.newLine();
			writer.write("#                                                          #");writer.newLine();
			writer.write("#                            v0.1                          #");writer.newLine();
			writer.write("############################################################");writer.newLine();
			writer.newLine();
			writer.write("##### General settings: ####################################");writer.newLine();
			writer.newLine();
			writer.write("# Print the settings while Buildr loads");writer.newLine();//done
			writer.write("GENERAL_DISPLAY_SETTINGS_ON_LOAD=false");writer.newLine(); 
			writer.newLine();
			writer.write("# Enable Permissions, if false Buildr checks for OP (will also check for OP if no PermissionsPlugin was found)");writer.newLine();
			writer.write("GENERAL_USE_PERMISSIONS=true");writer.newLine();
			writer.newLine();
			writer.write("# If set, will log quite alot in the server console (mostly debug)");writer.newLine();
			writer.write("GENERAL_DETAILED_LOG=false");writer.newLine();
			writer.newLine();
			writer.newLine();
			
			writer.write("##### Globalbuildmode settings: ############################");writer.newLine();
			writer.newLine();
			writer.write("# Enable the Globalbuildmode");writer.newLine(); //done
			writer.write("GLOBALBUILD_ENABLE=true");writer.newLine(); 
			writer.newLine();
			writer.write("# Enable weathercheck (always sun)");writer.newLine(); //done
			writer.write("GLOBALBUILD_WEATHER=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable timecheck (always day)");writer.newLine(); //done
			writer.write("GLOBALBUILD_TIME=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable Dropblocker");writer.newLine(); //done
			writer.write("GLOBALBUILD_NODROPS=true");writer.newLine();
			writer.newLine();
			writer.newLine();
			
			writer.write("##### Buildmode settings: ##################################");writer.newLine();
			writer.newLine();
			writer.write("# Enable the buildmode");writer.newLine(); //done
			writer.write("BUILDMODE_ENABLE=true");writer.newLine(); 
			writer.newLine();
			writer.write("# If set, users are only able to activate the buildmode if the mode is allowed in the world they are in");writer.newLine(); //done
			writer.write("BUILDMODE_REQUIRE_ALLOW=false");writer.newLine(); 
			writer.newLine();
			writer.write("# Enable godmode while in buildmode");writer.newLine(); //done
			writer.write("BUILDMODE_GODMODE=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the buildmode inventory");writer.newLine(); //done
			writer.write("BUILDMODE_INVENTORY=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable Instant block break with a Pickaxe");writer.newLine(); //done
			writer.write("BUILDMODE_INSTANT_BLOCK_BREAK=true");writer.newLine();
			writer.newLine();
			writer.write("# Disable item pickup");writer.newLine(); //done
			writer.write("BUILDMODE_DISABLEPICKUP=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the unlimited itemstacks");writer.newLine(); //done
			writer.write("BUILDMODE_UNLIMITED_ITEMSTACK=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the treecutter (axe)");writer.newLine(); //done
			writer.write("BUILDMODE_TREECUTTER=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the jump command");writer.newLine(); //done
			writer.write("BUILDMODE_JUMP=true");writer.newLine();
			writer.newLine();
			writer.newLine();
			
			writer.write("##### Feature settings: ####################################");writer.newLine();
			writer.newLine();
			writer.write("# Enable the airfloor command");writer.newLine(); //done
			writer.write("FEATURE_AIRFLOOR=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the wall command");writer.newLine(); //done
			writer.write("FEATURE_BUILDER_WALL=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the wallx command");writer.newLine(); //done
			writer.write("FEATURE_BUILDER_WALLX=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the cuboid command");writer.newLine(); //done
			writer.write("FEATURE_BUILDER_CUBOID=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the sphere command");writer.newLine(); //done
			writer.write("FEATURE_BUILDER_SPHERE=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the cylinder command");writer.newLine(); //done
			writer.write("FEATURE_BUILDER_CYLINDER=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the top command");writer.newLine(); //done
			writer.write("FEATURE_TOP=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the give command");writer.newLine(); //done
			writer.write("FEATURE_GIVE=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the wool command");writer.newLine(); //done
			writer.write("FEATURE_WOOL=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the clearinventory command");writer.newLine(); //done
			writer.write("FEATURE_CLEAR_INVENTORY=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the location command");writer.newLine(); //done
			writer.write("FEATURE_LOCATION=true");writer.newLine();
			writer.newLine();
			writer.newLine();
			
			writer.write("##### Treecutter settings: ####################################");writer.newLine();
			writer.newLine();
			writer.write("# Remove leaves on treecut, will cut more than one tree at once if their leaves are connected");writer.newLine(); //done
			writer.write("TREECUTTER_CUT_LEAVES=true");writer.newLine();
			writer.newLine();
			writer.write("# if set, a tree can be felt if you hit its leaves, else you can only fell it by hitting log");writer.newLine(); //done
			writer.write("TREECUTTER_ACTIVATE_ON_LEAVES=true");writer.newLine();
			writer.newLine();
			writer.write("##### Worlds below here that will have globalbuild on startup: ");writer.newLine();
			writer.write("# e.g. world=true");writer.newLine();
			writer.write("# If you don't want a world, put false instead.");writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.close();
			stream.close();
			
		} catch (Exception e) {
			System.out.println("ERROR writing cfgfile");
			e.printStackTrace();
		}
	}
	
	public boolean getConfigValue(String key){
		return settings.get(key);
	}
	
	public boolean hasConfigValue(String key){
		return settings.containsKey(key);
	}
	
	public HashMap<String, Boolean> getSettings(){
		return settings;
	}
}
