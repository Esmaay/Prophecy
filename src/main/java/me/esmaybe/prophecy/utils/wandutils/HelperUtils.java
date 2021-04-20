package me.esmaybe.prophecy.utils.wandutils;

import me.esmaybe.prophecy.VOGWands;
import me.esmaybe.prophecy.managers.wandmanager.tempblock.TempBlockCreation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelperUtils {

    private static final Material[] unbreakables = {Material.BEDROCK, Material.BARRIER, Material.NETHER_PORTAL, Material.END_PORTAL, Material.END_PORTAL_FRAME, Material.CHEST, Material.ENDER_CHEST, Material.TRAPPED_CHEST, Material.DISPENSER, Material.DROPPER, Material.FURNACE, Material.BLAST_FURNACE, Material.SMOKER, Material.BARREL};

    public static void damage(Location loc, double damage) {
        for (LivingEntity l : EntityUtils.getMonsters(loc))
            l.damage(damage);
    }

    public static void shoot(Location loc, int force) {
        for (LivingEntity l : EntityUtils.getMonsters(loc))
            l.setVelocity(new Vector(0, force, 0));
    }

    public static void flame(Location loc, int duration) {
        for (LivingEntity l : EntityUtils.getMonsters(loc))
            l.setFireTicks(duration * 20);
    }

    public static void potion(Location loc, PotionEffectType type, int duration, int amplifier) {
        for (LivingEntity l : EntityUtils.getMonsters(loc))
            l.addPotionEffect(new PotionEffect(type, duration, amplifier));
    }

    public static void makeTrail(Player p, int waitBeforeStart, final int maxRange, final int blocksBetween, int ticksBetween, final SpellEffect effect) {
        final BlockIterator blocks = new BlockIterator((LivingEntity)p);
        int countUp = 0;
        while (countUp++ < waitBeforeStart && blocks.hasNext())
            blocks.next();
        (new BukkitRunnable() {
            int timer = 0;

            public void run() {
                int blockSkipper = blocksBetween;
                while (blockSkipper-- != 0 && blocks.hasNext())
                    blocks.next();
                if (this.timer++ < maxRange && blocks.hasNext()) {
                    Block b = blocks.next();
                    if (b.getType() != Material.AIR) {
                        cancel();
                        return;
                    }
                } else {
                    cancel();
                    return;
                }
                if (blocks.hasNext()) {
                    Location loc = blocks.next().getLocation();
                    effect.playEffect(loc);
                }
            }
        }).runTaskTimer(VOGWands.getInstance(), -1L, ticksBetween);
    }

    public static List<Location> GenerateSphere(Location Center, int Radius, boolean hollow) {
        List<Location> CircleBlocks = new ArrayList<Location>();
        int Bx = Center.getBlockX();
        int By = Center.getBlockY();
        int Bz = Center.getBlockZ();
        for (int x = Bx - Radius; x <= Bx + Radius; x++)
            for (int y = By - Radius; y <= By + Radius; y++)
                for (int z = Bz - Radius; z <= Bz + Radius; z++) {
                    double distance = (Bx - x) * (Bx - x) + (Bz - z) * (Bz - z) + (By - y) * (By - y);
                    if ((distance < Radius * Radius) && ((!hollow) || (distance >= (Radius - 1) * (Radius - 1)))) {
                        Location l = new Location(Center.getWorld(), x, y, z);
                        CircleBlocks.add(l);
                    }
                }
        return CircleBlocks;
    }

    public static boolean isUnbreakable(Block block) {
        return Arrays.asList(unbreakables).contains(block.getType());
    }

    public static List<Location> getCircle(Location loc, int radius, int height, boolean hollow, boolean sphere, double plusY) {
        List<Location> circleblocks = new ArrayList();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - radius; x <= cx + radius; x++)
            for (int z = cz - radius; z <= cz + radius; z++)
                for (int y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if ((dist < radius * radius) && ((!hollow) || (dist >= (radius - 1) * (radius - 1)))) {
                        Location l = new Location(loc.getWorld(), x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
        return circleblocks;
    }

    public static Block getTopBlock(Location loc, int range) {
        return getTopBlock(loc, range, range);
    }

    public static Block getTopBlock(Location loc, int positiveY, int negativeY) {
        Block blockHolder = loc.getBlock();
        int y = 0;
        while ((blockHolder.getType() != Material.AIR) && (Math.abs(y) < Math.abs(positiveY))) {
            y++;
            Block tempBlock = loc.clone().add(0.0D, y, 0.0D).getBlock();
            if (tempBlock.getType() == Material.AIR || tempBlock.isPassable()) {
                return blockHolder;
            }
            blockHolder = tempBlock;
        }
        while ((blockHolder.getType() == Material.AIR) && (Math.abs(y) < Math.abs(negativeY))) {
            y--;
            blockHolder = loc.clone().add(0.0D, y, 0.0D).getBlock();
            if (blockHolder.getType() != Material.AIR && !blockHolder.isPassable()) {
                return blockHolder;
            }
        }
        return blockHolder;
    }

    public static void createBoom(Location loc, int regentime, int radius, double damage) {
        for (Location loca : getCircle(loc, radius, 0, false, true, -1)) {
            if (!isUnbreakable(loca.getBlock())) {
                new TempBlockCreation(loca.getBlock(), Material.AIR, Material.AIR.createBlockData(), regentime, true);
            }
        }

        for (Entity e : TargetUtils.getEntitiesAroundPoint(loc, radius)) {
            if (e instanceof LivingEntity) ((LivingEntity) e).damage(damage);
        }

        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1, 0D, 0D, 0D, 0.01);
    }

    public static Vector getOrthogonalVector(Vector axis, double degrees, double length) {
        Vector ortho = new Vector(axis.getY(), -axis.getX(), 0.0D);
        ortho = ortho.normalize();
        ortho = ortho.multiply(length);

        return rotateVectorAroundVector(axis, ortho, degrees);
    }

    public static Vector rotateVectorAroundVector(Vector axis, Vector rotator, double degrees) {
        double angle = Math.toRadians(degrees);
        Vector rotation = axis.clone();
        Vector rotate = rotator.clone();
        rotation = rotation.normalize();

        Vector thirdaxis = rotation.crossProduct(rotate).normalize().multiply(rotate.length());

        return rotate.multiply(Math.cos(angle)).add(thirdaxis.multiply(Math.sin(angle)));
    }


    public static ArrayList<Block> getBlocksAroundCenter(Location loc, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();

        for (int x = (loc.getBlockX() - radius); x <= (loc.getBlockX() + radius); x++) {
            for (int y = (loc.getBlockY() - radius); y <= (loc.getBlockY() + radius); y++) {
                for (int z = (loc.getBlockZ() - radius); z <= (loc.getBlockZ() + radius); z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if (l.distance(loc) <= radius) {
                        blocks.add(l.getBlock());
                    }
                }
            }
        }
        return blocks;
    }



}
