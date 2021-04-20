package me.esmaybe.prophecy.managers.wandmanager.wands;

import me.esmaybe.prophecy.VOGWands;
import me.esmaybe.prophecy.managers.Spell;
import me.esmaybe.prophecy.managers.Wand;
import me.esmaybe.prophecy.managers.wandmanager.spells.prophecy.*;
import me.esmaybe.prophecy.utils.ChatUtils;
import me.esmaybe.prophecy.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Prophecy extends Wand {

    private final String name;
    private final ItemStack item;
    private final HashMap<Integer, Spell> spells = new HashMap<>();

    public Prophecy(VOGWands VOGWands) {
        this.name = "Prophecy";
        this.item = new ItemBuilder(Material.GHAST_TEAR).setDisplayName(ChatUtils.format("&8» &6Prophecy")).build();
        loadSpells();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public HashMap<Integer, Spell> getSpells() {
        return this.spells;
    }

    public void loadSpells() {
        spells.put(1, new SparkSpell());
        spells.put(2, new HealWaveSpell());
        spells.put(3, new TeslaStrikeSpell());
        spells.put(4, new WitchWaveSpell());
        spells.put(5, new LaunchSpell());
        spells.put(6, new BurstSpell());
        spells.put(7, new EarthSmashSpell());
        spells.put(8, new InfectSpell());
        spells.put(9, new StatueSpell());
    }

}
