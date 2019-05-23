package io.github.thatsmusic99.headsplus.config.headsx;

import io.github.thatsmusic99.headsplus.HeadsPlus;
import io.github.thatsmusic99.headsplus.config.headsx.icons.*;
import io.github.thatsmusic99.headsplus.util.InventoryManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public interface Icon {

    String getIconName();

    void onClick(Player p, InventoryManager im, InventoryClickEvent e);

    Material getDefaultMaterial();

    List<String> getDefaultLore();

    String getDefaultDisplayName();

    default Icon getReplacementIcon() {
        return new Glass();
    }

    static Icon getIconFromName(String s) {
        for (Icon i : getIcons()) {
            if (i.getIconName().equalsIgnoreCase(s) && !(i instanceof Air)) {
                return i;
            }
        }
        return null;
    }

    static List<Icon> getIcons() {
        List<Icon> icons = new ArrayList<>();
        icons.add(new Challenge());
        icons.add(new Close());
        icons.add(new Favourites());
        icons.add(new Glass());
        icons.add(new Head());

        icons.add(new Nav(Nav.Page.MENU, "Main Menu", Material.NETHER_STAR));
        icons.add(new Nav(Nav.Page.START, "First Page", Material.ARROW));
        icons.add(new Nav(Nav.Page.LAST, "Last Page", Material.ARROW));
        icons.add(new Nav(Nav.Page.BACK, "Back", Material.ARROW));
        icons.add(new Nav(Nav.Page.BACK_2, "Back 2", Material.ARROW));
        icons.add(new Nav(Nav.Page.BACK_3, "Back 3", Material.ARROW));
        icons.add(new Nav(Nav.Page.NEXT, "Next", Material.ARROW));
        icons.add(new Nav(Nav.Page.NEXT_2, "Next 2", Material.ARROW));
        icons.add(new Nav(Nav.Page.NEXT_3, "Next 3", Material.ARROW));

        icons.add(new Search());
        icons.add(new Stats());
        icons.add(new Air());
        icons.add(new HeadSection());
        icons.add(new ChallengeSection.Easy());
        icons.add(new ChallengeSection.EasyMedium());
        icons.add(new ChallengeSection.Medium());
        icons.add(new ChallengeSection.MediumHard());
        icons.add(new ChallengeSection.Hard());
        icons.add(new ChallengeSection.Tedious());
        icons.add(new ChallengeSection.TediousPainful());
        icons.add(new ChallengeSection.Painful());
        icons.add(new ChallengeSection.PainfulDeadly());
        icons.add(new ChallengeSection.Deadly());
        return icons;
    }

    static Icon getIconFromSingleLetter(String s) {
        for (Icon i : getIcons()) {
            if (i.getSingleLetter().equalsIgnoreCase(s) && !(i instanceof Air)) {
                return i;
            }
        }
        return null;
    }

    default Material getMaterial() {
        return Material.getMaterial(HeadsPlus.getInstance().getItems().getConfig().getString("icons." + getIconName() + ".material"));
    }

    default List<String> getLore() {
        return HeadsPlus.getInstance().getItems().getConfig().getStringList("icons." + getIconName() + ".lore");
    }

    default String getDisplayName() {
        return HeadsPlus.getInstance().getItems().getConfig().getString("icons." + getIconName() + ".display-name");
    }

    String getSingleLetter();
}
