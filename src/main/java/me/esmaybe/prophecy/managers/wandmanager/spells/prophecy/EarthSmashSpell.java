package me.esmaybe.prophecy.managers.wandmanager.spells.prophecy;

import me.esmaybe.prophecy.VOGWands;
import me.esmaybe.prophecy.managers.Spell;
import me.esmaybe.prophecy.utils.wandutils.HelperUtils;
import me.esmaybe.prophecy.utils.wandutils.TargetUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EarthSmashSpell extends Spell {
    @Override
    public String getName() {
        return "EarthSmash";
    }

    @Override
    public void execute(Player player) {
        Location start = player.getLocation().clone().subtract(0.0D, 1.0D, 0.0D);
        Location curr = start.clone();
        double width = 4.0;
        new BukkitRunnable() {
            int length = 0;
            int maxLength = 24;

            public void run() {
                if (this.length > this.maxLength) {
                    cancel();
                    return;
                }

                this.length += 1;

                for (double i = -width; i <= width; i += 0.5D) {
                    Location check = curr.clone();
                    if (i != 0.0D) {
                        Vector dir = HelperUtils.getOrthogonalVector(check.getDirection(), 90.0D, i);
                        check.add(dir);
                    }
                    playEffect(check.getBlock(), player);
                }
                curr.add(start.getDirection().multiply(1));
            }
        }.runTaskTimer(VOGWands.getInstance(), 1L, 3L);
    }

    private void playEffect(Block b, Player p) {
        b = getTopBlock(b.getLocation());

        if (b.getType() == Material.AIR) {
            return;
        } else {
            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());

            for (Entity e : TargetUtils.getEntitiesAroundPoint(b.getLocation(), 2)) {
                if ((e instanceof LivingEntity) && e.getEntityId() != p.getEntityId()) {
                    e.setVelocity(new Vector(0.0, 1.5, 0.0));
                }
            }
        }
    }

    public Block getTopBlock(Location loc) {
        Location l = HelperUtils.getTopBlock(loc, 2).getLocation().add(0, 1.0D, 0);
        Block b = l.getBlock();

        b = b.getRelative(BlockFace.DOWN);
        return b;
    }


}
