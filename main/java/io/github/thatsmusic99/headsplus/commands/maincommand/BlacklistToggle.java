package io.github.thatsmusic99.headsplus.commands.maincommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import io.github.thatsmusic99.headsplus.HeadsPlus;
import io.github.thatsmusic99.headsplus.commands.HeadsPlusCommand;
import io.github.thatsmusic99.headsplus.config.HeadsPlusConfig;

public class BlacklistToggle {
	
	private final FileConfiguration config = HeadsPlus.getInstance().getConfig();
	private HeadsPlusConfig hpc = new HeadsPlusConfig();
	
	public void toggleNoArgs(CommandSender sender) {
		if (sender.hasPermission("headsplus.maincommand.blacklist.toggle")) {
			  try {
				  if (config.getBoolean("blacklistOn")) {
					  config.set("blacklistOn", false);
					  config.options().copyDefaults(true);
			          HeadsPlus.getInstance().saveConfig();
					  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("bl-off"))));
				  } else if (!config.getBoolean("blacklistOn")) {
					  config.set("blacklistOn", true);
					  config.options().copyDefaults(true);
					  HeadsPlus.getInstance().saveConfig();
					  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("bl-on"))));
				  }
			  } catch (Exception e) {
				  HeadsPlus.getInstance().log.severe("[HeadsPlus] Failed to toggle blacklist!");
				  e.printStackTrace();
				  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("bl-fail"))));
			  }
		  } else {
		      sender.sendMessage(new HeadsPlusCommand().noPerms);
		  }
	}
	public void toggle(CommandSender sender, String str) {
		if (sender.hasPermission("headsplus.maincommand.blacklist.toggle")) {
			try {
			   	if (str.equalsIgnoreCase("on")) {
			 		if (!config.getBoolean("blacklistOn")) {
					    config.set("blacklistOn", true);
					    config.options().copyDefaults(true);
					    HeadsPlus.getInstance().saveConfig();
					    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("bl-on"))));
				    } else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("bl-a-on"))));
					}
					       
			    } else if (str.equalsIgnoreCase("off")) {
					if (config.getBoolean("blacklistOn")) {
						config.set("blacklistOn", false);
					    config.options().copyDefaults(true);
			            HeadsPlus.getInstance().saveConfig();
					    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("bl-off"))));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("bl-a-off"))));
					}
				} else if (!(str.equalsIgnoreCase("on"))) {
				 	sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/headsplus blacklist [On|Off]");
				}
		    } catch (Exception e) {
				HeadsPlus.getInstance().log.severe("[HeadsPlus] Failed to toggle blacklist!");
				e.printStackTrace();
			    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(hpc.getMessages().getString("bl-fail"))));
		    }
		} else {
	    	sender.sendMessage(new HeadsPlusCommand().noPerms);
	    }
	}
}
