package me.esmaybe.prophecy.utils.wandutils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TargetUtils {

    public static List<Entity> getTargetList(Location loc, int radius) {
        List<Entity> target = new ArrayList<>();
        int rs = radius * radius;
        Location tmp = new Location(loc.getWorld(), 0.0D, 0.0D, 0.0D);
        for (Entity entity : loc.getWorld().getEntities()) {
            if (entity.getLocation(tmp).distanceSquared(loc) < rs) {
                target.add(entity);
            }
        }
        return target;
    }

    public static List<Entity> getEntitiesAroundPoint(Location location, double radius) {
        ArrayList<Entity> target = new ArrayList<>();
        World world = location.getWorld();
        int smallX = (int) (location.getX() - radius) >> 4;
        int bigX = (int) (location.getX() + radius) >> 4;
        int smallZ = (int) (location.getZ() - radius) >> 4;
        int bigZ = (int) (location.getZ() + radius) >> 4;
        for (int x = smallX; x <= bigX; ++x) {
            for (int z = smallZ; z <= bigZ; ++z) {
                if (!world.isChunkLoaded(x, z)) continue;
                target.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities()));
            }
        }
        Iterator<Entity> targetIterator = target.iterator();
        while (targetIterator.hasNext()) {
            Entity e = targetIterator.next();
            if (e.getWorld().equals(location.getWorld()) && e.getLocation().distanceSquared(location) > radius * radius) {
                targetIterator.remove();
                continue;
            }
            if (!(e instanceof Player) || !((Player) e).getGameMode().equals(GameMode.SPECTATOR)) continue;
            targetIterator.remove();
        }
        return target;
    }

    public List<Block> getBlocksAroundPoint(Location location, double radius) {
        ArrayList<Block> blocks = new ArrayList<>();
        int xorg = location.getBlockX();
        int yorg = location.getBlockY();
        int zorg = location.getBlockZ();
        int r = (int) radius * 4;
        for (int x = xorg - r; x <= xorg + r; ++x) {
            for (int y = yorg - r; y <= yorg + r; ++y) {
                for (int z = zorg - r; z <= zorg + r; ++z) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (block.getLocation().distanceSquared(location) > radius * radius) continue;
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }
}
