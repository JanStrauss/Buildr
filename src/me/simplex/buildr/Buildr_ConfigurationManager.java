package me.simplex.buildr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Buildr_ConfigurationManager {
	private Buildr plugin;
	private File configfile;
	private HashMap<String, Boolean> settings;
	
	public Buildr_ConfigurationManager(Buildr buildr){
		this.plugin = buildr;
		this.configfile = new File(plugin.getPluginDirectory()+File.separator+"settings.properties");
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
			writer.write("# Enable the treecutter (Axe)");writer.newLine(); //TODO
			writer.write("BUILDMODE_TREECUTTER=true");writer.newLine();
			writer.newLine();
			writer.write("# Let stairs drop stairs if destoyed with instantblockbreak on");writer.newLine(); //done
			writer.write("BUILDMODE_STAIRMODE=true");writer.newLine();
			writer.newLine();
			writer.newLine();
			
			writer.write("##### Feature settings: ####################################");writer.newLine();
			writer.newLine();
			writer.write("# Enable the airfloor command");writer.newLine(); //done
			writer.write("FEATURE_AIRFLOOR=true");writer.newLine();
			writer.newLine();
			writer.write("# Enable the wall command");writer.newLine(); //done
			writer.write("FEATURE_WALLBUILDER=true");writer.newLine();
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
	
	public HashMap<String, Boolean> getSettings(){
		return settings;
	}
}
