package me.esmaybe.prophecy.managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Wand {

    public abstract String getName();

    public abstract ItemStack getItem();

    public abstract HashMap<Integer, Spell> getSpells();
    public abstract void loadSpells();

}
