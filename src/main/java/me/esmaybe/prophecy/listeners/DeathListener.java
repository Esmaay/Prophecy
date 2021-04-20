package me.esmaybe.prophecy.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        String damager = event.getDamager().toString();
        if ((event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
                && (damager.equalsIgnoreCase("CraftFirework"))) {
            event.setCancelled(true);
        }
    }

}
