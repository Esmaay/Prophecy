package me.esmaybe.prophecy.managers.wandmanager.spells.prophecy;

import me.esmaybe.prophecy.managers.Spell;
import me.esmaybe.prophecy.utils.wandutils.FireworkUtils;
import me.esmaybe.prophecy.utils.wandutils.HelperUtils;
import me.esmaybe.prophecy.utils.wandutils.LocationUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SparkSpell extends Spell {

    @Override
    public String getName() {
        return "Spark";
    }

    @Override
    public void execute(Player player) {
        Location loc = LocationUtils.getPlayerBlock(player, 20);
        FireworkUtils.playFirework(loc, FireworkEffect.Type.BURST, Color.BLACK, Color.LIME, true, true);
        HelperUtils.damage(loc, 17D);
    }
}
