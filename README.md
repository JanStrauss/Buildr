Buildr - Toolbox for creative players/servers
======

Buildr is a collection of useful tools to make building/creating stuff easier and faster.
The key features of Buildr are the two buildmodes. 

The Globalbuildmode that offers Worldbased enhancements to building, while the Userbuildmode gives a single player some handy tools.
While the modes are disabled you can play minecraft without 

Most of the tools are nothing new and can most likely be found in other plugins and because of this you can disable every tool if you already use a plugin that offers this functionality in the detailed configuration file. 

Builder supports permissions and has a fallback to OP if no permission plugin was found.

Global build mode:
------------------
While a world is in the Globalbuildmode, it won't get dark and the weather won't change - so you can build without beeing interruped because you have to change the weather or set the time to see your creation again.
Also blocks won't drop items in this world so the floor isn't flooded with useless items.

####tl;dr:

* no nighttime -> alway sun
* no storm/thunder
* no itemdrops

User build mode:
----------------
While a user is in buildmode the gets a special build inventory so he doesn't have to bother about which items to drop form his normal inventory to get space for all the different blocks to build with. Also blocks will break (almost) instantly if the player uses a Pickaxe (any of them), the inventory won't get flooded by useless items (pickups disabled), players won't get damaged, blocks are unlimited (stacksize is always 64). Last but not least cutting down trees is quite a pain, but while in buildmode players can fell a tree with just one click of an axe.

####tl;dr:

* Second inventory while in Buildmode
* Instant Block Break
* Godmode
* Unlimited Items
* No itempickups
* treecutter

OTHER FEATURES:
---------------

#### /wall command
create Walls (or floors/ceilings) easily with just 2 clicks and this command
usage: after you typed in the command you have to rightclick on the two blocks between the wall should be build while holding a stick. the line described by the 2 blocks must be parallel to at least one axis 

####/wool command
gives you a stack of wool in the specified color

####/top command
Ports the user to the highest Block at its current position

#### /airfloor command
this command allows you to place one block or even a floor direct above yourself.
usage: /airfloor <material (name or id)> <height (the block beneath you would be 0)> <size (square side length)>

####/give command
gives items directly to a player or yourself. 
`/give 4` would give yourself a stack of 64 Cobblestone;
`/give stone 20` would give yourself a stack of 20 Stone;
`/give noob 20 40` would give noob a stack of 40 Glass;


####/clearinv command
wipes the inventory 

####/location command
gives the location of the block beneath the player

####/undo command
allows the user to undo his latest actions performed with the treecutter, wallbuilder or airfloor tool.

	
COMMANDS:
---------
``` YML
commands:
  globalbuild:
    description: Toggles the Global Buildmode for the world you're in
    aliases: [gb,gbm]
    usage: |
           /<command>
           Example: /<command>
  build:
    description: Toggles the User Buildmode
    aliases: [ub,gbm,userbuild]
    usage: |
           /<command>
           Example: /<command>
  allowbuild:
    description: Unlocks/Locks the activation of the buildmode in the world you're in. Kicks all users out of buildmode on lock. Only has a Effect if BUILDMODE_BUILDMODE_REQUIRE_ALLOW is true
    aliases: [ab,abm,allowbm]
    usage: |
           /<command>
           Example: /<command>
  wall:
    description: Starts the wall building function
    aliases: [bw]
    usage: |
           /<command> <material #> {a|air|aironly}
  airfloor:
    description: places a block (area) of material x y blocks above the player
    aliases: [af,airf]
    usage: |
           /<command> <material #> <height #>
           Example: /<command> 4 20 3
  wool:
    description: gives a Stack of wool in the specified color to the player
    usage: |
           /<command> <color>
           Example: /<command> black
  top:
    description: ports the player to the highest block at its current position
    usage: |
           /<command> 
           Example: /<command> 
  undo:
    description: Undoes the lastest action the player used (airfloor, wall)
    aliases: [bu,buildrundo]
    usage: |
           /<command> 
           Example: /<command> 
  give:
    description: gives a Stack of items to the player
    aliases: [gv,g]
    usage: |
           /<command> <player> <itemid #|itemname> <amount #>
           Example: /<command> 20 50 gives a stack of 50 Glass to the player
  clearinv:
    description: clears the inventory of the player
    aliases: [clrinv,ci,clearinventory]
    usage: |
           /<command> 
           Example: /<command>
  location:
    description: returns the 
    aliases: [loc,gps]
    usage: |
           /<command> 
           Example: /<command>
```
PERMISSIONS:
-----------
####Commands:

`buildr.cmd.build` - /build command

`buildr.cmd.globalbuild` - /globalbuild command

`buildr.cmd.allow` - /allowbuild command

`buildr.cmd.wall` - /wall command

`buildr.cmd.airfloor` - /airfloor command

`buildr.cmd.top` - /top command

`buildr.cmd.undo` - /undo command

`buildr.cmd.give` - /give command

`buildr.cmd.clearinv` - /clearinv command

`buildr.cmd.loaction` - /location command


####Features:

`buildr.feature.treecutter` - /location command

rest is not done yet... might add it on demand but the limitations in the configfile should work for most situations

TODO:
-----
*	Better Wallbuilder, not just walls that are dimension parallel