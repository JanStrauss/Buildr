package me.simplex.buildr.util;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Buildr_UndoStack {

	private int maxSize;
	private LinkedList<HashMap<Block, Material>> stack;

	public Buildr_UndoStack(final int maxSize){
		this.maxSize = maxSize;
		stack = new LinkedList<HashMap<Block, Material>>();
	}
 
	public HashMap<Block, Material> peek() {
		return stack.peekFirst();
	}
  	
  	public HashMap<Block, Material> poll() {
  		return stack.pollFirst();
  	}
  	
  	public void push(HashMap<Block, Material> item){
  		stack.push(item);
  		if (stack.size() > maxSize) {
			stack.removeLast();
		}
  	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}
}
