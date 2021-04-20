package me.esmaybe.prophecy;

import lombok.Getter;
import me.esmaybe.prophecy.commands.ProphecyCommand;
import me.esmaybe.prophecy.listeners.DeathListener;
import me.esmaybe.prophecy.listeners.InteractListener;
import me.esmaybe.prophecy.listeners.ProjectileListener;
import me.esmaybe.prophecy.managers.wandmanager.WandManager;
import me.esmaybe.prophecy.managers.wandmanager.tempblock.TempBlock;
import me.esmaybe.prophecy.managers.wandmanager.tempblock.TempBlockCreation;
import me.esmaybe.prophecy.utils.wandutils.FireworkUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class VOGWands extends JavaPlugin {

    @Getter private WandManager wandManager;
    @Getter private FireworkUtils fireworkUtils;

    @Getter public static VOGWands instance;

    @Override
    public void onEnable() {
        instance = this;
        this.fireworkUtils = new FireworkUtils();
        this.wandManager = new WandManager(this);

        getCommand("prophecy").setExecutor(new ProphecyCommand(this));

        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileListener(), this);

    }

    @Override
    public void onDisable() {
        System.out.println("Reverting changes");
        TempBlockCreation.revertAll();
        TempBlock.removeAll();
        System.out.println("Reverted changes correctly");
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

}
