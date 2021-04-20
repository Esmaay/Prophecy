package me.esmaybe.prophecy.managers.wandmanager;

import me.esmaybe.prophecy.VOGWands;
import me.esmaybe.prophecy.managers.Wand;
import me.esmaybe.prophecy.managers.wandmanager.spells.prophecy.*;
import me.esmaybe.prophecy.managers.wandmanager.wands.Prophecy;
import me.esmaybe.prophecy.utils.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class WandManager {

    private ArrayList<Prophecy> wands = new ArrayList<>();

    public WandManager(VOGWands VOGWands) {
        wands.add(new Prophecy(VOGWands));
    }

    public void giveItem(Player player, Wand wand) {
        ItemStack item = wand.getItem();
        item.setLore(Collections.singletonList(ChatUtils.format("&k1")));
        player.getInventory().addItem(item);
        player.sendMessage(ChatUtils.format("&eJe hebt de &c" + wand.getName() + " &egekregen!"));
        String lore = Objects.requireNonNull(item.getLore()).get(0);
        lore = ChatColor.stripColor(lore);
        player.sendMessage(ChatUtils.format("&eHuidige spell: &b" + wand.getSpells().get(Integer.parseInt(lore)).getName()));

    }
}
