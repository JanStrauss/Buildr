/*
 * Copyright 2015 s1mpl3x
 * Copyright 2015 pdwasson
 *
 * This file is part of Buildr.
 *
 * Buildr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Buildr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Buildr  If not, see <http://www.gnu.org/licenses/>.
 */
package me.simplex.buildr.manager.commands;

import me.simplex.buildr.Buildr;
import static me.simplex.buildr.manager.commands.Buildr_Manager_Command_Super.sendTo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * A Command handler that handles a few chores common to builder commands, i.e. those commands that build
 * stuff such as walls, cuboids, etc.
 * @author pwasson
 */
public abstract class AbstractBuilderCommand extends Buildr_Manager_Command_Super {
    protected final String commandName;
    protected final String permission;
    protected final int maxArgs;


    public AbstractBuilderCommand(Buildr plugin,
            String commandName,
            String permission,
            int maxArgs) {
        super(plugin);
        this.commandName = commandName;
        this.permission = permission;
        this.maxArgs = maxArgs;
    }


    @Override
    public final boolean onCommand(CommandSender sender,
            Command command,
            String label,
            String[] args) {
        if (!command.getName().equalsIgnoreCase(commandName)) {
            return false;
        }
        if (args.length < 1 || args.length > maxArgs) {
            return false;
        }
        if (!plugin.checkPermission((Player) sender, permission)) {
            sendTo(sender, MsgType.ERROR, "You do not have permission to perform this action");
            return true;
        }

        return onCommandSelf(sender, command, label, args);
    }


    /**
     * The builder-specific command handler. By the time this is called, the command name has already been
     * checked to ensure it matches, the permission has been checked, and the number of arguments has been
     * checked to make sure it is greater than or equal to one and less than or equal to <code>maxArgs</code>.
     * This method should then parse the arguments, do any other required checks, and set up the
     * Builder object.
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    protected abstract boolean onCommandSelf(CommandSender sender,
            Command command,
            String label,
            String[] args);
}
