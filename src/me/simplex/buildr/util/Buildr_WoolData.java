package me.simplex.buildr.util;

import org.bukkit.inventory.ItemStack;

public enum Buildr_WoolData {
	 WHITE((short)0),
	 ORANGE((short)1),
	 MAGENTA((short)2),
	 LIGHT_BLUE((short)3),
	 YELLOW((short)4),
	 LIME((short)5),
	 PINK((short)6),
	 GREY((short)7),
	 LIGHT_GREY((short)8),
	 CYAN((short)9),
	 PURPLE((short)10),
	 BLUE((short)11),
	 BROWN((short)12),
	 GREEN((short)13),
	 RED((short)14),
	 BLACK((short)15);
	
	private short dmg_value;
	
	private Buildr_WoolData(short dmg_value) {
		this.dmg_value = dmg_value;
	}

	public ItemStack giveStack(){
		return new ItemStack(35, 64, dmg_value);
	}
	
}
