package io.github.thatsmusic99.headsplus.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.thatsmusic99.headsplus.HeadsPlus;
import io.github.thatsmusic99.headsplus.api.SellHeadEvent;
import io.github.thatsmusic99.headsplus.commands.maincommand.DebugPrint;
import io.github.thatsmusic99.headsplus.config.HeadsPlusConfig;
import io.github.thatsmusic99.headsplus.config.HeadsPlusConfigHeads;
import io.github.thatsmusic99.headsplus.config.headsx.HeadsPlusConfigHeadsX;
import io.github.thatsmusic99.headsplus.locale.LocaleManager;
import io.github.thatsmusic99.headsplus.nms.NMSManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SellHead implements CommandExecutor, IHeadsPlusCommand {
	
	private boolean sold;
	private final HeadsPlusConfigHeads hpch = HeadsPlus.getInstance().getHeadsConfig();
	private final HeadsPlusConfig hpc = HeadsPlus.getInstance().getMessagesConfig();
	private final HeadsPlusConfigHeadsX hpchx = HeadsPlus.getInstance().getHeadsXConfig();
	private final List<String> soldHeads = new ArrayList<>();
	private final HashMap<String, Integer> hm = new HashMap<>();
    private final String disabled = hpc.getString("disabled");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
			if (sender instanceof Player) {
			    if (HeadsPlus.getInstance().canSellHeads()) {

			        soldHeads.clear();
			        hm.clear();
                    ItemStack invi = checkHand((Player) sender);
                    if (args.length == 0 && (checkHand((Player) sender).getType() == Material.SKULL_ITEM) && (sender.hasPermission("headsplus.sellhead"))) { // If sold via hand
                        if (nms().getSkullOwnerName((SkullMeta) invi.getItemMeta()) == null || nms().getSkullOwnerName((SkullMeta) invi.getItemMeta()).equalsIgnoreCase("HPXHead")) {
                            if (nms().isSellable(invi)) {
                                for (String key : hpch.mHeads) {
                                    if (!key.equalsIgnoreCase("sheep")) {
                                        if (checkSkullForTexture((Player) sender, invi, hpch.getConfig().getStringList(key + ".name"), key, args)) {
                                            return true; // Ends because head is already sold
                                        }
                                    } else {
                                        if (checkColorSheep((Player) sender, args, invi)) {
                                            return true;
                                        }
                                    }
                                }
                                for (String key : hpch.uHeads) {
                                    if (checkSkullForTexture((Player) sender, invi, hpch.getConfig().getStringList(key + ".name"), key, args)) {
                                        return true;
                                    }
                                }
                            } else {
                                sender.sendMessage(hpc.getString("false-head"));
                                return true;
                            }
                        }
                        SkullMeta skullM = (SkullMeta) invi.getItemMeta();
                        String owner = nms().getSkullOwnerName(skullM);
		                if (nms().isSellable(invi)) {
                            Economy econ = HeadsPlus.getInstance().getEconomy();
                            List<String> mHeads = hpch.mHeads;
                            List<String> uHeads = hpch.uHeads;
                            for (String key : mHeads) {
                                if (key.equalsIgnoreCase("sheep")) {
                                    for (String s : hpch.getConfig().getConfigurationSection("sheep.name").getKeys(false)) {
                                        for (int i = 0; i < hpch.getConfig().getStringList("sheep.name." + s).size(); i++) {
                                            if (owner.equalsIgnoreCase(hpch.getConfig().getStringList("sheep.name." + s).get(i))) {
                                                a(invi, key, args, (Player) sender);
                                            }
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < hpch.getConfig().getStringList(key + ".name").size(); i++) {
                                        if (owner.equalsIgnoreCase(hpch.getConfig().getStringList(key + ".name").get(i))) {
                                            a(invi, key, args, (Player) sender);
                                            return true;
                                        } else if ((owner.matches(hpch.getConfig().getStringList("irongolem.name").get(i))) && (key.equalsIgnoreCase("irongolem"))) {
                                            a(invi, key, args, (Player) sender);
                                            return true;
                                        }
                                    }
                                }
                            }
                            for (String key : uHeads) {
                                if (owner.equalsIgnoreCase(hpch.getConfig().getString(key + ".name"))) {
                                    Double price = hpch.getConfig().getDouble(key + ".price");
                                    if (invi.getAmount() > 0) {
                                        price = setPrice(price, args, invi, (Player) sender);
                                        SellHeadEvent she = new SellHeadEvent(price, soldHeads, (Player) sender, econ.getBalance((Player) sender), econ.getBalance((Player) sender) + price, hm);
                                        Bukkit.getServer().getPluginManager().callEvent(she);
                                        if (!she.isCancelled()) {
                                            EconomyResponse zr = econ.depositPlayer((Player) sender, price);
                                            String success = hpc.getString("sell-success").replaceAll("%l", Double.toString(zr.amount)).replaceAll("%b", Double.toString(zr.balance));
                                            String fail = hpc.getString("sell-fail");
                                            if (zr.transactionSuccess()) {
                                                if (price > 0) {
                                                    itemRemoval((Player) sender, args, invi);
                                                    sender.sendMessage(success);
                                                    sold = true;
                                                }
                                            } else {
                                                sender.sendMessage(fail + ": " + zr.errorMessage);
                                            }
                                        }
                                    }
                                }
                            }
                            if (!sold) {
                                Double price = hpch.getConfig().getDouble("player.price");
                                if (invi.getAmount() > 0) {
                                    price = setPrice(price, args, invi, (Player) sender);
                                }
                                SellHeadEvent she = new SellHeadEvent(price, soldHeads, (Player) sender, econ.getBalance((Player) sender), econ.getBalance((Player) sender) + price, hm);
                                Bukkit.getServer().getPluginManager().callEvent(she);
                                if (!she.isCancelled()) {
                                    EconomyResponse zr = econ.depositPlayer((Player) sender, price);
                                    String success = hpc.getString("sell-success").replaceAll("%l", Double.toString(zr.amount)).replaceAll("%b", Double.toString(zr.balance));
                                    String fail = hpc.getString("sell-fail");
                                    if (zr.transactionSuccess()) {
                                        itemRemoval((Player) sender, args, invi);
                                        sender.sendMessage(success);
                                        sold = true;
                                    } else {
                                        sender.sendMessage(fail + ": " + zr.errorMessage);
                                    }
                                }
                            }
                            if (!sold) {
                                sender.sendMessage(hpc.getString("false-head"));
                            }
                        } else {
                            sender.sendMessage(hpc.getString("false-head"));
                        }
                    } else {
                        if (!sender.hasPermission("headsplus.sellhead")) {
                            sender.sendMessage(hpc.getString("no-perm"));
                        } else if (args.length > 0) {
                            if (args[0].equalsIgnoreCase("all")) {
                                sellAll((Player) sender, args, invi);
                            } else {
                                Player p = (Player) sender;
                                double price = 0.0;
                                for (ItemStack i : p.getInventory()) {
                                    if (i != null) {
                                        boolean found = false;
                                        if (i.getType() == Material.SKULL_ITEM) {
                                            if (i.getDurability() == 3) {
                                                SkullMeta sm = (SkullMeta) i.getItemMeta();
                                                for (String str : hpch.mHeads) {
                                                    try {
                                                        if (nms().getSkullOwnerName(sm) != null) {
                                                            if (str.equalsIgnoreCase("sheep")) {
                                                                for (String s : hpch.getConfig().getConfigurationSection(str + ".name").getKeys(false)) {
                                                                    for (int in = 0; in < hpch.getConfig().getStringList(str + ".name." + s).size(); in++) {
                                                                        if (nms().getSkullOwnerName(sm).equalsIgnoreCase(hpch.getConfig().getStringList(str + ".name." + s).get(in))) {
                                                                            found = true;
                                                                            price = setPrice(price, args, i, p);
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                for (int in = 0; in < hpch.getConfig().getStringList(str + ".name").size(); in++) {
                                                                    if (nms().getSkullOwnerName(sm).equalsIgnoreCase(hpch.getConfig().getStringList(str + ".name").get(in))) {
                                                                        found = true;
                                                                        price = setPrice(price, args, i, p);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (NullPointerException ex) {
                                                        //
                                                    }
                                                }
                                                for (String str : hpch.uHeads) {
                                                    try {
                                                        if (nms().getSkullOwnerName(sm) != null) {
                                                            if (nms().getSkullOwnerName(sm).equalsIgnoreCase(hpch.getConfig().getString(str + ".name"))) {
                                                                found = true;
                                                                price = setPrice(price, args, i, p);
                                                            }
                                                        }
                                                    } catch (NullPointerException ex) {
                                                        //
                                                    }
                                                }
                                                if (!found) {
                                                    if (nms().getSkullOwnerName(sm) != null) {
                                                        if (args[0].equalsIgnoreCase("player")) {
                                                            price = setPrice(price, args, i, p);
                                                        } else if (nms().getSkullOwnerName(sm).equalsIgnoreCase("HPXHead")) {
                                                            for (String key : hpch.mHeads) {
                                                                if (!key.equalsIgnoreCase("sheep")) {
                                                                    price = b(price, key, hpch.getConfig().getStringList(key + ".name"), i, (Player) sender, args, true, false);
                                                                } else {
                                                                    for (String s : hpch.getConfig().getConfigurationSection("sheep.name").getKeys(false)) {
                                                                        price = b(price, key, hpch.getConfig().getStringList(key + ".name." + s), i, (Player) sender, args, true, false);
                                                                    }
                                                                }
                                                            }
                                                            for (String key : hpch.uHeads) {
                                                                price = b(price, key, hpch.getConfig().getStringList(key + ".name"), i, (Player) sender, args, true, false);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } if (price == 0.0) {
                                    sender.sendMessage(hpc.getString("no-heads"));
                                    return true;
                                }
                                pay(p, args, new ItemStack(Material.AIR), price);
                            }
                        } else {
                            String falseItem = hpc.getString("false-item");
                            sender.sendMessage(falseItem);
                        }
                    }
                } else {
                    sender.sendMessage(disabled);
                }
            } else {
                sender.sendMessage("[HeadsPlus] You must be a player to run this command!");
            }
        } catch (Exception e) {
		    new DebugPrint(e, "Command (sellhead)", true, sender);
		}
        return false;
	}

	@SuppressWarnings("deprecation")
    private static ItemStack checkHand(Player p) {
		if (Bukkit.getVersion().contains("1.8")) {
			return p.getInventory().getItemInHand();
		} else {
			return p.getInventory().getItemInMainHand();
		}
	}
	@SuppressWarnings("deprecation")
	private void setHand(Player p, ItemStack i) {
		if (Bukkit.getVersion().contains("1.8")) {
			p.getInventory().setItemInHand(i);
		} else {
			p.getInventory().setItemInMainHand(i);
		}
	}
	private void itemRemoval(Player p, String[] a, ItemStack i) throws NoSuchFieldException, IllegalAccessException {
		if (a.length > 0) { 
			if (a[0].equalsIgnoreCase("all")) {
				for (ItemStack is : p.getInventory()) {
					if (is != null) {
						if (is.getType() == Material.SKULL_ITEM) {
                            if (nms().isSellable(is)) {
                                p.getInventory().remove(is);
                            }
					    }
					}
				}
			} else if (a[0].matches("^[0-9]+$")) { 
				if (i.getAmount() > Integer.parseInt(a[0])) {
					checkHand(p).setAmount(checkHand(p).getAmount() - Integer.parseInt(a[0]));
			    } else {
			    	setHand(p, new ItemStack(Material.AIR));
		        }
		    } else {
		    	for (ItemStack is : p.getInventory()) {
		    		if (is != null) {
		    			if (is.getType() == Material.SKULL_ITEM) {
		    				SkullMeta sm = (SkullMeta) is.getItemMeta();
                            if (nms().isSellable(is)) {
								boolean found = false;
								for (String s : hpch.mHeads) {
								    if (s.equalsIgnoreCase("sheep")) {
                                        for (String st : hpch.getConfig().getConfigurationSection("sheep.name").getKeys(false)) {
                                            found = c(s, a, is, hpch.getConfig().getStringList(s + ".name." + st), p);
                                        }
                                    } else {
                                        found = c(s, a, is, hpch.getConfig().getStringList(s + ".name"), p);
                                    }
							    }
							    for (String s : hpch.uHeads) {
                                    found = c(s, a, is, hpch.getConfig().getStringList(s + ".name"), p);
                                }
                                if (!found && a[0].equalsIgnoreCase("player")) {
                                    boolean player = true;
                                    SkullMeta sms = (SkullMeta) is.getItemMeta();
                                    for (String s : hpch.uHeads) {
                                        if (d(hpch.getConfig().getStringList(s + ".name"), sms)) {
                                            player = false;
                                            break;
                                        }
                                    }
                                    for (String s : hpch.mHeads) {
                                        if (s.equalsIgnoreCase("sheep")) {
                                            for (String st : hpch.getConfig().getConfigurationSection("sheep.name").getKeys(false)) {
                                                if (d(hpch.getConfig().getStringList(s + ".name." + st), sms)) {
                                                    player = false;
                                                    break;
                                                }
                                            }
                                        } else {
                                            if (d(hpch.getConfig().getStringList(s + ".name"), sms)) {
                                                player = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (player) {

                                        if (nms().isSellable(is)){
                                            p.getInventory().remove(is);
                                        }
                                    }
							    } else if (nms().getSkullOwnerName(sm).equalsIgnoreCase("HPXHead")) {
                                    for (String key : hpch.mHeads) {
                                        if (key.equalsIgnoreCase("sheep")) {
                                            for (String s : hpch.getConfig().getConfigurationSection("sheep.name").getKeys(false)) {
                                                b(0.0, key, hpch.getConfig().getStringList(key + ".name." + s), is, p, a, false, false);
                                            }
                                        } else {
                                            b(0.0, key, hpch.getConfig().getStringList(key + ".name"), is, p, a, false, false);
                                        }

                                    }
                                    for (String key : hpch.uHeads) {
                                        b(0.0, key, hpch.getConfig().getStringList(key + ".name"), is, p, a, false, false);
                                    }
                                }
							}
		    			}
		    		}
		    	}
		    }
		} else {
			setHand(p, new ItemStack(Material.AIR));
		}
	}
	private double setPrice(Double p, String[] a, ItemStack i, Player pl) throws NoSuchFieldException, IllegalAccessException {
		if (a.length > 0) { // More than one argument
			if (!a[0].matches("^[0-9]+$")) { // More than one head
				if (a[0].equalsIgnoreCase("all")) { // Sell everything
					if (i.getType().equals(Material.SKULL_ITEM)) {
						SkullMeta sm = (SkullMeta) i.getItemMeta();

                        if (nms().isSellable(i)) {
							boolean found = false;
							for (String s : hpch.mHeads) {
							    if (s.equalsIgnoreCase("sheep")) {
							        for (String st : hpch.getConfig().getConfigurationSection("sheep.name").getKeys(false)) {
							            if (!Objects.equals(e(hpch.getConfig().getStringList(s + ".name." + st), p, s, sm, i), p)) {
                                            p = e(hpch.getConfig().getStringList(s + ".name." + st), p, s, sm, i);
                                            soldHeads.add(s);
                                            i(s, i.getAmount());
                                            found = true;
                                            break;
                                        }
                                    }
                                } else {
                                    if (!Objects.equals(e(hpch.getConfig().getStringList(s + ".name"), p, s, sm, i), p)) {
                                        p = e(hpch.getConfig().getStringList(s + ".name"), p, s, sm, i);
                                        soldHeads.add(s);
                                        i(s, i.getAmount());
                                        found = true;
                                        break;
                                    }
                                }
						    }
						    for (String s : hpch.uHeads) {
                                if (!Objects.equals(e(hpch.getConfig().getStringList(s + ".name"), p, s, sm, i), p)) {
                                    p = e(hpch.getConfig().getStringList(s + ".name"), p, s, sm, i);
                                    soldHeads.add(s);
                                    i(s, i.getAmount());
                                    found = true;
                                    break;
                                }
						    }
                            if (!Objects.equals(e(hpch.getConfig().getStringList("irongolem.name"), p, "irongolem", sm, i), p)) {
                                p = e(hpch.getConfig().getStringList("irongolem.name"), p, "irongolem", sm, i);
                                soldHeads.add("irongolem");
                                i("irongolem", i.getAmount());
                                found = true;
                            }

						    if (nms().getSkullOwnerName(sm).equalsIgnoreCase("HPXHead")) {
                                for (String key : hpch.mHeads) {
                                    if (key.equalsIgnoreCase("sheep")) {
                                        for (String s : hpch.getConfig().getConfigurationSection("sheep.name").getKeys(false)) {
                                            if (!b(p, key, hpch.getConfig().getStringList(key + ".name." + s), i, pl, a, false, true).equals(p)) {
                                                p = b(p, key, hpch.getConfig().getStringList(key + ".name." + s), i, pl, a, false, true);
                                                soldHeads.add(key);
                                                i(key, i.getAmount());
                                                found = true;
                                                break;
                                            }
                                        }
                                    } else {
                                        if (!b(p, key, hpch.getConfig().getStringList(key + ".name"), i, pl, a, false, true).equals(p)) {
                                            p = b(p, key, hpch.getConfig().getStringList(key + ".name"), i, pl, a, false, true);
                                            soldHeads.add(key);
                                            i(key, i.getAmount());
                                            found = true;
                                            break;
                                        }
                                    }

                                }
                                for (String key : hpch.uHeads) {
                                    if (!b(p, key, hpch.getConfig().getStringList(key + ".name"), i, pl, a, false, true).equals(p)) {
                                        p = b(p, key, hpch.getConfig().getStringList(key + ".name"), i, pl, a, false, true);
                                        soldHeads.add(key);
                                        i(key, i.getAmount());
                                        found = true;
                                        break;
                                    }
                                }
                            }
						    if (!found) {
						    	p = p + (i.getAmount() * hpch.getConfig().getDouble("player.price"));
						    	soldHeads.add("player");
						    	i("player", i.getAmount());
						    }
						}
					}
				} else { // Selected mob
					for (String s : hpch.mHeads) { // All mobs with defined head names
						if (a[0].equalsIgnoreCase(s)) { // If the first arg e
						    SkullMeta sm = (SkullMeta) i.getItemMeta();
						    if (s.equalsIgnoreCase("sheep")) {
						        for (String st : hpch.getConfig().getConfigurationSection("sheep.name").getKeys(false)) {
                                    p = f(i, sm, p, s, hpch.getConfig().getStringList(s + ".name." + st));
                                }
                            } else {
                                p = f(i, sm, p, s, hpch.getConfig().getStringList(s + ".name"));
                            }
						}
					}
					for (String s : hpch.uHeads) {
                        SkullMeta sm = (SkullMeta) i.getItemMeta();
						if (a[0].equalsIgnoreCase(s)) {
                            p = f(i, sm, p, s, hpch.getConfig().getStringList(s + ".name"));
                        }
					}
					if (a[0].equalsIgnoreCase("player")) {
					    boolean player = true;
					    SkullMeta sm = (SkullMeta) i.getItemMeta();
					    for (String s : hpch.uHeads) {
					        if (d(hpch.getConfig().getStringList(s + ".name"), sm)) {
                                player = false;
                                break;
                            }
                        }
                        for (String s : hpch.mHeads) {
					        if (s.equalsIgnoreCase("sheep")) {
					            for (String st : hpch.getConfig().getConfigurationSection(s + ".name").getKeys(false)) {
                                    if (d(hpch.getConfig().getStringList(s + ".name." + st), sm)) {
                                        player = false;
                                        break;
                                    }
                                }
                            } else {
                                if (d(hpch.getConfig().getStringList(s + ".name"), sm)) {
                                    player = false;
                                    break;
                                }
                            }
                        }
                        if (player) {
					        p = p + (i.getAmount() * hpch.getConfig().getDouble("player.price"));
					        soldHeads.add("player");
					        i("player", i.getAmount());
					    }
                    }
				}	
			} else {
				if (Integer.parseInt(a[0]) <= i.getAmount()) {
					SkullMeta sm = (SkullMeta) i.getItemMeta();
					String s = null;
					for (String str : hpch.mHeads) {
                        if (str.equalsIgnoreCase("sheep")) {
                            for (String st : hpch.getConfig().getConfigurationSection(s + ".name").getKeys(false)) {
                                if (g(hpch.getConfig().getStringList(str + ".name." + st), sm)) {
                                    s = str;
                                }
                            }
                        } else {
                            if (g(hpch.getConfig().getStringList(str + ".name"), sm)) {
                                s = str;
                            }
                        }
					}
					p = hpch.getConfig().getDouble(s + ".price") * Integer.parseInt(a[0]);
                    soldHeads.add(s);
                    i(s, i.getAmount());
				} else {
					pl.sendMessage(hpc.getString("not-enough-heads"));
				}
			}
		} else {
			SkullMeta sm = (SkullMeta) i.getItemMeta();
			String s = null;
			for (String str : hpch.mHeads) {
                if (str.equalsIgnoreCase("sheep")) {
                    for (String st : hpch.getConfig().getConfigurationSection(str + ".name").getKeys(false)) {
                        if (g(hpch.getConfig().getStringList(str + ".name." + st), sm)) {
                            s = str;
                        }
                    }
                } else {
                    if (g(hpch.getConfig().getStringList(str + ".name"), sm)) {
                        s = str;
                    }
                }
			}
            for (String str : hpch.uHeads) {
			    if (g(hpch.getConfig().getStringList(str + ".name"), sm)) {
			        s = str;
                }
			}
            if (s == null) {
			    s = "player";
            }
			 p = hpch.getConfig().getDouble(s + ".price") * i.getAmount();
            soldHeads.add(s);
	    }
		return p;
	}
	private void sellAll(Player p, String[] a, ItemStack i) throws NoSuchFieldException, IllegalAccessException {
		Double price = 0.0;
		for (ItemStack is : p.getInventory()) {
            if (is != null) {
                if (is.getType().equals(Material.SKULL_ITEM)) {
                    price = setPrice(price, a, is, p);
                }
            }
        }
        try {
            if (hm.get("sheep") != 0 || hm.get("sheep") != null) {
                if (hm.get("sheep") % 3 == 0) {
                    hm.put("sheep", hm.get("sheep") / 3);
                }
            }
        } catch (Exception ignored) {

        }

        if (price == 0) {
            p.sendMessage(hpc.getString("no-heads"));
            return;
        }

		pay(p, a, i, price);
	}
	private void pay(Player p, String[] a, ItemStack i, double pr) throws NoSuchFieldException, IllegalAccessException {
		Economy econ = HeadsPlus.getInstance().getEconomy();
		SellHeadEvent she = new SellHeadEvent(pr, soldHeads, p, econ.getBalance(p), econ.getBalance(p) + pr, hm);
		Bukkit.getServer().getPluginManager().callEvent(she);
		if (!she.isCancelled()) {
            EconomyResponse zr = econ.depositPlayer(p, pr);
            String success = hpc.getString("sell-success").replaceAll("%l", Double.toString(zr.amount)).replaceAll("%b", Double.toString(zr.balance));
            String fail = hpc.getString("sell-fail");
            if (zr.transactionSuccess()) {
                itemRemoval(p, a, i);
                p.sendMessage(success);
                sold = true;
            } else {
                p.sendMessage(fail + ": " + zr.errorMessage);
            }
        }
	}

	private boolean checkColorSheep(Player sender, String[] args, ItemStack invi) throws NoSuchFieldException, IllegalAccessException {
	    for (String s : hpch.getConfig().getConfigurationSection( "sheep.name").getKeys(false)) {
	        for (int i = 0; i < hpch.getConfig().getStringList("sheep.name." + s).size(); i++) {
                if (hpchx.isHPXSkull(hpch.getConfig().getStringList("sheep.name." + s).get(i))) {
                    Field pro = ((SkullMeta) invi.getItemMeta()).getClass().getDeclaredField("profile");
                    pro.setAccessible(true);
                    GameProfile gm = (GameProfile) pro.get(invi.getItemMeta());
                    for (Property p : gm.getProperties().get("textures")) {
                        if (p.getValue().equals(hpchx.getTextures(hpch.getConfig().getStringList("sheep.name." + s).get(i)))) {
                            Double price = hpch.getConfig().getDouble("sheep.price");
                            if (invi.getAmount() > 0) {
                                price *= invi.getAmount();
                            }
                            pay(sender, args, invi, price);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private boolean checkSkullForTexture(Player pl, ItemStack is, List<String> list, String key, String[] args) throws NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < list.size(); i++) {
            if (hpchx.isHPXSkull(list.get(i))) {
                Field pro = ((SkullMeta) is.getItemMeta()).getClass().getDeclaredField("profile");
                pro.setAccessible(true);
                GameProfile gm = (GameProfile) pro.get(is.getItemMeta());
                for (Property p : gm.getProperties().get("textures")) {
                    if (p.getValue().equals(hpchx.getTextures(hpch.getConfig().getStringList(key + ".name").get(i)))) {
                        Double price = hpch.getConfig().getDouble(key + ".price");
                        if (is.getAmount() > 0) {
                            price *= is.getAmount();
                        }
                        pay(pl, args, is, price);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void a(ItemStack is, String key, String[] args, Player p) throws NoSuchFieldException, IllegalAccessException {
        Double price = hpch.getConfig().getDouble(key + ".price");
        soldHeads.add(key);
        i(key, is.getAmount());
        if (is.getAmount() > 0) {
            price = setPrice(price, args, is, p);
        }
        pay(p, args, is, price);
    }

    private Double b(Double price, String key, List<String> ls, ItemStack i, Player p, String[] args, boolean e, boolean f) throws NoSuchFieldException, IllegalAccessException {
        for (String l : ls) {
            if (hpchx.isHPXSkull(l)) {
                GameProfile gm = h(i);
                for (Property pr : gm.getProperties().get("textures")) {
                    if (pr.getValue().equals(hpchx.getTextures(l))) {
                        if (i.getAmount() > 0) {
                            if (e) {
                                price = setPrice(price, args, i, p);
                                return price;
                            } else if (f) {
                                soldHeads.add(key);
                                i(key, i.getAmount());
                                price = price + (i.getAmount() * hpch.getConfig().getDouble(key + ".price"));
                                return price;
                            } else {
                                p.getInventory().remove(i);
                            }
                        }
                    }
                }
            }
        }
        return price;
    }

    private boolean c(String s, String[] a, ItemStack is, List<String> ls, Player p) {
        for (String l : ls) {
            if (a[0].equalsIgnoreCase(s)) {
                if (nms().getSkullOwnerName((SkullMeta) is.getItemMeta()).equals(l)) {
                    p.getInventory().remove(is);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean d(List<String> ls, SkullMeta sms) {
        for (String l : ls) {
            if (nms().getSkullOwnerName(sms).equalsIgnoreCase(l)) {
                return true;
            }
        }
        return false;
    }
    private Double e(List<String> ls, Double p, String s, SkullMeta sm, ItemStack i) {
        for (String l : ls) {
            if (nms().getSkullOwnerName(sm).equals(l)) {
                p = p + (i.getAmount() * hpch.getConfig().getDouble(s + ".price"));
                return p;
            }
        }
        return p;
    }

    private Double f(ItemStack i, SkullMeta sm, Double p, String s, List<String> ls2) throws NoSuchFieldException, IllegalAccessException {
        for (String aLs2 : ls2) {

            if (nms().getSkullOwnerName(sm).equalsIgnoreCase(aLs2)) {

                if (nms().isSellable(i)) {
                    soldHeads.add(s);
                    i(s, i.getAmount());
                    p = p + (i.getAmount() * hpch.getConfig().getDouble(s + ".price"));
                }
            } else if (nms().getSkullOwnerName(sm).equalsIgnoreCase("HPXHead")) {
                if (hpchx.isHPXSkull(aLs2)) {

                    GameProfile gm = h(i);
                    for (Property pr : gm.getProperties().get("textures")) {
                        if (pr.getValue().equals(hpchx.getTextures(aLs2))) {
                            if (i.getAmount() > 0) {
                                if (nms().isSellable(i)) {
                                    soldHeads.add(s);
                                    i(s, i.getAmount());
                                    p = p + (i.getAmount() * hpch.getConfig().getDouble(s + ".price"));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return p;
    }

    private boolean g(List<String> ls, SkullMeta sm) {
        for (String l : ls) {
            if (nms().getSkullOwnerName(sm).equalsIgnoreCase(l)) {
                return true;
            }
        }
        return false;
    }

    private GameProfile h(ItemStack i) throws NoSuchFieldException, IllegalAccessException {
        Field pro;
        pro = ((SkullMeta) i.getItemMeta()).getClass().getDeclaredField("profile");
        pro.setAccessible(true);
        GameProfile gm;
        gm = (GameProfile) pro.get(i.getItemMeta());
        return gm;
    }

    private void i(String s, int amount) {
	    if (hm.get(s) == null) {
            hm.put(s, amount);
            return;
        }
	    if (hm.get(s) > 0) {
	        int i = hm.get(s);
	        i += amount;
	        hm.put(s, i);
        } else {
	        hm.put(s, amount);
        }
    }

    @Override
    public String getCmdName() {
        return "sellhead";
    }

    @Override
    public String getPermission() {
        return "headsplus.sellhead";
    }

    @Override
    public String getCmdDescription() {
        return LocaleManager.getLocale().descSellhead();
    }

    @Override
    public String getSubCommand() {
        return "Sellhead";
    }

    @Override
    public String getUsage() {
        return "/sellhead [All|Entity|#]";
    }

    // TODO
    @Override
    public HashMap<Boolean, String> isCorrectUsage(String[] args, CommandSender sender) {
        HashMap<Boolean, String> h = new HashMap<>();
        if (args.length > 1) {
            if (args[1].matches("^[A-Za-z0-9_]+$")) {
                h.put(true, "");
            } else {
                h.put(false, hpc.getString("alpha-names"));
            }
        } else {
            h.put(false, hpc.getString("invalid-args"));
        }

        return h;
    }

    @Override
    public boolean isMainCommand() {
        return false;
    }

    @Override
    public boolean fire(String[] args, CommandSender sender) {
        return false;
    }

    private NMSManager nms() {
	    return HeadsPlus.getInstance().getNMS();
    }
}