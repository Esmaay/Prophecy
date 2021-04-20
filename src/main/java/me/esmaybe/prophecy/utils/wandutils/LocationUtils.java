package me.esmaybe.prophecy.utils.wandutils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static Location getPlayerBlock(Player player, int range) {
        return player.getTargetBlock(null, range).getLocation();
    }



}
