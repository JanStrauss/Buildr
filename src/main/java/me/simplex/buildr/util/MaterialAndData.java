/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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


    @Override
    public String toString() {
        String rez = material.toString();
        if (null != data) rez = rez + ":" + data.toString();
        return rez;
    }
}
