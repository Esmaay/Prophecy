package me.esmaybe.prophecy.commands;

import me.esmaybe.prophecy.VOGWands;
import me.esmaybe.prophecy.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProphecyCommand implements CommandExecutor {

    private VOGWands VOGWands;

    public ProphecyCommand(VOGWands VOGWands) {
        this.VOGWands = VOGWands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage(ChatUtils.format("&cAlleen spelers kunnen dit doen!")); return true; }
        Player player = (Player) sender;
        if (!player.hasPermission("prophecy.use")) {
            player.sendMessage(ChatUtils.format("&cJe kunt dit niet doen!"));
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(ChatUtils.format("&7/prophecy get"));
            return true;
        }
        if (args[0].equalsIgnoreCase("get")) {
            if (!player.hasPermission("prophecy.get")) {
                player.sendMessage(ChatUtils.format("&cJe kunt dit niet doen!"));
                return true;
            }
            VOGWands.getWandManager().giveItem(player);
        }
        return true;
    }
}
