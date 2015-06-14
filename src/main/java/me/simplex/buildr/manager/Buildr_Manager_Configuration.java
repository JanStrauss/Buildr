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
	private final Buildr plugin;
	public static File configfile;
	private final HashMap<String, Boolean> settings;
	private String version;
	
	public Buildr_Manager_Configuration(Buildr buildr){
		this.plugin = buildr;
		configfile = new File(plugin.getPluginDirectory()+File.separator+"settings.properties");
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
					if(!line.startsWith("#") && !line.isEmpty() && !line.startsWith("CFG=")){
						String linecont[] = line.split("=");
						if(linecont.length==2){
							String key = linecont[0];
							String value = linecont[1];
							settings.put(key, Boolean.valueOf(value));
						}
					}
					if ((version == null) && line.startsWith("CFG=")) {
						version = line.substring(4).trim();
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
			BufferedWriter w = new BufferedWriter(stream);
			w.write("############################################################");w.newLine();
			w.write("#                                                          #");w.newLine();
			w.write("#                    BUILDR CONFIGURATION                  #");w.newLine();
			w.write("#                                                          #");w.newLine();
			w.write("#                             v0.8.0                       #");w.newLine();
			w.write("############################################################");w.newLine();
			w.newLine();
			w.write("CFG=0.8.0");w.newLine();
			w.newLine();
			w.write("##### General settings: ####################################");w.newLine();
			w.newLine();
			w.write("# Print the settings while Buildr loads");w.newLine();//done
			w.write("GENERAL_DISPLAY_SETTINGS_ON_LOAD=false");w.newLine(); 
			w.newLine();
			w.write("# Enable Permissions, if false Buildr checks for OP");w.newLine();
			w.write("GENERAL_USE_PERMISSIONS=true");w.newLine();
			w.newLine();
			w.write("# If set, will log quite alot in the server console (mostly debug)");w.newLine();
			w.write("GENERAL_DETAILED_LOG=false");w.newLine();
			w.newLine();
			w.newLine();
			
			w.write("##### Globalbuildmode settings: ############################");w.newLine();
			w.newLine();
			w.write("# Enable the Globalbuildmode");w.newLine(); //done
			w.write("GLOBALBUILD_ENABLE=true");w.newLine(); 
			w.newLine();
			w.write("# Enable weathercheck (always sun)");w.newLine(); //done
			w.write("GLOBALBUILD_WEATHER=true");w.newLine();
			w.newLine();
			w.write("# Enable timecheck (always day)");w.newLine(); //done
			w.write("GLOBALBUILD_TIME=true");w.newLine();
			w.newLine();
			w.write("# Enable Dropblocker");w.newLine(); //done
			w.write("GLOBALBUILD_NODROPS=true");w.newLine();
			w.newLine();
			w.write("# Force Players to Buildmode if the world they are in is in Globalbuildmode");w.newLine(); //done
			w.write("GLOBALBUILD_FORCE_BUILDMODE=false");w.newLine();
			w.newLine();
			w.newLine();
			
			w.write("##### Buildmode settings: ##################################");w.newLine();
			w.newLine();
			w.write("# Enable the buildmode");w.newLine(); //done
			w.write("BUILDMODE_ENABLE=true");w.newLine(); 
			w.newLine();
			w.write("# If set, users won't be switched out of buildmode after a reconnect/login");w.newLine(); //done
			w.write("BUILDMODE_STAY_AFTER_LOGOUT=false");w.newLine(); 
			w.newLine();
			w.write("# If set, users are only able to activate the buildmode if the mode is allowed in the world they are in");w.newLine(); //done
			w.write("BUILDMODE_REQUIRE_ALLOW=false");w.newLine(); 
			w.newLine();
			w.write("# Enable godmode while in buildmode");w.newLine(); //done
			w.write("BUILDMODE_GODMODE=true");w.newLine();
			w.newLine();
			w.write("# Enable Instant block break with a Pickaxe");w.newLine(); //done
			w.write("BUILDMODE_INSTANT_BLOCK_BREAK=true");w.newLine();
			w.newLine();
			w.write("# Enable Instant block break with every item");w.newLine(); //done
			w.write("BUILDMODE_INSTANT_BLOCK_BREAK_ALL=false");w.newLine();
			w.newLine();
			w.write("# Disable item pickup");w.newLine(); //done
			w.write("BUILDMODE_DISABLEPICKUP=true");w.newLine();
			w.newLine();
			w.write("# Enable the unlimited itemstacks");w.newLine(); //done
			w.write("BUILDMODE_UNLIMITED_ITEMSTACK=true");w.newLine();
			w.newLine();
			w.write("# Enable the treecutter (axe)");w.newLine(); //done
			w.write("BUILDMODE_TREECUTTER=true");w.newLine();
			w.newLine();
			w.write("# Enable the jump command");w.newLine(); //done
			w.write("BUILDMODE_JUMP=true");w.newLine();
			w.newLine();
			w.write("# Toggle the gamemode if a player enters/leaves buildmode");w.newLine(); //done
			w.write("BUILDMODE_TOGGLE_GAMEMODE=false");w.newLine();
			w.newLine();
			w.newLine();
			
			w.write("##### Feature settings: ####################################");w.newLine();
			w.newLine();
			w.write("# Enable the airfloor command");w.newLine(); //done
			w.write("FEATURE_AIRFLOOR=true");w.newLine();
			w.newLine();
			w.write("# Enable the wall command");w.newLine(); //done
			w.write("FEATURE_BUILDER_WALL=true");w.newLine();
			w.newLine();
			w.write("# Enable the wallx command");w.newLine(); //done
			w.write("FEATURE_BUILDER_WALLX=true");w.newLine();
			w.newLine();
			w.write("# Enable the cuboid command");w.newLine(); //done
			w.write("FEATURE_BUILDER_CUBOID=true");w.newLine();
			w.newLine();
			w.write("# Enable the sphere command");w.newLine(); //done
			w.write("FEATURE_BUILDER_SPHERE=true");w.newLine();
			w.newLine();
            w.write("# Enable the cylinder command");w.newLine(); //done
            w.write("FEATURE_BUILDER_CYLINDER=true");w.newLine();
            w.newLine();
            w.write("# Enable the slope command");w.newLine(); //done
            w.write("FEATURE_BUILDER_SLOPE=true");w.newLine();
            w.newLine();
			w.write("# Enable the top command");w.newLine(); //done
			w.write("FEATURE_TOP=true");w.newLine();
			w.newLine();
			w.write("# Enable the give command");w.newLine(); //done
			w.write("FEATURE_GIVE=true");w.newLine();
			w.newLine();
			w.write("# Enable the givex command");w.newLine(); //done
			w.write("FEATURE_GIVEX=true");w.newLine();
			w.newLine();
			w.write("# Enable the wool command");w.newLine(); //done
			w.write("FEATURE_WOOL=true");w.newLine();
			w.newLine();
			w.write("# Enable the clearinventory command");w.newLine(); //done
			w.write("FEATURE_CLEAR_INVENTORY=true");w.newLine();
			w.newLine();
			w.write("# Enable the location command");w.newLine(); //done
			w.write("FEATURE_LOCATION=true");w.newLine();
			w.newLine();
			w.newLine();
			
			w.write("##### Treecutter settings: ####################################");w.newLine();
			w.newLine();
			w.write("# Remove leaves on treecut, will cut more than one tree at once if their leaves are connected");w.newLine(); //done
			w.write("TREECUTTER_CUT_LEAVES=true");w.newLine();
			w.newLine();
			w.write("# if set, a tree can be felt if you hit its leaves, else you can only fell it by hitting log");w.newLine(); //done
			w.write("TREECUTTER_ACTIVATE_ON_LEAVES=true");w.newLine();
			w.newLine();
			w.newLine();
			
			w.write("##### World settings  ####################################");w.newLine();
			w.newLine();
			w.write("# Worlds below here will have globalbuild enabled startup");w.newLine();
			w.write("# e.g. world=true");w.newLine();
			w.newLine();
			w.newLine();
			
			w.close();
			stream.close();
			
		} catch (Exception e) {
			System.out.println("ERROR writing cfgfile");
			e.printStackTrace();
		}
	}
	
	public void checkVersion(){
		if (version == null || !version.equals(plugin.getCurrentConfig())) {
			plugin.importantLog("outdated settings.properties!");
			
			File con = new File("plugins/Buildr/settings.properties");
			File oldcon = new File("plugins/Buildr/settings_old_"+version+".properties");
			if (oldcon.exists()) {
				oldcon.delete();
			}
			
			con.renameTo(oldcon);
			con.delete();
			createSettings();
		}
		else {
			plugin.log("Config: Version OK");
		}
	}
	
	public boolean getConfigValue(String key){
		Object ret = settings.get(key);
		if (ret == null) {
			return false;
		}
		return settings.get(key);
	}
	
	public boolean hasConfigValue(String key){
		return settings.containsKey(key);
	}
	
	public HashMap<String, Boolean> getSettings(){
		return settings;
	}
}
