package io.github.thatsmusic99.headsplus.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.SkullMeta;

public class ZombieRecipe {
	
	private static ZombieRecipe instance;

	public ItemStack head; {
		ItemStack zHead = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta zMeta = (SkullMeta) zHead.getItemMeta();
		zMeta.setOwner("MHF_Zombie");
		zMeta.setDisplayName("Zombie Head");
		zHead.setItemMeta(zMeta);
		
	}
    @SuppressWarnings("deprecation")
	public ShapelessRecipe zombieRecipe = new ShapelessRecipe(head)
             .addIngredient(Material.ROTTEN_FLESH)
             .addIngredient(Material.SKULL_ITEM, 1);
    public static ZombieRecipe getInstance() {
    	return instance;
    }
}
