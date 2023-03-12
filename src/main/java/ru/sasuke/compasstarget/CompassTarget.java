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

import java.util.ArrayList;
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

        Player p = (Player) sender;

        if(!(p.getInventory().getItemInHand().getType() == Material.COMPASS)) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("nocompass"))));
            return true;
        }

        for(String wName : getConfig().getStringList("blocked-worlds")) {
            if(Bukkit.getWorld(wName).equals(p.getWorld())) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("block-world"))));
                return true;
            }
        }


        try {
            int x = Integer.parseInt(args[0]);
            int z = Integer.parseInt(args[1]);
            if(x > getConfig().getInt("x1") || x < getConfig().getInt("x2") || z > getConfig().getInt("z1") || z < getConfig().getInt("z2")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("set-error"))));
                return true;
            }

            p.setCompassTarget(new Location(p.getWorld(), x, 0, z));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Objects.requireNonNull(getConfig().getString("set-success")).replace("%x", args[0]).replace("%z", args[1]))));
        } catch (IllegalArgumentException ignored) {
        }


        return false;
    }


}
