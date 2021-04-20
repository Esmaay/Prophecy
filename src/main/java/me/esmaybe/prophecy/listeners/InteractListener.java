package me.esmaybe.prophecy.listeners;

import me.esmaybe.prophecy.VOGWands;
import me.esmaybe.prophecy.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class InteractListener implements Listener {

    private ArrayList<UUID> cooldown = new ArrayList<>();

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) == null) return;
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName().equals(ChatUtils.format("&8» &6Prophecy"))) {
            event.getPlayer().sendMessage(ChatUtils.format("&eJe hebt de &cProphecy &egeselecteerd!"));
            String lore = Objects.requireNonNull(event.getPlayer().getInventory().getItem(event.getNewSlot()).getLore()).get(0);
            lore = ChatColor.stripColor(lore);
            event.getPlayer().sendMessage(ChatUtils.format("&eHuidige spell: &b" + VOGWands.getInstance().getWandManager().getWand("Prophecy").getSpells().get(Integer.parseInt(lore)).getName()));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                ItemStack item = player.getInventory().getItemInMainHand();
                String itemName = item.getItemMeta().getDisplayName();
                if (itemName.equals(ChatUtils.format("&8» &6Prophecy"))) {
                    String lore = item.getLore().get(0);
                    lore = ChatColor.stripColor(lore);

                    VOGWands.getInstance().getWandManager().getWand("Prophecy").getSpells().get(Integer.valueOf(lore)).execute(player);
                }
            }
            return;
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (cooldown.contains(event.getPlayer().getUniqueId())) return;
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                ItemStack item = player.getInventory().getItemInMainHand();
                String itemName = item.getItemMeta().getDisplayName();
                if (itemName.equals(ChatUtils.format("&8» &6Prophecy"))) {
                    if (player.isSneaking()) {
                        String lore = item.getLore().get(0);
                        lore = ChatColor.stripColor(lore);
                        int currentSpell = Integer.parseInt(lore);
                        int newSpell = currentSpell - 1;
                        if (newSpell < 1) {
                            newSpell = VOGWands.getInstance().getWandManager().getWand("Prophecy").getSpells().size();
                        }
                        item.setLore(Collections.singletonList(ChatUtils.format("&k" + newSpell)));
                        player.sendMessage(ChatUtils.format("&7Je hebt &f" + VOGWands.getInstance().getWandManager().getWand("Prophecy").getSpells().get(newSpell).getName() + " &7geselecteerd!"));
                        cooldown.add(event.getPlayer().getUniqueId());
                    } else {
                        String lore = item.getLore().get(0);
                        lore = ChatColor.stripColor(lore);
                        int currentSpell = Integer.parseInt(lore);
                        int newSpell = currentSpell + 1;
                        if (newSpell > VOGWands.getInstance().getWandManager().getSpells().size()) {
                            newSpell = 1;
                        }
                        item.setLore(Collections.singletonList(ChatUtils.format("&k" + newSpell)));
                        player.sendMessage(ChatUtils.format("&7Je hebt &f" + VOGWands.getInstance().getWandManager().getSpells().get(newSpell).getName() + " &7geselecteerd!"));
                        cooldown.add(event.getPlayer().getUniqueId());
                    }
                    Bukkit.getScheduler().runTaskLater(VOGWands.getInstance(), () -> {
                        cooldown.remove(player.getUniqueId());
                    }, 1L);
                    return;
                }
            }
        }
    }

}
