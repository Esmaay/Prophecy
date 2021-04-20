package me.esmaybe.prophecy.utils.wandutils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {

    public List<LivingEntity> getEntities(int radius, Location loc) {
        List<LivingEntity> target = new ArrayList<LivingEntity>();
        Location tmp = new Location(loc.getWorld(), 0.0D, 0.0D, 0.0D);
        for (Entity entity : loc.getWorld().getEntities()) {
            if (entity.getLocation(tmp).distanceSquared(loc) < (radius * radius) &&
                    entity instanceof LivingEntity)
                target.add((LivingEntity)entity);
        }
        return target;
    }

    public static List<LivingEntity> getMonsters(Location loc) {
        List<LivingEntity> monsters = new ArrayList<LivingEntity>();
        for (Entity e : loc.getWorld().getEntities()) {
            if (e instanceof LivingEntity &&
                    e.getLocation().distance(loc) <= 3.0D)
                monsters.add((LivingEntity)e);
        }
        return monsters;
    }

}
