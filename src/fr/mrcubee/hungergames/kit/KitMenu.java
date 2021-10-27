package fr.mrcubee.hungergames.kit;

import fr.mrcubee.fastgui.FastGUI;
import fr.mrcubee.fastgui.inventory.FastInventory;

import fr.mrcubee.hungergames.HungerGamesAPI;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitMenu {

    private static final ItemStack WHITE_GLASS_ITEM = removeIdentifier(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.WHITE.getWoolData()));
    private static final ItemStack BLACK_GLASS_ITEM = removeIdentifier(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getWoolData()));
    private static final ItemStack BARRIER_ITEM = removeIdentifier(new ItemStack(Material.BARRIER));

    private static ItemStack removeIdentifier(ItemStack itemStack) {
        ItemMeta itemMeta;

        if (itemStack == null)
            return null;
        itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return itemStack;
        itemMeta.setDisplayName(ChatColor.WHITE.toString());
        itemMeta.setLore(null);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static FastInventory buildInventory(Kit[] kits, int startIndex, int length) {
        FastInventory fastInventory;
        Kit kit;

        if (kits == null || startIndex + length > kits.length || length > 28)
            return null;
        fastInventory = FastGUI.createInventory(6);
        if (fastInventory == null)
            return null;
        fastInventory.fillInventory(BARRIER_ITEM);
        fastInventory.setRow(0, BLACK_GLASS_ITEM);
        fastInventory.setRow(5, BLACK_GLASS_ITEM);
        fastInventory.setColumn(0, BLACK_GLASS_ITEM);
        fastInventory.setColumn(8, BLACK_GLASS_ITEM);
        for (int i = 0; i < length; i++) {
            kit = kits[i + startIndex];
            fastInventory.createCustomItemButton((i % 7) + ((i / 7) * 9) + 10, kit.getItemStack(), kit.getItemEditor(), kit);
        }
        return fastInventory;
    }

    public static FastInventory[] getKitInventories() {
        KitManager kitManager = HungerGamesAPI.getGame().getKitManager();
        Kit[] kits = kitManager.getKits();
        int numberOfPage;
        FastInventory[] result;
        int startIndex;

        if (kits == null || kits.length < 1)
            return null;
        numberOfPage = (kits.length / 29) + 1;
        result = new FastInventory[numberOfPage];
        for (int i = 0; i < numberOfPage; i++) {
            startIndex = i * 28;
            result[i] = buildInventory(kits, startIndex, Math.min(kits.length - startIndex, 28));
        }
        return result;
    }
}
