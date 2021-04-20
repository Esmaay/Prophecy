package me.esmaybe.prophecy.listeners;

import me.esmaybe.prophecy.managers.wandmanager.tempblock.TempBlockCreation;
import me.esmaybe.prophecy.utils.wandutils.HelperUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileListener implements Listener {


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Snowball) {
            Snowball s = (Snowball) event.getEntity();
            if (s.getShooter() instanceof Player && s.getCustomName() != null) {
                if (s.getCustomName().equals("iceinfectsnowball")) {
                    for (Location loca : HelperUtils.GenerateSphere(s.getLocation(), 7, false)) {
                        if (!loca.getBlock().getType().equals(Material.AIR)) {
                            if (!HelperUtils.isUnbreakable(loca.getBlock())) {
                                new TempBlockCreation(loca.getBlock(), Material.JUNGLE_LEAVES, Material.JUNGLE_LEAVES.createBlockData(), 400000, true);
                            }
                        }
                    }
                    s.getWorld().playSound(s.getLocation(), Sound.BLOCK_ANVIL_PLACE, 100.0F, 1.0F);
                    s.getWorld().spawnParticle(Particle.CLOUD, s.getLocation(), 120, 2.5F, 2.5F, 2.5F, 0.01F);
                }
            }
        }
    }
}
