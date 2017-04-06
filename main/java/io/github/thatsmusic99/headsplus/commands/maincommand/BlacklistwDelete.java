package io.github.thatsmusic99.headsplus.commands.maincommand;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import io.github.thatsmusic99.headsplus.HeadsPlus;
import io.github.thatsmusic99.headsplus.commands.HeadsPlusCommand;
import io.github.thatsmusic99.headsplus.config.HeadsPlusConfig;

public class BlacklistwDelete {
	
	private static FileConfiguration config = HeadsPlus.getInstance().getConfig();
	@SuppressWarnings("unused")
	private static File configF = new File(HeadsPlus.getInstance().getDataFolder(), "config.yml");

	public static void blacklistDel(CommandSender sender, String world) {
		if (sender.hasPermission("headsplus.maincommand.blacklistw.delete")) {
			String prefix = HeadsPlus.getInstance().translateMessages(ChatColor.translateAlternateColorCodes('&', HeadsPlusConfig.getMessages().getString("prefix")));
		  	if (world.matches("^[A-Za-z0-9_]+$")) {
		        try {
		            config.options().copyDefaults(true);
                    HeadsPlus.getInstance().saveConfig();
                    File cfile = new File(HeadsPlus.getInstance().getDataFolder(), "config.yml");
			          if (!(cfile.exists())) {
			              HeadsPlus.getInstance().log.info("[HeadsPlus] Config not found, creating!");
			           
			              sender.sendMessage(prefix + " " + ChatColor.DARK_AQUA + "Config wasn't found, now created." );
			          }
			          try {
			              List<String> blacklist = (List<String>)config.getStringList("blacklistw");
			              String rHead = world.toLowerCase();
			              if (blacklist.contains(rHead)) {
				              blacklist.remove(rHead);
				              config.set("blacklistw", blacklist);
				              config.options().copyDefaults(true);
				              HeadsPlus.getInstance().saveConfig();
				              sender.sendMessage(prefix + " " + ChatColor.DARK_AQUA + world + " has been removed from the blacklist!");
			              } else {
			    	          sender.sendMessage(prefix + " " + ChatColor.DARK_AQUA + "This world is not on the blacklist!");
			          
                    }} catch (Exception e) {
			    	      HeadsPlus.getInstance().log.severe("[HeadsPlus] Failed to remove world!");
			    	      e.printStackTrace();
			          }
		       } catch (Exception e) {
			       HeadsPlus.getInstance().log.severe("[HeadsPlus] Failed to remove world!");
			       e.printStackTrace();
		       }
		  } else {
			  sender.sendMessage(prefix + " " + ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(HeadsPlusConfig.getMessages().getString("alpha-names"))));
		  }
	} else {
		sender.sendMessage(HeadsPlusCommand.noPerms);
	}
	}

}