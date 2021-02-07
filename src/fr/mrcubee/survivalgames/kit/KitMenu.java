package fr.mrcubee.survivalgames.kit;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.mrcubee.survivalgames.SurvivalGamesAPI;
import org.bukkit.inventory.ItemStack;

public class KitMenu {

    private static Kit[] getKits(int page) {
        KitManager kitManager = SurvivalGamesAPI.getGame().getKitManager();
        Kit[] kits = kitManager.getKits();
        Kit[] result;
        int start = page * 45;
        int number;

        if (start < 0 || start >= kits.length)
            return null;
        number = Math.min(54, kits.length);
        result = new Kit[number];
        System.arraycopy(kits, start, result, 0, number);
        return result;
    }

    public static Inventory getInventory(Player player, int page) {
        Kit[] kits;
        Inventory inventory;
        int size;

        if (player == null || page < 0 || (kits = getKits(page)) == null)
            return null;
        size = Math.max(1, (kits.length / 9) + 1) * 9;
        inventory = Bukkit.createInventory(null, size, SurvivalGamesAPI.getGame().getGameSetting().getMenuKitName());
        Arrays.asList(kits).forEach(kit -> {
            ItemStack itemStack = kit.getItemStack(player);

            if (itemStack != null)
                inventory.addItem(itemStack);
        });
        return inventory;
    }
}
