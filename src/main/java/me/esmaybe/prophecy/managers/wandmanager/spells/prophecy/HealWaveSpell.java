package me.esmaybe.prophecy.managers.wandmanager.spells.prophecy;

import me.esmaybe.prophecy.managers.Spell;
import me.esmaybe.prophecy.utils.wandutils.FireworkUtils;
import me.esmaybe.prophecy.utils.wandutils.HelperUtils;
import me.esmaybe.prophecy.utils.wandutils.SpellEffect;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class HealWaveSpell extends Spell implements SpellEffect {

    @Override
    public String getName() {
        return "HealWave";
    }

    @Override
    public void execute(Player player) {
        HelperUtils.makeTrail(player, 4, 30, 1, 2, this);
    }

    @Override
    public void playEffect(Location loc) {
        FireworkUtils.playFirework(loc, FireworkEffect.Type.BURST, Color.WHITE, Color.LIME, false, true);
        loc.getWorld().spawnParticle(Particle.HEART, loc, 20);
        HelperUtils.potion(loc, PotionEffectType.REGENERATION, 5 * 20, 100);
    }
}
