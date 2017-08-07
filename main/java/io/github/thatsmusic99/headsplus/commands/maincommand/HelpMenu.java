package io.github.thatsmusic99.headsplus.commands.maincommand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.github.thatsmusic99.headsplus.HeadsPlus;
import io.github.thatsmusic99.headsplus.commands.HeadsPlusCommand;
import io.github.thatsmusic99.headsplus.config.HeadsPlusConfig;

public class HelpMenu {
	
	
	public static void helpNoArgs(CommandSender sender) {
	if (sender.hasPermission("headsplus.maincommand")) {
		List<PermissionEnums> headPerms = new ArrayList<>();
	    for (PermissionEnums key : PermissionEnums.values()) {
	    	if (!key.cmd.startsWith("/hp")) continue;
	    	if (sender.hasPermission(key.str)) {
	    		headPerms.add(key);
	    	}
	    }
	    int pageNo = 1;
	    int hpp = headPerms.size();
	    while (hpp > 8) {
	    	pageNo++;
	    	hpp = hpp - 8;
	    }
		sender.sendMessage(ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor1")) + "===============" + ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor2")) + " HeadsPlus " + ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor3")) + "1/" + String.valueOf(pageNo) + " " + ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor1")) + "===============");
		int TimesSent = 0;
		for (PermissionEnums key2 : headPerms) {
			if (TimesSent <= 7) {
				sender.sendMessage(ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor3")) + key2.cmd + " - " + ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor4")) + key2.dsc);
				TimesSent++;
			}
		}
	    } else {
	    	sender.sendMessage(HeadsPlusCommand.noPerms);
	    } 
	}
	public static void helpNo(CommandSender sender, String str) {
		if (sender.hasPermission("headsplus.maincommand")) {
			if (str.matches("^[0-9]+$")) {
				List<PermissionEnums> headPerms = new ArrayList<>();
				for (PermissionEnums key : PermissionEnums.values()) {
					if (sender.hasPermission(key.str)) {
						headPerms.add(key);
					}
				}
				int pages = 1;
				int hps = headPerms.size();
				while (hps > 8) {
					pages++;
					hps = hps - 8;
				}
				int entries = 8;
				int page = Integer.parseInt(str);
				int sIndex = (page - 1) * entries;
				int eIndex = entries + sIndex;
				if (eIndex > headPerms.size()) {
					eIndex = headPerms.size();
				}
				
				if ((page > pages) || (0 >= page)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(HeadsPlusConfig.getMessages().getString("invalid-pg-no"))));
				} else {
					sender.sendMessage(ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor1")) + "===============" + ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor2")) + " HeadsPlus " + ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor3")) + String.valueOf(page) + "/" + String.valueOf(pages) + " " + ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor1")) + "===============");
					List<PermissionEnums> hppsl = headPerms.subList(sIndex, eIndex);
				    for (PermissionEnums key : hppsl) {
				        sender.sendMessage(ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor3")) + key.cmd + " - " + ChatColor.valueOf(HeadsPlus.getInstance().getConfig().getString("themeColor4")) + key.dsc);
				    }
				}
			}
		} else {
	    	sender.sendMessage(HeadsPlusCommand.noPerms);
	    }
	}
}
