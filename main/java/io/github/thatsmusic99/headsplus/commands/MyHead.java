package io.github.thatsmusic99.headsplus.commands;

import io.github.thatsmusic99.headsplus.HeadsPlus;
import io.github.thatsmusic99.headsplus.config.HeadsPlusConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class MyHead implements CommandExecutor {

    private HeadsPlusConfig hpc = HeadsPlus.getInstance().hpc;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("myhead")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
                return false;
            }
            HeadsPlus.getInstance().saveConfig();
            List<String> bl = new ArrayList<>();
            for (String str : HeadsPlus.getInstance().getConfig().getStringList("blacklist")) {
                bl.add(str.toLowerCase());
            }
            List<String> wl = new ArrayList<>();
            for (String str : HeadsPlus.getInstance().getConfig().getStringList("whitelist")) {
                wl.add(str.toLowerCase());
            }

            boolean blacklistOn = HeadsPlus.getInstance().getConfig().getBoolean("blacklistOn");
            boolean wlOn = HeadsPlus.getInstance().getConfig().getBoolean("whitelistOn");
            String head = sender.getName().toLowerCase();
            if (((Player) sender).getInventory().firstEmpty() == -1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', hpc.getMessages().getString("full-inv")));
                return true;
            }
            if (wlOn) {
                if (blacklistOn) {
                    if (wl.contains(head)) {
                        if (!bl.contains(head)) {
                            giveHead((Player) sender, sender.getName());
                            return true;
                        } else if (sender.hasPermission("headsplus.bypass.blacklist")) {
                            giveHead((Player) sender, sender.getName());
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("blacklist-head"))));
                            return true;
                        }
                    } else if (sender.hasPermission("headsplus.bypass.whitelist")) {
                        if (!bl.contains(head)) {
                            giveHead((Player) sender, sender.getName());
                            return true;
                        } else if (sender.hasPermission("headsplus.bypass.blacklist")) {
                            giveHead((Player) sender, sender.getName());
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("blacklist-head"))));
                            return true;
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("whitelist-head"))));
                        return true;
                    }
                } else {
                    if (wl.contains(head)) {
                        giveHead((Player) sender, sender.getName());
                        return true;
                    } else if (sender.hasPermission("headsplus.bypass.whitelist")){
                        giveHead((Player) sender, sender.getName());
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("whitelist-head"))));
                        return true;
                    }
                }
            } else {
                if (blacklistOn) {
                    if (!bl.contains(head)) {
                        giveHead((Player) sender, sender.getName());
                        return true;
                    } else if (sender.hasPermission("headsplus.bypass.blacklist")){
                        giveHead((Player) sender, sender.getName());
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("blacklist-head"))));
                        return true;
                    }
                } else {
                    giveHead((Player) sender, sender.getName());
                    return true;
                }
            }
        }
        return false;
    }
    private static void giveHead(Player p, String n) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(n);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().hpch.getHeads().getString("player.display-name").replaceAll("%d", n)));
        skull.setItemMeta(meta);
        Location playerLoc = (p).getLocation();
        double playerLocY = playerLoc.getY() + 1;
        playerLoc.setY(playerLocY);
        World world = (p).getWorld();
        world.dropItem(playerLoc, skull).setPickupDelay(0);
    }
}
