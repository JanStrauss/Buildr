package me.simplex.buildr.util;

import java.util.HashMap;
import java.util.LinkedList;
import org.bukkit.block.Block;

public class Buildr_Stack_Undo {

	private int maxSize;
	private LinkedList<HashMap<Block, Buildr_Container_UndoBlock>> stack;

	public Buildr_Stack_Undo(final int maxSize){
		this.maxSize = maxSize;
		stack = new LinkedList<HashMap<Block, Buildr_Container_UndoBlock>>();
	}
 
	public HashMap<Block, Buildr_Container_UndoBlock> peek() {
		return stack.peekFirst();
	}
  	
  	public HashMap<Block, Buildr_Container_UndoBlock> poll() {
  		return stack.pollFirst();
  	}
  	
  	public void push(HashMap<Block, Buildr_Container_UndoBlock> item){
  		stack.push(item);
  		if (stack.size() > maxSize) {
			stack.removeLast();
		}
  	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}
}
