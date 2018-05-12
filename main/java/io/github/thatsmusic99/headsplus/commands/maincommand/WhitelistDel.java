package io.github.thatsmusic99.headsplus.commands.maincommand;

import io.github.thatsmusic99.headsplus.HeadsPlus;
import io.github.thatsmusic99.headsplus.commands.IHeadsPlusCommand;
import io.github.thatsmusic99.headsplus.config.HeadsPlusConfig;
import io.github.thatsmusic99.headsplus.locale.LocaleManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

public class WhitelistDel implements IHeadsPlusCommand {

    private final HeadsPlusConfig hpc = HeadsPlus.getInstance().getMessagesConfig();

    @Override
    public String getCmdName() {
        return "whitelistdel";
    }

    @Override
    public String getPermission() {
        return "headsplus.maincommand.whitelist.delete";
    }

    @Override
    public String getCmdDescription() {
        return LocaleManager.getLocale().descWhitelistDelete();
    }

    @Override
    public String getSubCommand() {
        return "Whitelistdel";
    }

    @Override
    public String getUsage() {
        return "/hp whitelistdel";
    }

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
        return true;
    }

    @Override
    public boolean fire(String[] args, CommandSender sender) {
        try {
            FileConfiguration config = HeadsPlus.getInstance().getConfig();
            List<String> wl = config.getStringList("whitelist");
            String rHead = args[1].toLowerCase();
            if (wl.contains(rHead)) {
                wl.remove(rHead);
                config.set("whitelist", wl);
                config.options().copyDefaults(true);
                HeadsPlus.getInstance().saveConfig();
                sender.sendMessage(hpc.getString("head-removed-wl").replaceAll("\\{name}", args[1]));
            } else {
                sender.sendMessage(hpc.getString("head-a-removed-wl"));
            }
        } catch (Exception e) {
            new DebugPrint(e, "Subcommand (whitelistdel)", true, sender);

        }

        return true;
    }
}
