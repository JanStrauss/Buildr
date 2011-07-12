Buildr - Toolbox for creative players/servers
=============================================

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
You can Lock or Unlock the buildmode for a world or generally lock/unlock it, just have a closer look at the config/permissions.

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


TODO:
-----
*	Non-parallel Wallbuilder
*	Linebuilder
*	Cuboidbuilder
*	Spherebuilder
*	Cylinderbuilder 
*	and other stuff

KNOWN ISSUES:
-------------
*	?