package me.esmaybe.prophecy.managers;

import org.bukkit.entity.Player;

public abstract class Spell {

    public abstract String getName();

    public abstract void execute(Player player);

}
