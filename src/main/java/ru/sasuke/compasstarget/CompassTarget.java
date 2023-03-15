package ru.sasuke.compasstarget;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class CompassTarget extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Objects.requireNonNull(getCommand("targetcompass")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("usage"))));
            return true;
        }

        if(args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("usage"))));
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("reload-msg"))));
            return true;
        }

        Player p = (Player) sender;

        if(!(p.getInventory().getItemInHand().getType() == Material.COMPASS)) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("nocompass"))));
            return true;
        }


        try {
            int x = Integer.parseInt(args[0]);
            int z = Integer.parseInt(args[1]);


            for(String key : getConfig().getConfigurationSection("worlds").getKeys(false)) {
                int x1 = getConfig().getInt("worlds." + key + ".x1");
                int x2 = getConfig().getInt("worlds." + key + ".x2");
                int z1 = getConfig().getInt("worlds." + key + ".z1");
                int z2 = getConfig().getInt("worlds." + key + ".z2");
                if(Bukkit.getWorld(key).equals(p.getWorld())) {
                    if(x > x1 || x < x2 || z > z1 || z < z2) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("set-error"))));
                        return true;
                    }
                }
            }

            p.setCompassTarget(new Location(p.getWorld(), x, 0, z));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Objects.requireNonNull(getConfig().getString("set-success")).replace("%x", args[0]).replace("%z", args[1]))));
        } catch (IllegalArgumentException ignored) {
        }


        return false;
    }


}
