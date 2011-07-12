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
