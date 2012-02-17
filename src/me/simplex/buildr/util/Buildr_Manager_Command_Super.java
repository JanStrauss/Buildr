package me.simplex.buildr.util;

import me.simplex.buildr.Buildr;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Buildr_Manager_Command_Super implements CommandExecutor {
	protected Buildr plugin;
	
	public Buildr_Manager_Command_Super(Buildr plugin) {
		this.plugin = plugin;
	}
	
	protected boolean checkSenderIsPlayer(CommandSender s){
		return (s instanceof Player); //disable console 
	}
	
	protected enum MsgType {INFO, WARNING, ERROR};
	
	protected void SendToPlayer(Player player, MsgType type, String msg){
		player.sendMessage(BuildMsg(type, msg));
	}
	
	protected void sendToSender(CommandSender sender, MsgType type, String msg){
		sender.sendMessage(BuildMsg(type, msg));
	}
	
	private String BuildMsg(MsgType type, String msg){
		String PREFIX = "[Buildr] ";
		ChatColor MsgColor = ChatColor.WHITE;

		switch (type) {
			case INFO: 	  MsgColor = ChatColor.WHITE;  break;
			case WARNING: MsgColor = ChatColor.YELLOW; break;
			case ERROR:   MsgColor = ChatColor.RED;    break;
			default: break;
		}
		
		return ChatColor.BLUE + PREFIX + MsgColor + msg;
	}
	
	protected Material parseMaterial(String mat_re){
		int id = -1;
		try {
			id = Integer.parseInt(mat_re);
		} 
		catch (NumberFormatException e) {
			try {
				id = Material.matchMaterial(mat_re).getId();
			} 
			catch (NullPointerException e2) {
			}
		}
		if (id != -1 && Material.getMaterial(id).isBlock()) {
			return Material.getMaterial(id);
		}
		else {
			return null;
		}
	}
}