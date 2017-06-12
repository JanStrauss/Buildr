# Buildr

Demo video:
<a href="http://www.youtube.com/watch?feature=player_embedded&v=DyAL4RRUWeo
" target="_blank"><img src="http://img.youtube.com/vi/DyAL4RRUWeo/0.jpg" 
alt="Buildr video" width="240" height="180" border="10" /></a>

## FEATURES:
### Global build mode:
While a world is in the Globalbuildmode, it won't get dark and the weather won't change - so you can build without beeing interruped because you have to change the weather or set the time to see your creation again. Also blocks won't drop items in this world so the floor isn't flooded with useless items.

tl;dr:

* no nighttime -> alway sun
* no storm/thunder
* no itemdrops

### User build mode:
While a user is in buildmode blocks will break (almost) instantly if the player uses a Pickaxe (any of them), the inventory won't get flooded by useless items (pickups disabled), players won't get damaged, blocks are unlimited (stacksize is always 64). Last but not least cutting down trees is quite a pain, but while in buildmode players can fell a tree with just one click of an axe. You can Lock or Unlock the buildmode for a world or generally lock/unlock it, just have a closer look at the config/permissions.

tl;dr:

* Second inventory while in Buildmode
* Instant Block Break
* Godmode
* Unlimited Items
* No itempickups
* Treecutter removes trees instant

### Basic Structure builders:
You can build walls, floors/ceilings,cuboids,spheres,half spheres, cylinders easily with the build-commands of Buildr. Just type the command (see below) and click 2 blocks and Buildr creates these structures for you. The 3-Dimensional structures can be build hollow and all structures have a replace function. You can use wool as material with every structure builder. Just use wool:color as material in the command. And just like the Treecutter you can undo every action you perform with those builders with the /undo or /bu command.

    /wall builder

create Walls (or floors/ceilings) easily with just 2 clicks and this command usage: after you typed in the command you have to rightclick on the two blocks between the wall should be build while holding a stick. the line described by the 2 blocks must be parallel to at least one axis

    /wallx builder

allows you to create walls that aren't parallel to one of the axis

    /cuboid builder

allows you to create Cuboids (Great tool to begin your house with if you set it to hollow).

    /sphere builder

allows you to create a sphere. They can be solid or hollow.

    /halfsphere builder

allows you to create a half sphere. Can be solid or hollow.

    /cylinder builder

allows you to create a cylinder. They can be solid or hollow.

Example for the replace-function:
/cubeoid wool:red r1
this command will replace all stone blocks in the selected area with red wool
/wall 2 r3
this command will replace all dirt blocks in the selected area with grass


Other features:
/wool command
gives you a stack of wool in the specified color

/top command
Ports the user to the highest Block at its current position

/airfloor command
this command allows you to place one block or even a floor direct above yourself. usage: /airfloor

/gv command
gives items directly to a player or yourself.
"/gv 4" would give yourself a stack of 64 Cobbleston
"/gv stone 20" would give yourself a stack of 20 Stone
"/gv noob 20 40" would give noob a stack of 40 Glass

/gvx command
gives you a stack of items with the specified datavalue
/gvx 17:1 gives you redwood log


/clearinv command
wipes the inventory

/location command
gives the location of the block beneath the player

/port command
ports the player to the block he is currently facing.

/undo command
allows the user to undo his latest actions performed with the treecutter, airfloor tool and all builders (wall, cuboid, sphere etc.)
