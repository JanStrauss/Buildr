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
