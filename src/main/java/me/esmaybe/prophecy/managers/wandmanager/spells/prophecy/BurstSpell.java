package me.esmaybe.prophecy.managers.wandmanager.spells.prophecy;

import me.esmaybe.prophecy.VOGWands;
import me.esmaybe.prophecy.managers.Spell;
import me.esmaybe.prophecy.managers.wandmanager.tempblock.TempBlockCreation;
import me.esmaybe.prophecy.utils.wandutils.HelperUtils;
import me.esmaybe.prophecy.utils.wandutils.TargetUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class BurstSpell extends Spell {

    private ArrayList<UUID> isActive = new ArrayList();

    @Override
    public String getName() {
        return "Burst";
    }

    @Override
    public void execute(Player player) {
        if(this.isActive.contains(player.getUniqueId())) return;
        this.isActive.add(player.getUniqueId());

        for(Location loca : HelperUtils.GenerateSphere(player.getLocation(), 3, true)) {
            if (!HelperUtils.isUnbreakable(loca.getBlock())) {
                new TempBlockCreation(loca.getBlock(), Material.OBSIDIAN, Material.OBSIDIAN.createBlockData(), 10000, true);
            }
        }

        for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 100F, 1.0F);
        }

        new BukkitRunnable() {
            int stage = 1;
            public void run() {
                if (stage < 4) {
                    stage(player, stage);
                } else if (stage <= 8) {
                    stageTwo(player, stage);
                } else {
                    lastStage(player);
                    this.cancel();
                    isActive.remove(player.getUniqueId());
                    return;
                }

                stage++;
            }
        }.runTaskTimer(VOGWands.getInstance(), 10L, 10L);
    }

    public void stage(Player player, int stage) {
        for(Location loca : HelperUtils.GenerateSphere(player.getLocation(), 2 + stage, false)) {
            if (!HelperUtils.isUnbreakable(loca.getBlock())) {
                if (!loca.getBlock().getType().equals(Material.AIR) && !loca.getBlock().getType().equals(Material.OBSIDIAN)) {
                    new TempBlockCreation(loca.getBlock(), Material.OAK_LEAVES, Material.OAK_LEAVES.createBlockData(), 400000, true);
                }
            }
        }
    }

    public void stageTwo(Player player, int stage) {
        for(Location loc : HelperUtils.getCircle(player.getLocation(), 2 + stage, 1, true, false, -1)) {
            Location loca = HelperUtils.getTopBlock(loc, 4).getLocation();
            if (!HelperUtils.isUnbreakable(loca.getBlock())) {
                if (!loca.getBlock().getType().equals(Material.AIR) && !loca.getBlock().getType().equals(Material.OAK_LEAVES) && !loca.getBlock().getType().equals(Material.OBSIDIAN)) {
                    new TempBlockCreation(loca.getBlock(), Material.JUNGLE_LEAVES, Material.JUNGLE_LEAVES.createBlockData(), 300000, true);
                    loca.getWorld().playEffect(loca, Effect.STEP_SOUND, Material.JUNGLE_LEAVES);
                }
            }
        }
    }

    public void lastStage(Player player) {
        for(Location loca : HelperUtils.GenerateSphere(player.getLocation(), 6, false)) {
            player.getWorld().spawnParticle(Particle.CLOUD, loca, 2, 0.2F, 0.2F, 0.2F, 0.01F);
        }

        for(Entity entity : TargetUtils.getTargetList(player.getLocation(), 14)) {
            if (entity instanceof Player) {
                if(entity != player) {
                    ((Player) entity).damage(8D);
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 1));
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 24, 0));
                    ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 240, 1));
                }
            } else if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).damage(8D);
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 600, 1));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 24, 0));
                ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 240, 1));
            }
        }
    }


}
