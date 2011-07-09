package me.simplex.buildr.util;

import java.util.HashMap;
import java.util.LinkedList;
import org.bukkit.block.Block;

public class Buildr_UndoStack {

	private int maxSize;
	private LinkedList<HashMap<Block, Buildr_UndoBlockContainer>> stack;

	public Buildr_UndoStack(final int maxSize){
		this.maxSize = maxSize;
		stack = new LinkedList<HashMap<Block, Buildr_UndoBlockContainer>>();
	}
 
	public HashMap<Block, Buildr_UndoBlockContainer> peek() {
		return stack.peekFirst();
	}
  	
  	public HashMap<Block, Buildr_UndoBlockContainer> poll() {
  		return stack.pollFirst();
  	}
  	
  	public void push(HashMap<Block, Buildr_UndoBlockContainer> item){
  		stack.push(item);
  		if (stack.size() > maxSize) {
			stack.removeLast();
		}
  	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}
}
