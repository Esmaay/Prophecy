package me.esmaybe.prophecy.managers.wandmanager.spells.prophecy;

import me.esmaybe.prophecy.managers.Spell;
import me.esmaybe.prophecy.utils.wandutils.FireworkUtils;
import me.esmaybe.prophecy.utils.wandutils.HelperUtils;
import me.esmaybe.prophecy.utils.wandutils.SpellEffect;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class WitchWaveSpell extends Spell implements SpellEffect {

    @Override
    public String getName() {
        return "WitchWave";
    }

    @Override
    public void execute(Player player) {
        HelperUtils.makeTrail(player, 4, 20, 1, 1, this);
    }


    public void playEffect(Location loc) {
        FireworkUtils.playFirework(loc, FireworkEffect.Type.BURST, Color.GREEN, Color.BLACK, false, true);
        FireworkUtils.playFirework(loc, FireworkEffect.Type.BURST, Color.GREEN, Color.BLACK, false, true);
        FireworkUtils.playFirework(loc, FireworkEffect.Type.BURST, Color.GREEN, Color.BLACK, false, true);
        FireworkUtils.playFirework(loc, FireworkEffect.Type.BURST, Color.BLUE, Color.BLACK, false, true);
        HelperUtils.potion(loc, PotionEffectType.CONFUSION, 400, 2);
        HelperUtils.potion(loc, PotionEffectType.POISON, 400, 2);
        HelperUtils.potion(loc, PotionEffectType.HUNGER, 400, 2);
    }
}
