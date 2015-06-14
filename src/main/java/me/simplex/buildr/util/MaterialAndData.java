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
package me.simplex.buildr.util;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;


/**
 *
 * @author pwasson
 */
public class MaterialAndData {
    private final Material material;
    private final MaterialData data;


    public MaterialAndData(Material material,
            MaterialData data) {
        this.material = material;
        this.data = data;
    }


    public Material getMaterial() {
        return material;
    }


    public MaterialData getData() {
        return data;
    }


    public byte getDataIDorZero() {
        if (null == data)
            return 0;
        else
            return data.getData();
    }
    

    @Override
    public String toString() {
        String rez = material.toString();
        if (null != data) rez = rez + ":" + data.toString();
        return rez;
    }
}
