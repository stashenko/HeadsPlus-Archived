package io.github.thatsmusic99.headsplus.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.thatsmusic99.headsplus.commands.maincommand.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.github.thatsmusic99.headsplus.HeadsPlus;
import io.github.thatsmusic99.headsplus.config.HeadsPlusConfig;

public class HeadsPlusCommand implements CommandExecutor {
	
	public final String noPerms = ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(HeadsPlusConfig.getMessages().getString("no-perm")));
	private final String tooManyArgs = ChatColor.translateAlternateColorCodes('&', HeadsPlus.getInstance().translateMessages(HeadsPlusConfig.getMessages().getString("too-many-args")));
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		HelpMenu hm = new HelpMenu();
		List<String> validCmds = new ArrayList<>(Arrays.asList("reload",
                "blacklistadd",
                "blacklistdel",
                "blacklist",
                "info",
                "blacklistl",
                "help",
                "blacklistw",
                "blacklistwadd",
                "blacklistwdel",
                "blacklistwl",
                "blacklistw",
                "whitelistadd",
                "whitelistdel",
                "whitelistl",
                "whitelist",
				"whitelistwadd",
                "whitelistwdel",
                "whitelistwl",
                "whitelistw"));

		if ((cmd.getName().equalsIgnoreCase("headsplus")) || (cmd.getName().equalsIgnoreCase("hp"))) {
			   if ((args.length == 0)) {
				   hm.helpNoArgs(sender);
			   }
			    
			   if ((args.length >= 1) && (args[0].equalsIgnoreCase("reload"))) { 
				   new MCReload().reload(sender);
			   }   
			   if ((args.length == 2) && (args[0].equalsIgnoreCase("blacklistadd"))) {
				   BlacklistAdd.blacklistAdd(sender, args[1]);
			   } 
			   else if ((args.length == 1) && (args[0].equalsIgnoreCase("blacklistadd"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklist.add")) {
					   sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/headsplus blacklistadd [IGN]");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   } 
			   else if ((args.length > 2) && (args[0].equalsIgnoreCase("blacklistadd"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklist.add")) {
					   sender.sendMessage(tooManyArgs);
					   sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/headsplus blacklistadd [IGN]");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   }
			if ((args.length == 2) && (args[0].equalsIgnoreCase("whitelistadd"))) {
				WhitelistAdd.wlAdd(sender, args[1]);
			}
			else if ((args.length == 1) && (args[0].equalsIgnoreCase("whitelistadd"))) {
				if (sender.hasPermission("headsplus.maincommand.whitelist.add")) {
					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/headsplus whitelistadd [IGN]");
				} else {
					sender.sendMessage(noPerms);
				}
			}
			else if ((args.length > 2) && (args[0].equalsIgnoreCase("whitelistadd"))) {
				if (sender.hasPermission("headsplus.maincommand.whitelist.add")) {
					sender.sendMessage(tooManyArgs);
					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/headsplus whitelistadd [IGN]");
				} else {
					sender.sendMessage(noPerms);
				}
			}
		      
			   if ((args.length == 2) && (args[0].equalsIgnoreCase("blacklistdel"))) {
			 	  BlacklistDelete.blacklistDel(sender, args[1]);
			   } else if ((args.length == 1) && (args[0].equalsIgnoreCase("blacklistdel"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklist.delete")) {
					   sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp blacklistdel [IGN]");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   } else if ((args.length > 2) && (args[0].equalsIgnoreCase("blacklistdel"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklist.delete")) {
					   sender.sendMessage(tooManyArgs);
					   sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp blacklistdel [IGN]");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   }
			if ((args.length == 2) && (args[0].equalsIgnoreCase("whitelistdel"))) {
				WhitelistDel.whitelistDel(sender, args[1]);
			} else if ((args.length == 1) && (args[0].equalsIgnoreCase("whitelistdel"))) {
				if (sender.hasPermission("headsplus.maincommand.whitelist.delete")) {
					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp whitelistdel [IGN]");
				} else {
					sender.sendMessage(noPerms);
				}
			} else if ((args.length > 2) && (args[0].equalsIgnoreCase("whitelistdel"))) {
				if (sender.hasPermission("headsplus.maincommand.whitelist.delete")) {
					sender.sendMessage(tooManyArgs);
					sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp whitelistdel [IGN]");
				} else {
					sender.sendMessage(noPerms);
				}
			}
			   if ((args.length == 1) && (args[0].equalsIgnoreCase("blacklist"))) {
				   BlacklistToggle.toggleNoArgs(sender);
			   }
			   if ((args.length >= 2) && (args[0].equalsIgnoreCase("blacklist"))) {
				   BlacklistToggle.toggle(sender, args[1]);
			   }
            if ((args.length == 1) && (args[0].equalsIgnoreCase("whitelist"))) {
                WhitelistToggle.toggleNoArgs(sender);
            }
            if ((args.length >= 2) && (args[0].equalsIgnoreCase("whitelist"))) {
                WhitelistToggle.toggle(sender, args[1]);
            }
			   if ((args.length >= 1) && (args[0].equalsIgnoreCase("info"))) {
				   Info.info(sender);
			   }
			   if ((args.length == 1) && (args[0].equalsIgnoreCase("blacklistl"))) {
				   BlacklistList.blacklistListNoArgs(sender);
			   }
			   if ((args.length == 2) && (args[0].equalsIgnoreCase("blacklistl"))) {
				   BlacklistList.blacklistList(sender, args[1]);
			   }
			   if ((args.length > 2) && (args[0].equalsIgnoreCase("blacklistl"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklist.list")) {
				       sender.sendMessage(tooManyArgs);
				       sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp blacklistl [Page no.]");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   }
            if ((args.length == 1) && (args[0].equalsIgnoreCase("whitelistl"))) {
                WhitelistList.wlListNoArgs(sender);
            }
            if ((args.length == 2) && (args[0].equalsIgnoreCase("whitelistl"))) {
                WhitelistList.wlList(sender, args[1]);
            }
            if ((args.length > 2) && (args[0].equalsIgnoreCase("whitelistl"))) {
                if (sender.hasPermission("headsplus.maincommand.whitelist.list")) {
                    sender.sendMessage(tooManyArgs);
                    sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp whitelistl [Page no.]");
                } else {
                    sender.sendMessage(noPerms);
                }
            }
			   if ((args.length >= 1) && (args[0].matches("^[0-9]+$"))) {
				   hm.helpNo(sender, args[0]);
			   }
			   if ((args.length == 1) && (args[0].equalsIgnoreCase("help"))) {
				   hm.helpNoArgs(sender);
			   }
			   if ((args.length >= 2) && (args[0].equalsIgnoreCase("help"))) {
			 	   hm.helpNo(sender, args[1]);
			   }
			   if ((args.length > 0) && !validCmds.contains(args[0].toLowerCase()) && !args[0].matches("^[0-9]+$")) {
				   hm.helpNoArgs(sender);
			   }
			   if ((args.length == 2) && (args[0].equalsIgnoreCase("blacklistwadd"))) {
				   BlacklistwAdd.blacklistAdd(sender, args[1]);
			   }
			   if ((args.length == 1) && (args[0].equals("blacklistwadd"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklistw.add")) {
					   sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp blacklistwadd <World>");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   }
			   if ((args.length > 2) && (args[0].equalsIgnoreCase("blacklistwadd"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklistw.add")) {
					   sender.sendMessage(tooManyArgs);
					   sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp blacklistwadd <World>");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   }
			   if ((args.length == 2) && (args[0].equalsIgnoreCase("blacklistwdel"))) {
				   BlacklistwDelete.blacklistDel(sender, args[1]);
			   }
			   if ((args.length == 1) && (args[0].equals("blacklistwdel"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklistw.delete")) {
					   sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp blacklistwdel <World>");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   }
			   if ((args.length > 2) && (args[0].equalsIgnoreCase("blacklistwdel"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklistw.delete")) {
					   sender.sendMessage(tooManyArgs);
					   sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp blacklistwdel <World>");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   }
			   if ((args.length == 1) && (args[0].equalsIgnoreCase("blacklistw"))) {
				   BlacklistwToggle.toggleWorldNoArgs(sender);
			   }
			   if ((args.length >= 2) && (args[0].equalsIgnoreCase("blacklistw"))) {
				   BlacklistwToggle.toggleWorld(sender, args[1]);
			   }
			   if ((args.length == 1) && (args[0].equalsIgnoreCase("blacklistwl"))) {
				   BlacklistwList.blacklistwListNoArgs(sender);
			   }
			   if ((args.length == 2) && (args[0].equalsIgnoreCase("blacklistwl"))) {
				   BlacklistwList.blacklistwList(sender, args[1]);
			   }
			   if ((args.length > 2) && (args[0].equalsIgnoreCase("blacklistwl"))) {
				   if (sender.hasPermission("headsplus.maincommand.blacklistw.list")) {
				       sender.sendMessage(tooManyArgs);
				       sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp blacklistwl [Page no.]");
				   } else {
					   sender.sendMessage(noPerms);
				   }
			   }
            if ((args.length == 2) && (args[0].equalsIgnoreCase("whitelistwadd"))) {
                WhitelistwAdd.wlAdd(sender, args[1]);
            }
            if ((args.length == 1) && (args[0].equals("whitelistwadd"))) {
                if (sender.hasPermission("headsplus.maincommand.whitelistw.add")) {
                    sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp whitelistwadd <World>");
                } else {
                    sender.sendMessage(noPerms);
                }
            }
            if ((args.length > 2) && (args[0].equalsIgnoreCase("whitelistwadd"))) {
                if (sender.hasPermission("headsplus.maincommand.whitelistw.add")) {
                    sender.sendMessage(tooManyArgs);
                    sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp whitelistwadd <World>");
                } else {
                    sender.sendMessage(noPerms);
                }
            }

            if ((args.length == 2) && (args[0].equalsIgnoreCase("whitelistwdel"))) {
                WhitelistwDelete.wlDel(sender, args[1]);
            }
            if ((args.length == 1) && (args[0].equals("whitelistwdel"))) {
                if (sender.hasPermission("headsplus.maincommand.whitelistw.delete")) {
                    sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp whitelistwdel <World>");
                } else {
                    sender.sendMessage(noPerms);
                }
            }
            if ((args.length > 2) && (args[0].equalsIgnoreCase("whitelistwdel"))) {
                if (sender.hasPermission("headsplus.maincommand.whitelistw.delete")) {
                    sender.sendMessage(tooManyArgs);
                    sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp whitelistwdel <World>");
                } else {
                    sender.sendMessage(noPerms);
                }
            }
            if ((args.length == 1) && (args[0].equalsIgnoreCase("whitelistw"))) {
                WhitelistwToggle.togglewlwNoArgs(sender);
            }
            if ((args.length >= 2) && (args[0].equalsIgnoreCase("whitelistw"))) {
                WhitelistwToggle.toggleWorld(sender, args[1]);
            }
            if ((args.length == 1) && (args[0].equalsIgnoreCase("whitelistwl"))) {
                WhitelistwList.wlwListNoArgs(sender);
            }
            if ((args.length == 2) && (args[0].equalsIgnoreCase("whitelistwl"))) {
                WhitelistwList.wlwList(sender, args[1]);
            }
            if ((args.length > 2) && (args[0].equalsIgnoreCase("whitelistwl"))) {
                if (sender.hasPermission("headsplus.maincommand.whitelistw.list")) {
                    sender.sendMessage(tooManyArgs);
                    sender.sendMessage(ChatColor.DARK_RED + "Usage: " + ChatColor.RED + "/hp whitelistwl [Page no.]");
                } else {
                    sender.sendMessage(noPerms);
                }
            }
       
	} return false;
	}
}