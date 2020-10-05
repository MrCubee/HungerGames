package fr.mrcubee.survivalgames.kit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mrcubee.survivalgames.SurvivalGamesAPI;

public class KitMenu {

    private static Kit[] getKits(int page) {
        KitManager kitManager = SurvivalGamesAPI.getGame().getKitManager();
        String[] kitNames = kitManager.getKitsNames();
        Kit[] kits;
        int start = page * 45;
        int number = (kitNames.length > 45) ? 45 : kitNames.length;

        if (start < 0 || start >= kitNames.length)
            return null;
        kits = new Kit[number];
        for (int i = 0; i < number; i++)
            kits[i] = kitManager.getKitByName(kitNames[start + i]);
        return kits;
    }

    private static ItemStack getItem(Kit kit) {
        ItemStack itemStack = kit.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lores = new ArrayList<String>();

        itemMeta.setDisplayName(kit.getName());
        if (kit.getDescription() != null && kit.getDescription().length() > 0) {
            String[] lines = kit.getDescription().split("\n");
            for (String line : lines)
                lores.add(line);
        }
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static void fillInventory(Kit[] kits, Inventory inventory) {
        ItemStack kitItem;

        if (kits == null || inventory == null)
            return;
        for (Kit kit : kits) {
            kitItem = getItem(kit);
            inventory.addItem(kitItem);
        }
    }

    public static Inventory getInventory(int page) {
        Kit[] kits;
        Inventory inventory;
        int length;
        int size;

        if (page < 0 || (kits = getKits(page)) == null)
            return null;
        if ((length = kits.length) <= 9)
            size = 9;
        else if ((length / 9) <= 5)
            size = ((length / 9) + ((length % 9 == 0) ? 0 : 1)) * 9;
        else
            size = 54;
        inventory = Bukkit.createInventory(null, size, SurvivalGamesAPI.getGame().getGameSetting().getMenuKitName());
        fillInventory(kits, inventory);
        return inventory;
    }

    public static int getMaxPage() {
        KitManager kitManager = SurvivalGamesAPI.getGame().getKitManager();
        int totalKits = kitManager.getKitsNames().length;

        if (totalKits < 1)
            return 0;
        return ((totalKits / 45) + ((totalKits % 2 == 0) ? 0 : 1));
    }

}
