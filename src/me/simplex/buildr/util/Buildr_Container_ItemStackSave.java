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
package me.simplex.buildr.util;

import java.io.Serializable;

public class Buildr_Container_ItemStackSave implements Serializable {
	private static final long serialVersionUID = -2718242945604220137L;
	private int type;
    private int amount = 0;
    private short durability = 0;
    
    private byte material_data = 0;
    
    
	public Buildr_Container_ItemStackSave(int type, int amount,short durability, byte materialData) {
		this.type = type;
		this.amount = amount;
		this.durability = durability;
		this.material_data = materialData;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public int getType() {
		return type;
	}


	public int getAmount() {
		return amount;
	}


	public short getDurability() {
		return durability;
	}


	public byte getMaterial_data() {
		return material_data;
	}
    
 
}
