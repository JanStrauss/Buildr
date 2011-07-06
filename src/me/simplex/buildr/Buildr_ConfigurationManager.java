package me.simplex.buildr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Buildr_ConfigurationManager {
	Buildr plugin;
	
	public Buildr_ConfigurationManager(Buildr plugin){
		this.plugin = plugin;
	}
	
	public boolean checkDirectory(){
		return new File(plugin.getPluginDirectory()).mkdir();
    }
	
	public boolean checkConfigFile(){
		return new File(plugin.getPluginDirectory()+File.separator+"settings.cfg").exists();
    }
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> loadSettings(){
		HashMap<String, Object> settings=null;
		ObjectInputStream objctInStrm;
		try {
			objctInStrm = new ObjectInputStream(new FileInputStream(plugin.getPluginDirectory()+File.separator+"settings.cfg"));
			settings = (HashMap<String, Object>)objctInStrm.readObject();
			objctInStrm.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return settings;
	}
	
	public void saveSettings(HashMap<String, Object> settings){
		try {
			ObjectOutputStream objctOutStrm = new ObjectOutputStream(new FileOutputStream(plugin.getPluginDirectory()+File.separator+"settings.cfg"));
			objctOutStrm.writeObject(settings);
			objctOutStrm.flush();
			objctOutStrm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createSettings(){
		HashMap<String, Object> settings = new HashMap<String, Object>();
		settings.put("SPAM_ON_STARTUP", true);
		saveSettings(settings);
	}
}
