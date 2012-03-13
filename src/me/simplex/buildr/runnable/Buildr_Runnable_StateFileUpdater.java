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
package me.simplex.buildr.runnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import me.simplex.buildr.Buildr;

public class Buildr_Runnable_StateFileUpdater implements Runnable{
	private File statefile;
	private ArrayList<String> builders;
	private Buildr plugin;

	
	public Buildr_Runnable_StateFileUpdater(File statefile, ArrayList<String> builders, Buildr plugin) {
		this.statefile = statefile;
		this.builders = builders;
		this.plugin = plugin;
	}

	@Override
	public void run() {
		if (builders== null) {
			//statefile.delete();
			return;
		}		
		//System.out.println("StateFile update. size:"+builders.size());
		
		try {
			ObjectOutputStream objctOutStrm = new ObjectOutputStream(new FileOutputStream(statefile));
			objctOutStrm.writeObject(builders);
			objctOutStrm.flush();
			objctOutStrm.close();
		} catch (Exception e) {
			plugin.log("Failed to write to InventoryStateFile");
			e.printStackTrace();
		}
	}
}
