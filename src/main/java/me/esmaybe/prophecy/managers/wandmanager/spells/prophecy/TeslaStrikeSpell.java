package me.esmaybe.prophecy.managers.wandmanager.spells.prophecy;

import me.esmaybe.prophecy.managers.Spell;
import me.esmaybe.prophecy.utils.wandutils.FireworkUtils;
import me.esmaybe.prophecy.utils.wandutils.HelperUtils;
import me.esmaybe.prophecy.utils.wandutils.SpellEffect;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeslaStrikeSpell extends Spell implements SpellEffect {
    @Override
    public String getName() {
        return "TeslaStrike";
    }

    @Override
    public void execute(Player player) {
        HelperUtils.makeTrail(player, 4, 5, 1, 1, this);
    }

    @Override
    public void playEffect(Location loc) {
        loc.getWorld().strikeLightning(loc);
        HelperUtils.damage(loc, 17D);
        FireworkUtils.playFirework(loc, FireworkEffect.Type.BURST, Color.LIME, Color.GREEN, false, true);
    }
}
