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
import me.simplex.buildr.util.Buildr_Interface_Building;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


/**
 * A command handler that parses the arguments used by most of the builders: build material,
 * optional replace material, and optional <code>hollow</code> flag. Subclasses just need to
 * implement one method,
 * {@link #doBuildCommand(org.bukkit.command.CommandSender, org.bukkit.Material, byte, boolean, org.bukkit.Material) doBuildCommand},
 * to start the build UI process.
 * @author pwasson
 */
public abstract class StdArgBuilderCommand extends AbstractBuilderCommand {
    protected final boolean supportHollow;

    public StdArgBuilderCommand(Buildr plugin,
            String commandName,
            String permission) {
        super(plugin, commandName, permission, 3);
        this.supportHollow = true;
    }


    public StdArgBuilderCommand(Buildr plugin,
            String commandName,
            String permission,
            boolean inSupportHollow) {
        super(plugin, commandName, permission, inSupportHollow ? 3 : 2);
        this.supportHollow = inSupportHollow;
    }


    @Override
    protected final boolean onCommandSelf(CommandSender sender,
            Command command,
            String label,
            String[] args) {
        try {
            CommonArguments commonArgs = parseCommonArguments(args, supportHollow);

            doBuildCommand(sender, commonArgs.getBuildMaterial().getMaterial(),
                    commonArgs.getBuildMaterial().getDataIDorZero(), commonArgs.isHollow(),
                    (null != commonArgs.getReplaceMaterial()) ? commonArgs.getReplaceMaterial().getMaterial()
                            : null);
            return true;
        } catch (BadFormatException formatX) {
            sendTo(sender, Buildr_Manager_Command_Super.MsgType.ERROR,
                    formatX.getMessage());
            return true;
        }
    }


    /**
     * takes the building parameters parsed from the command arguments, tells the user what
     * it is about to start building and how to proceed, then constructs a new Builder object
     * and sends it to the plugin.
     * @param sender
     * @param material
     * @param material_data
     * @param hollow
     * @param replace_mat
     * @see Buildr#getStartedBuildings()
     * @see Buildr_Interface_Building
     */
    protected abstract void doBuildCommand(CommandSender sender,
            Material material,
            byte material_data,
            boolean hollow,
            Material replace_mat);
}
