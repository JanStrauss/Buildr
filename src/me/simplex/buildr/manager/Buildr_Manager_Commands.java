package me.simplex.buildr.manager;

import java.util.HashMap;

import me.simplex.buildr.Buildr;
import me.simplex.buildr.manager.builder.Buildr_Manager_Builder_Cuboid;
import me.simplex.buildr.manager.builder.Buildr_Manager_Builder_Cylinder;
import me.simplex.buildr.manager.builder.Buildr_Manager_Builder_Sphere;
import me.simplex.buildr.manager.builder.Buildr_Manager_Builder_Wall;
import me.simplex.buildr.manager.builder.Buildr_Manager_Builder_Wallx;
import me.simplex.buildr.runnable.Buildr_Runnable_Undo;
import me.simplex.buildr.util.Buildr_Container_UndoBlock;
import me.simplex.buildr.util.Buildr_Type_Wool;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Buildr_Manager_Commands {
	private Buildr plugin;

	
	/**
	 * 
	 * @param plugin
	 */
	public Buildr_Manager_Commands(Buildr plugin) {
		this.plugin = plugin;
	}

	public boolean handleCommand(CommandSender sender, Command command, String[] args){
		//GLOBALBUILD
		if (command.getName().equalsIgnoreCase("globalbuild")) {
			if (args.length != 0) {
				return false;
			}
			
			if (plugin.checkPermission((Player)sender, "buildr.cmd.globalbuild")) {
				World world;
				if (args.length > 0) {
					world = plugin.getServer().getWorld(args[0]);
				}
				else {
					world = ((Player)sender).getWorld();
				}
				if (world != null) {
					this.cmd_globalbuild(sender, world);
				}
				else {
					sender.sendMessage(ChatColor.RED+"There is no world with this name");
				}
				return true;
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
				return true;
			}
			
		}
		
		//BUILD
		else if (command.getName().equalsIgnoreCase("build")) {
			if (args.length != 0) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.build")) {
				this.cmd_build(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//TOP
		else if (command.getName().equalsIgnoreCase("top")) {
			if (args.length != 0) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.top")) {
				this.cmd_top(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		// AIRFLOOR
		else if (command.getName().equalsIgnoreCase("airfloor")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.airfloor")) {
				if (args.length==0) {
					return false;
				}
				try {
					int material=0,height=0,size=0;
					if (args.length > 3 && args.length < 2) {
						return false;
					}
					if (args.length == 3) {
						size = Math.abs(Integer.parseInt(args[2]));
						if (size > 100) {
							size = 100;
						}
					}
					height = Math.abs(Integer.parseInt(args[1]));
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						material = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase());
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sender.sendMessage("No such wool");
							return true;
						}
					}
					else {
						try {
							material = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								material = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {
								sender.sendMessage(ChatColor.RED+"wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(material)== null) {
						sender.sendMessage("Invalid Material");
						return true;
					}
					this.cmd_airfloor(sender, material, mat_data, height, size);
					} 
				catch (NumberFormatException e) {
					sender.sendMessage("Wrong format, usage: /airfloor <material> <height> <size>");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//UNDO
		else if (command.getName().equalsIgnoreCase("undo")) {
			if (args.length != 0) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.undo")) {
				this.cmd_undo(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//GIVE
		else if (command.getName().equalsIgnoreCase("give")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.give")) {
				if (args.length == 0 ||args.length > 3) {
					return false;
				}
				if (args.length == 1) {
					this.cmd_give(sender,args[0],-1,null);
				}
				else if (args.length==2) {
					int amount = -1;
					try {
						amount = Integer.parseInt(args[1]);
						if (amount >64) {
							amount = 64;
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED+"wrong format");
						return true;
					}
					this.cmd_give(sender,args[0],amount,null);
				}
				else if(args.length == 3) {
					int amount = -1;
					try {
						amount = Integer.parseInt(args[2]);
						if (amount >64) {
							amount = 64;
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED+"wrong format");
						return true;
					}
					this.cmd_give(sender,args[1],amount,args[0]);
				}
				
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//WOOL
		else if (command.getName().equalsIgnoreCase("wool")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.wool")) {
				if (args.length!=1) {
					return false;
				}
				this.cmd_wool(sender, args[0]);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//ClearInv
		else if (command.getName().equalsIgnoreCase("clearinv")) {
			if (args.length != 0) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.clearinv")) {
				if (args.length!=0) {
					return false;
				}
				this.cmd_clrInv(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//Wall
		else if (command.getName().equalsIgnoreCase("wall")) {
			if (args.length < 1 || args.length > 2) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.wall")) {
				if (args.length >=1 && args.length <=2) {
					Material material;
					int id;
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						id = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase());
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sender.sendMessage("No such wool");
							return true;
						}
					}
					else {
						try {
							id = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								id = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {
								sender.sendMessage(ChatColor.RED+"wrong format");
								return true;
							}

						}
					}

					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sender.sendMessage(ChatColor.RED+"unvalid blocktype");
						return true;
					}
					if (args.length==1) {
							this.cmd_wall(sender, material, mat_data, false);
							return true;
					}
					else if (args.length==2) {
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							this.cmd_wall(sender, material, mat_data, true);
							return true;
						}
						else {
							return false;
						}
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//WALLX
		else if (command.getName().equalsIgnoreCase("wallx")) {
			if (args.length < 1 || args.length > 2) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.wallx")) {
				if (args.length >=1 && args.length <=2) {
					Material material;
					int id;
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						id = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase());
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sender.sendMessage("No such wool");
							return true;
						}
					}
					else {
						try {
							id = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								id = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {
								sender.sendMessage(ChatColor.RED+"wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sender.sendMessage(ChatColor.RED+"unvalid blocktype");
						return true;
					}
					if (args.length==1) {
							this.cmd_wallx(sender, material, mat_data, false);
							return true;
					}
					else if (args.length==2) {
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							this.cmd_wallx(sender, material, mat_data, true);
							return true;
						}
						else {
							return false;
						}
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//CUBOID
		else if (command.getName().equalsIgnoreCase("cuboid")) {
			if (args.length < 1 || args.length > 3) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.cuboid")) {
				if (args.length >=1 && args.length <=3) {
					Material material;
					int id;
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						id = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase());
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sender.sendMessage("No such wool");
							return true;
						}
					}
					else {
						try {
							id = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								id = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {
								sender.sendMessage(ChatColor.RED+"wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sender.sendMessage(ChatColor.RED+"unvalid blocktype");
						return true;
					}
					if (args.length==1) {
							this.cmd_cuboid(sender, material, mat_data, false, false);
							return true;
					}
					else if (args.length == 2) {
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							this.cmd_cuboid(sender, material, mat_data, true,false);
							return true;
						}
						else if(args[1].equalsIgnoreCase("h") || args[1].equalsIgnoreCase("hollow")) {
							this.cmd_cuboid(sender, material, mat_data, false, true);
							return true;
						}
						else {
							return false;
						}
					}
					else if (args.length == 3) {
						boolean hollow = false;
						boolean aironly = false;
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							aironly = true;
						}
						if(args[2].equalsIgnoreCase("h") || args[2].equalsIgnoreCase("hollow")) {
							hollow = true;
						}
						this.cmd_cuboid(sender, material, mat_data, aironly, hollow);
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//SPHERE
		else if (command.getName().equalsIgnoreCase("sphere")) {
			if (args.length < 1 || args.length > 3) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.sphere")) {
				if (args.length >=1 && args.length <=3) {
					Material material;
					int id;
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						id = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase());
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sender.sendMessage("No such wool");
							return true;
						}
					}
					else {
						try {
							id = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								id = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {
								sender.sendMessage(ChatColor.RED+"wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sender.sendMessage(ChatColor.RED+"unvalid blocktype");
						return true;
					}
					if (args.length==1) {
							this.cmd_sphere(sender, material, mat_data, false, false);
							return true;
					}
					else if (args.length == 2) {
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							this.cmd_sphere(sender, material, mat_data, true,false);
							return true;
						}
						else if(args[1].equalsIgnoreCase("h") || args[1].equalsIgnoreCase("hollow")) {
							this.cmd_sphere(sender, material, mat_data, false, true);
							return true;
						}
						else {
							return false;
						}
					}
					else if (args.length == 3) {
						boolean hollow = false;
						boolean aironly = false;
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							aironly = true;
						}
						if(args[2].equalsIgnoreCase("h") || args[2].equalsIgnoreCase("hollow")) {
							hollow = true;
						}
						this.cmd_sphere(sender, material, mat_data, aironly, hollow);
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//HALFSPHERE
		else if (command.getName().equalsIgnoreCase("halfsphere")) {
			if (args.length < 1 || args.length > 3) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.sphere")) {
				if (args.length >=1 && args.length <=3) {
					Material material;
					int id;
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						id = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase());
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sender.sendMessage("No such wool");
							return true;
						}
					}
					else {
						try {
							id = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								id = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {
								sender.sendMessage(ChatColor.RED+"wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sender.sendMessage(ChatColor.RED+"unvalid blocktype");
						return true;
					}
					if (args.length==1) {
							this.cmd_half_sphere(sender, material, mat_data, false, false);
							return true;
					}
					else if (args.length == 2) {
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							this.cmd_half_sphere(sender, material, mat_data, true,false);
							return true;
						}
						else if(args[1].equalsIgnoreCase("h") || args[1].equalsIgnoreCase("hollow")) {
							this.cmd_half_sphere(sender, material, mat_data, false, true);
							return true;
						}
						else {
							return false;
						}
					}
					else if (args.length == 3) {
						boolean hollow = false;
						boolean aironly = false;
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							aironly = true;
						}
						if(args[2].equalsIgnoreCase("h") || args[2].equalsIgnoreCase("hollow")) {
							hollow = true;
						}
						this.cmd_half_sphere(sender, material, mat_data, aironly, hollow);
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//HALFSPHERE
		else if (command.getName().equalsIgnoreCase("cylinder")) {
			if (args.length < 1 || args.length > 3) {
				return false;
			}
			if (plugin.checkPermission((Player)sender, "buildr.cmd.cylinder")) {
				if (args.length >=1 && args.length <=3) {
					Material material;
					int id;
					byte mat_data = (byte)0;
					if (args[0].toUpperCase().startsWith("WOOL:")) {
						id = 35;
						Buildr_Type_Wool woolcolor;
						try {
							woolcolor = Enum.valueOf(Buildr_Type_Wool.class, args[0].toUpperCase());
							mat_data = woolcolor.getBlockDataValue();
						} catch (IllegalArgumentException e) {
							sender.sendMessage("No such wool");
							return true;
						}
					}
					else {
						try {
							id = Integer.parseInt(args[0]);
						} catch (NumberFormatException e) {
							try {
								id = Material.matchMaterial(args[0]).getId();
							} catch (NullPointerException e2) {
								sender.sendMessage(ChatColor.RED+"wrong format");
								return true;
							}

						}
					}
					if (Material.getMaterial(id).isBlock()) {
						material = Material.getMaterial(id);
					}
					else {
						sender.sendMessage(ChatColor.RED+"unvalid blocktype");
						return true;
					}
					if (args.length==1) {
							this.cmd_cylinder(sender, material, mat_data, false, false);
							return true;
					}
					else if (args.length == 2) {
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							this.cmd_cylinder(sender, material, mat_data, true,false);
							return true;
						}
						else if(args[1].equalsIgnoreCase("h") || args[1].equalsIgnoreCase("hollow")) {
							this.cmd_cylinder(sender, material, mat_data, false, true);
							return true;
						}
						else {
							return false;
						}
					}
					else if (args.length == 3) {
						boolean hollow = false;
						boolean aironly = false;
						if (args[1].equalsIgnoreCase("a") || args[1].equalsIgnoreCase("air") || args[1].equalsIgnoreCase("aironly")) {
							aironly = true;
						}
						if(args[2].equalsIgnoreCase("h") || args[2].equalsIgnoreCase("hollow")) {
							hollow = true;
						}
						this.cmd_cylinder(sender, material, mat_data, aironly, hollow);
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}

			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//LOCATION
		else if (command.getName().equalsIgnoreCase("location")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.location")) {
				if (args.length!=0) {
					return false;
				}
				this.cmd_location(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		//ALLOWBUILD
		else if (command.getName().equalsIgnoreCase("allowbuild")) {
			if (plugin.checkPermission((Player)sender, "buildr.cmd.allowbuild")) {
				if (args.length!=0) {
					return false;
				}
				this.cmd_allowbuild(sender);
			}
			else {
				sender.sendMessage(ChatColor.RED+"You dont have the permission to perform this action");
			}
			return true;
		}
		
		//ELSE
		else {
			return false;
		}
	}
	
	public void cmd_globalbuild(CommandSender sender,World world){
		if (!plugin.getConfigValue("GLOBALBUILD_ENABLE")) {
			return;
		}
		if (plugin.getWorldBuildMode().contains(world)) {
			plugin.leaveGlobalbuildmode(world);
			sender.sendMessage("Globalbuildmode disabled");
			for (Player inhab : world.getPlayers()) {
				inhab.sendMessage(ChatColor.AQUA+((Player)sender).getName()+" disabled the Globalbuildmode on the world you are currently in");
			}
			plugin.log("Globalbuildmode disabled in World "+world.getName());
		}
		else {
			plugin.enterGlobalbuildmode(world);
			sender.sendMessage("Globalbuildmode enabled");
			for (Player inhab : world.getPlayers()) {
				inhab.sendMessage(ChatColor.AQUA+((Player)sender).getName()+" ensabled the Globalbuildmode on the world you are currently in");
			}
			plugin.log("Globalbuildmode enabled in World "+world.getName());
		}
	}
	
	/**
	 * 
	 * @param sender
	 */
	public void cmd_build(CommandSender sender){
		if (!plugin.getConfigValue("BUILDMODE_ENABLE")) {
			return;
		}
		if (plugin.getPlayerBuildMode().contains((Player)sender)) {
			
			sender.sendMessage(ChatColor.BLUE+"Buildmode disabled");
			plugin.leaveBuildmode((Player)sender);
		}
		else {
			
			sender.sendMessage(ChatColor.BLUE+"Buildmode enabled");
			plugin.enterBuildmode((Player)sender);
		}
	}
	
	/**
	 * 
	 * @param sender
	 */
	public void cmd_top(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_TOP")) {
			return;
		}
		Player player = (Player)sender;
		if (player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getWorld().getHighestBlockYAt(player.getLocation()), player.getLocation().getZ(),player.getLocation().getYaw(),player.getLocation().getPitch()))) 
		{
			player.sendMessage("Ported to Top.");
		}
		else {
			player.sendMessage("Something went wrong");
		}
	}
	
	/**
	 * 
	 * @param sender
	 * @param material
	 * @param height
	 * @param size
	 */
	public void cmd_airfloor(CommandSender sender, int material, byte mat_data, int height, int size){
		if (!plugin.getConfigValue("FEATURE_AIRFLOOR")) {
			return;
		}
		Player player = (Player)sender;
		int blockheight = player.getLocation().getBlockY()+height-1;
		Location location = new Location(player.getWorld(), player.getLocation().getBlockX(), blockheight, player.getLocation().getBlockZ());
		HashMap<Block, Buildr_Container_UndoBlock> UnDoList = new HashMap<Block, Buildr_Container_UndoBlock>();		
		if (blockheight > 128) {
			blockheight = 128;
		}
		if (size == 0 || size == 1) {
			Block block = player.getWorld().getBlockAt(location);
			UnDoList.put(block,new Buildr_Container_UndoBlock(block.getType(), block.getData()));
			block.setTypeId(material);
		}
		else {
			int offset;
			
			if ((size % 2) == 0) {offset = size/2;}
			else {offset = (size-1)/2;}
			
			int xstart= location.getBlockX()-offset;
			int zstart= location.getBlockZ()-offset;
			
			int x= xstart;
			int z= zstart;
			
			for (int i = 0; i < size; i++) { //x
				for (int j = 0; j < size; j++) {//z
					Block block = player.getWorld().getBlockAt(x, blockheight, z);
					UnDoList.put(block, new Buildr_Container_UndoBlock(block.getType(), block.getData()));
					player.getWorld().getBlockAt(x, blockheight, z).setTypeId(material);
					player.getWorld().getBlockAt(x, blockheight, z).setData(mat_data);
					z++;
				}
				z=zstart;
				x++;
			}
		}
		sender.sendMessage("Block(s) placed");
		plugin.getUndoList().addToStack(UnDoList, player);
		plugin.log(player.getName()+" used /airfloor: "+UnDoList.size()+" blocks affected");
	}
	

	/**
	 * 
	 * @param sender
	 */
	public void cmd_undo(CommandSender sender){		
		if (!plugin.getConfigValue("FEATURE_AIRFLOOR") && 
			!plugin.getConfigValue("FEATURE_WALLBUILDER") && 
			!plugin.getConfigValue("FEATURE_WALLXBUILDER") && 
			!plugin.getConfigValue("BUILDMODE_TREECUTTER")) {
			return;
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Buildr_Runnable_Undo((Player)sender, plugin));
	}
	
	/**
	 * 
	 * @param sender
	 * @param material
	 * @param amount
	 */
	public void cmd_give(CommandSender sender, String material, int amount, String player) {
		if (!plugin.getConfigValue("FEATURE_GIVE")) {
			return;
		}
		Material give_mat = null;
		try {
			give_mat = Material.getMaterial(Integer.parseInt(material));
		} catch (NumberFormatException e) {
			give_mat = Material.matchMaterial(material);
		}
		if (give_mat == null) {
			sender.sendMessage(ChatColor.RED+"No such Item found");
			return;
		}
		if (amount ==-1 || amount == 0) {
			amount = 64;
		}
		Player giveto = ((Player)sender);
		if (player !=null) {
			giveto = sender.getServer().getPlayer(player);
		}
		if (giveto == null) {
			sender.sendMessage(ChatColor.RED+"Player not found");
			return;
		}
		if (give_mat.getId() == 0) {
			sender.sendMessage(ChatColor.RED+"You can't give air");
			return;
		}
		giveto.getInventory().addItem(new ItemStack(give_mat,amount));
		sender.sendMessage("Gave "+giveto.getName()+" a stack of "+amount+" "+give_mat.toString()+"(ID: "+give_mat.getId()+")");
	}
	/**
	 * 
	 * @param sender
	 * @param args
	 */
	public void cmd_wool(CommandSender sender, String args) {
		if (!plugin.getConfigValue("FEATURE_WOOL")) {
			return;
		}
		String upcase= args.toUpperCase();
		Buildr_Type_Wool woolcolor;
		try {
			woolcolor = Enum.valueOf(Buildr_Type_Wool.class, upcase);
		} catch (IllegalArgumentException e) {
			sender.sendMessage("No such color");
			return;
		}
		((Player)sender).getInventory().addItem(woolcolor.giveStack());
		String ret = "Gave yourself a stack of "+woolcolor.toString().toLowerCase()+" wool";
		sender.sendMessage(ret);
	}
	/**
	 * 
	 * @param sender
	 * @param material
	 * @param aironly
	 */
	public void cmd_wall(CommandSender sender, Material material, byte material_data, boolean aironly) {
		if (!plugin.getConfigValue("FEATURE_BUILDER_WALL")) {
			return;
		}
		if (plugin.checkPlayerHasStartedBuilding((Player)sender)) {
			plugin.removeStartedBuilding((Player)sender);
			sender.sendMessage(ChatColor.YELLOW+"previous started building aborted.");
		}
		plugin.getStartedBuildings().add(new Buildr_Manager_Builder_Wall((Player)sender, material, aironly, plugin,material_data));
		String buildinfo ="Started new Wall. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") Aironly: "+ChatColor.BLUE+aironly;
		sender.sendMessage(buildinfo);
		sender.sendMessage("Rightclick on block 1 while holding a stick to continue");
	}
	
	/**
	 * 
	 * @param sender
	 * @param material
	 * @param aironly
	 */
	public void cmd_wallx(CommandSender sender, Material material,byte material_data, boolean aironly) {
		if (!plugin.getConfigValue("FEATURE_BUILDER_WALLX")) {
			return;
		}
		if (plugin.checkPlayerHasStartedBuilding((Player)sender)) {
			plugin.removeStartedBuilding((Player)sender);
			sender.sendMessage(ChatColor.YELLOW+"previous started building aborted.");
		}
		plugin.getStartedBuildings().add(new Buildr_Manager_Builder_Wallx((Player)sender, material, aironly, plugin,material_data));
		String buildinfo ="Started new WallX. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") Aironly: "+ChatColor.BLUE+aironly;
		sender.sendMessage(buildinfo);
		sender.sendMessage("Rightclick on block 1 while holding a stick to continue");
	}
	
	public void cmd_cuboid(CommandSender sender, Material material, byte material_data, boolean aironly,boolean hollow) {
		if (!plugin.getConfigValue("FEATURE_BUILDER_CUBOID")) {
			return;
		}
		if (plugin.checkPlayerHasStartedBuilding((Player)sender)) {
			plugin.removeStartedBuilding((Player)sender);
			sender.sendMessage(ChatColor.YELLOW+"previous started building aborted.");
		}
		plugin.getStartedBuildings().add(new Buildr_Manager_Builder_Cuboid((Player)sender, material, aironly, hollow, plugin,material_data));
		String buildinfo ="Started new Cuboid. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") Aironly: "+ChatColor.BLUE+aironly+ChatColor.WHITE+" Hollow: "+ChatColor.BLUE+hollow;
		sender.sendMessage(buildinfo);
		sender.sendMessage("Rightclick on block 1 while holding a stick to continue");
	}
	
	public void cmd_sphere(CommandSender sender, Material material, byte material_data, boolean aironly,boolean hollow) {
		if (!plugin.getConfigValue("FEATURE_BUILDER_SPHERE")) {
			return;
		}
		if (plugin.checkPlayerHasStartedBuilding((Player)sender)) {
			plugin.removeStartedBuilding((Player)sender);
			sender.sendMessage(ChatColor.YELLOW+"previous started building aborted.");
		}
		plugin.getStartedBuildings().add(new Buildr_Manager_Builder_Sphere((Player)sender, material, aironly, hollow, false, plugin,material_data));
		String buildinfo ="Started new Sphere. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") Aironly: "+ChatColor.BLUE+aironly+ChatColor.WHITE+" Hollow: "+ChatColor.BLUE+hollow;
		sender.sendMessage(buildinfo);
		sender.sendMessage("Rightclick on the center of your sphere while holding a stick to continue");
	}
	
	public void cmd_half_sphere(CommandSender sender, Material material, byte material_data, boolean aironly,boolean hollow) {
		if (!plugin.getConfigValue("FEATURE_BUILDER_SPHERE")) {
			return;
		}
		if (plugin.checkPlayerHasStartedBuilding((Player)sender)) {
			plugin.removeStartedBuilding((Player)sender);
			sender.sendMessage(ChatColor.YELLOW+"previous started building aborted.");
		}
		plugin.getStartedBuildings().add(new Buildr_Manager_Builder_Sphere((Player)sender, material, aironly, hollow, true, plugin,material_data));
		String buildinfo ="Started new half sphere. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") Aironly: "+ChatColor.BLUE+aironly+ChatColor.WHITE+" Hollow: "+ChatColor.BLUE+hollow;
		sender.sendMessage(buildinfo);
		sender.sendMessage("Rightclick on the center of your sphere while holding a stick to continue");
	}
	
	public void cmd_cylinder(CommandSender sender, Material material, byte material_data, boolean aironly,boolean hollow) {
		if (!plugin.getConfigValue("FEATURE_BUILDER_CYLINDER")) {
			return;
		}
		if (plugin.checkPlayerHasStartedBuilding((Player)sender)) {
			plugin.removeStartedBuilding((Player)sender);
			sender.sendMessage(ChatColor.YELLOW+"previous started building aborted.");
		}
		plugin.getStartedBuildings().add(new Buildr_Manager_Builder_Cylinder((Player)sender, material, aironly, hollow, plugin,material_data));
		String buildinfo ="Started new cylinder. Info: Blocktype: "+ChatColor.BLUE+material.toString()+ChatColor.WHITE+" (ID:"+ChatColor.BLUE+material.getId()+ChatColor.WHITE+") Aironly: "+ChatColor.BLUE+aironly+ChatColor.WHITE+" Hollow: "+ChatColor.BLUE+hollow;
		sender.sendMessage(buildinfo);
		sender.sendMessage("Rightclick on the center of your cylinder while holding a stick to continue");
	}

	/**
	 * 
	 * @param sender
	 */
	public void cmd_clrInv(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_CLEAR_INVENTORY")) {
			return;
		}
		((Player)sender).getInventory().clear();
		sender.sendMessage("Inventory cleared");
	}

	/**
	 * 
	 * @param sender
	 */
	public void cmd_location(CommandSender sender) {
		if (!plugin.getConfigValue("FEATURE_LOCATION")) {
			return;
		}
		Block block = ((Player)sender).getLocation().getBlock();
		String pos = "["+ChatColor.BLUE+block.getX()+ChatColor.WHITE+", "+ChatColor.BLUE+(block.getY()-1)+ChatColor.WHITE+", "+ChatColor.BLUE+block.getZ()+ChatColor.WHITE+"]";
		sender.sendMessage("Coordinates of the block beneath you: "+pos);
		
	}

	public void cmd_allowbuild(CommandSender sender) {
		if (!plugin.getConfigValue("BUILDMODE_REQUIRE_ALLOW")) {
			return;
		}
		World world = ((Player)sender).getWorld();
		if (plugin.checkWorldHasBuildmodeUnlocked(world)) {
			plugin.getWorldBuildmodeAllowed().remove(world);
			for (Player player : world.getPlayers()) {
				if (plugin.checkPlayerBuildMode(player)) {
					plugin.leaveBuildmode(player);
				}
				player.sendMessage(((Player)sender).getName()+" locked the Buildmode in this world");
			}
		}
		else {
			plugin.getWorldBuildmodeAllowed().add(world);
			for (Player player : world.getPlayers()) {
				player.sendMessage(((Player)sender).getName()+" unlocked the Buildmode in this world");
			}
		}
	}
}