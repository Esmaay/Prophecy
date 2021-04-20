package me.esmaybe.prophecy.managers.wandmanager.spells.prophecy;

import me.esmaybe.prophecy.managers.Spell;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class InfectSpell extends Spell {
    @Override
    public String getName() {
        return "Infect";
    }

    @Override
    public void execute(Player player) {
        Snowball s = player.launchProjectile(Snowball.class);
        Location l = player.getEyeLocation();
        Vector v = l.getDirection().multiply(3);
        s.setVelocity(v);
        s.setShooter(player);
        s.setCustomName("iceinfectsnowball");
    }
}
