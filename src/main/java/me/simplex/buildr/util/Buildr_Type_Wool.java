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

import org.bukkit.inventory.ItemStack;

public enum Buildr_Type_Wool {
	 WHITE(			(short) 0, 	(byte)0x0000),
	 ORANGE(		(short) 1,	(byte)0x0001),
	 MAGENTA(		(short) 2,	(byte)0x0002),
	 LIGHT_BLUE(	(short) 3,	(byte)0x0003),
	 YELLOW(		(short) 4,	(byte)0x0004),
	 LIME(			(short) 5,	(byte)0x0005),
	 PINK(			(short) 6,	(byte)0x0006),
	 GREY(			(short) 7,	(byte)0x0007),
	 LIGHT_GREY(	(short) 8,	(byte)0x0008),
	 CYAN(			(short) 9,	(byte)0x0009),
	 PURPLE(		(short)10,	(byte)0x000A),
	 BLUE(			(short)11,	(byte)0x000B),
	 BROWN(			(short)12,	(byte)0x000C),
	 GREEN(			(short)13,	(byte)0x000D),
	 RED(			(short)14,	(byte)0x000E),
	 BLACK(			(short)15,	(byte)0x000F);
	 
	private short dmg_value;
	private byte block_data_value;
	
	private Buildr_Type_Wool(short dmg_value,byte block_data_value) {
		this.dmg_value = dmg_value;
		this.block_data_value = block_data_value;
	}

	public ItemStack giveStack(){
		return new ItemStack(35, 64, dmg_value);
	}
	
	public byte getBlockDataValue(){
		return block_data_value;
	}
}
