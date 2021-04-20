package me.esmaybe.prophecy.managers.wandmanager.spells.prophecy;

import me.esmaybe.prophecy.VOGWands;
import me.esmaybe.prophecy.managers.Spell;
import me.esmaybe.prophecy.managers.wandmanager.tempblock.TempBlockCreation;
import me.esmaybe.prophecy.utils.wandutils.HelperUtils;
import me.esmaybe.prophecy.utils.wandutils.TargetUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StatueSpell extends Spell {
    @Override
    public String getName() {
        return "Statue";
    }

    @Override
    public void execute(Player player) {
        Snowball s = player.launchProjectile(Snowball.class);
        Location l = player.getEyeLocation();
        Vector v = l.getDirection().multiply(3);
        s.setVelocity(v);
        s.setShooter(player);

        new BukkitRunnable() {
            public void run() {
                if (!s.isValid()) {
                    this.cancel();
                }
                try {
                    if (s.isOnGround()) {
                        s.remove();
                    }
                    for (Entity e : TargetUtils.getTargetList(s.getLocation(), 3)) {
                        if (((e instanceof LivingEntity))
                                &&
                                (e != player)) {
                            Vector v = e.getVelocity().multiply(0).setY(0);
                            e.setVelocity(v);

                            if (e instanceof Player) {
                                ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
                                ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 160, 10));
                                ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 10));
                                ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
                                ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 160, 10));
                            }

                            new BukkitRunnable() {

                                public void run() {
                                    for (final Location l : HelperUtils.GenerateSphere(e.getLocation(), 3, true)) {
                                        if (l.getBlock().getType().equals(Material.AIR) || l.getBlock().getType().equals(Material.WATER)) {
                                            new TempBlockCreation(l.getBlock(), Material.IRON_BARS, Material.IRON_BARS.createBlockData(), 10000, true);

                                            new BukkitRunnable() {
                                                public void run() {
                                                    l.getBlock().setType(Material.AIR);
                                                }
                                            }.runTaskLater(VOGWands.getInstance(), 200);
                                        }
                                    }
                                }
                            }.runTaskLater(VOGWands.getInstance(), 2L);

                            new BukkitRunnable() {
                                int delay = 0;

                                public void run() {
                                    for (final Location l : HelperUtils.GenerateSphere(e.getLocation(), 4, true)) {
                                        if (l.getBlock().getType().equals(Material.AIR) || l.getBlock().getType().equals(Material.WATER)) {
                                            this.delay += 1;
                                            new BukkitRunnable() {
                                                public void run() {
                                                    new TempBlockCreation(l.getBlock(), Material.JUNGLE_LEAVES, Material.JUNGLE_LEAVES.createBlockData(), 10000, true);
                                                    Particles(s, l);

                                                    new BukkitRunnable() {
                                                        public void run() {
                                                            l.getBlock().setType(Material.AIR);
                                                            Particles(s, l);
                                                        }
                                                    }.runTaskLater(VOGWands.getInstance(), 200);
                                                }
                                            }.runTaskLater(VOGWands.getInstance(), this.delay);
                                        }
                                    }
                                }
                            }.runTaskLater(VOGWands.getInstance(), 2L);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(VOGWands.getInstance(), 0L, 1L);

        new BukkitRunnable() {
            public void run() {
                try {
                    if (!s.isValid()) {
                        cancel();
                    }

                    s.getWorld().spawnParticle(Particle.SNOW_SHOVEL, s.getLocation(), 100, 0.8F, 0.8F, 0.8F, 0.01F);
                    s.getWorld().spawnParticle(Particle.CLOUD, s.getLocation(), 100, 0.7F, 0.7F, 0.7F, 0.01F);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(VOGWands.getInstance(), 0L, 1L);
    }

    public void Particles(Snowball s, Location l) {
        l.getWorld().playEffect(l, Effect.STEP_SOUND, Material.JUNGLE_LEAVES);
        s.getWorld().spawnParticle(Particle.CLOUD, l, 10, 0.3F, 0.3F, 0.3F, 0.3F);
        s.getWorld().spawnParticle(Particle.CLOUD, l, 10, 0.3F, 0.3F, 0.3F, 0.3F);
    }
}
