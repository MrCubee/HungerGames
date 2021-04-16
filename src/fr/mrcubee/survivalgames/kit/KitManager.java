package fr.mrcubee.survivalgames.kit;

import java.util.*;

import fr.mrcubee.fastgui.inventory.FastInventory;
import fr.mrcubee.survivalgames.Game;
import fr.mrcubee.survivalgames.GameStats;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.mrcubee.survivalgames.SurvivalGames;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KitManager implements CommandExecutor {

    private static BukkitTask bukkitTask;

    private final SurvivalGames survivalGames;
    private final Set<Kit> kits;
    private FastInventory[] kitInventories;
    private final ItemStack radarItem;

    public KitManager(SurvivalGames survivalGames) {
        ItemMeta radarItemMeta;

        this.survivalGames = survivalGames;
        this.kits = new HashSet<Kit>();
        this.radarItem = new ItemStack(Material.COMPASS);
        radarItemMeta = this.radarItem.getItemMeta();
        radarItemMeta.setDisplayName(ChatColor.RED + "RADAR");
        radarItemMeta.setLore(null);
        this.radarItem.setItemMeta(radarItemMeta);
    }

    public void createKitUpdater() {
        Game game = this.survivalGames.getGame();

        if (game == null)
            return;
        if (KitManager.bukkitTask != null)
            KitManager.bukkitTask.cancel();
        KitManager.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (game.getGameStats() == GameStats.DURING)
                    kits.forEach(Kit::update);
            }
        }.runTaskTimer(survivalGames, 0, 10L);
    }

    public boolean registerKit(Kit kit) {
        if (kit == null || StringUtils.isWhitespace(kit.getName()) || kit.getItemStack() == null)
            return false;
        return this.kits.add(kit);
    }

    public boolean hasKit(Player player) {
        if (player == null)
            return false;
        for (Kit kit : kits)
            if (kit.containsPlayer(player))
                return true;
        return false;
    }

    public void removeKit(Player player) {
        if (player == null)
            return;
        for (Kit kit : kits)
            kit.removePlayer(player);
    }

    public boolean canLostItem(Player player, ItemStack itemStack) {
        Kit[] kits;

        if (player == null || itemStack == null)
            return true;
        if (itemStack.isSimilar(this.radarItem))
            return false;
        kits = getKitByPlayer(player);
        if (kits == null)
            return true;
        for (Kit kit : kits)
            if (!kit.canLostItem(itemStack))
                return false;
        return true;
    }

    public Kit[] getKits() {
        return this.kits.toArray(new Kit[0]);
    }

    public String[] getKitNames() {
        String[] names = new String[this.kits.size()];
        int index = 0;

        for (Kit kit : this.kits)
            names[index++] = kit.getName();
        return names;
    }

    public String[] getKitDisplayNames(Player player) {
        String[] names;
        int index;

        if (player == null)
            return null;
        index = 0;
        names = new String[this.kits.size()];
        for (Kit kit : this.kits)
            names[index++] = kit.getDisplayName(player);
        return names;
    }

    public Kit getKitByName(String name) {
        if (name == null || StringUtils.isWhitespace(name))
            return null;
        for (Kit kit : kits)
            if (kit.getName().equals(name))
                return kit;
        return null;
    }

    public Kit getKitByDisplayName(Player player, String name) {
        if (player == null || name == null)
            return null;
        for (Kit kit : kits)
            if (name.equalsIgnoreCase(kit.getDisplayName(player)))
                return kit;
        return null;
    }

    public Kit[] getKitByPlayer(Player player) {
        LinkedList<Kit> result;

        if (player == null)
            return null;
        result = new LinkedList<Kit>();
        for (Kit kit : kits)
            if (kit.containsPlayer(player))
                result.add(kit);
        if (result.size() <= 0)
            return null;
        return result.toArray(new Kit[0]);
    }

    public void giveKitToPlayer() {
        this.survivalGames.getGame().getPlayerInGame().forEach(player -> {
            player.getInventory().addItem(this.radarItem);
        });
        for (Kit kit : kits)
            kit.givePlayersKit();
    }

    public void removeKitToPlayer() {
        this.survivalGames.getGame().getPlayerInGame().forEach(player -> {
            player.getInventory().removeItem(this.radarItem);
        });
        for (Kit kit : kits)
            kit.removePlayersKit();
    }

    public ItemStack getRadarItem() {
        return this.radarItem.clone();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Kit[] kits;
        Player target;

        if (sender.isOp() && args != null && args.length > 1 && args[0].equalsIgnoreCase("view")) {
            target = Bukkit.getPlayer(args[1]);
            if (target == null)
                sender.sendMessage(ChatColor.RED + "Player do not exist !");
            else {
                kits = getKitByPlayer(target);
                sender.sendMessage(ChatColor.GOLD + "Kit: " + ChatColor.RESET + (((kits == null || kits.length < 1) ? "No Kit" : kits[0].getName())));
            }
        }
        return true;
    }

    public FastInventory[] getKitInventories() {
        if (this.kitInventories == null)
            this.kitInventories = KitMenu.getKitInventories();
        return this.kitInventories;
    }

    public void clearKitInventoriesCache() {
        this.kitInventories = null;
        System.gc();
    }
}
