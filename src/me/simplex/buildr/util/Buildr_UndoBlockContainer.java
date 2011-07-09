package me.simplex.buildr.util;

import org.bukkit.Material;

public class Buildr_UndoBlockContainer {
	private Material material;
	private byte materialData;
	
	public Buildr_UndoBlockContainer(Material material, byte materialData) {
		this.material = material;
		this.materialData = materialData;
	}

	public Material getMaterial() {
		return material;
	}

	public byte getMaterialData() {
		return materialData;
	}
	
	
}
