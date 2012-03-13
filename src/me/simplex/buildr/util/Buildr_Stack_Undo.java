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

import java.util.HashMap;
import java.util.LinkedList;
import org.bukkit.block.Block;

public class Buildr_Stack_Undo extends LinkedList<HashMap<Block, Buildr_Container_UndoBlock>> {
	private static final long serialVersionUID = 2757868195679642194L;
	private int maxSize;

	public Buildr_Stack_Undo(final int maxSize){
		super();
		this.maxSize = maxSize;
	}
 
  	public void pushWithLimit(HashMap<Block, Buildr_Container_UndoBlock> item){
  		push(item);
  		if (size() > maxSize) {
			removeLast();
		}
  	}
  	
}
