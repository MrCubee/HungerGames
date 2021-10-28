package fr.mrcubee.hungergames.kit;

import java.util.*;
import java.util.function.BiConsumer;

import fr.mrcubee.fastgui.inventory.Button;
import fr.mrcubee.langlib.Lang;
import fr.mrcubee.hungergames.HungerGamesAPI;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Kit implements Listener, BiConsumer<Button, HumanEntity> {

    private final String name;
    private final ItemStack itemStack;
    private final Set<Player> players;
    private final BiConsumer<HumanEntity, ItemStack> itemEditor;

    protected Kit(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
        this.players = new HashSet<Player>();
        this.itemEditor = new BiConsumer<HumanEntity, ItemStack>() {
            @Override
            public void accept(HumanEntity humanEntity, ItemStack itemStack) {
                Player player;
                ItemMeta itemMeta;
                String displayName;
                String description;

                if (itemStack == null || !(humanEntity instanceof Player))
                    return;
                player = (Player) humanEntity;
                itemMeta = itemStack.getItemMeta();

                displayName = getDisplayName(player);
                description = getDescription(player);
                if (displayName != null)
                    itemMeta.setDisplayName(displayName);
                if (description != null)
                    itemMeta.setLore(Arrays.asList(description.split("\n")));
                itemStack.setItemMeta(itemMeta);
            }
        };
    }

    public boolean addPlayer(Player player) {
        if (player == null || !player.isOnline())
            return false;
        this.players.add(player);
        return true;
    }

    public boolean removePlayer(Player player) {
        if ((player == null) || (!this.players.contains(player)))
            return false;
        return this.players.remove(player);
    }

    public boolean containsPlayer(Player player) {
        if (player == null)
            return false;
        return this.players.contains(player);
    }

    protected void givePlayersKit() {
        for (Player player : this.players)
            if (player != null)
                givePlayerKit(player);
    }

    public abstract boolean canTakeKit(Player player);

    public abstract void cantTakeKitReason(Player player);

    public abstract void givePlayerKit(Player player);

    protected void removePlayersKit() {
        for (Player player : this.players)
            if ((player != null) && (player.isOnline()))
                removePlayerKit(player);
    }

    public abstract void removePlayerKit(Player player);

    public abstract boolean canLostItem(ItemStack itemStack);

    public String getName() {
        return this.name;
    }

    public abstract String getDisplayName(Player player);

    public abstract String getDescription(Player player);

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public BiConsumer<HumanEntity, ItemStack> getItemEditor() {
        return this.itemEditor;
    }

    public ItemStack getItemStack(Player player) {
        String displayName;
        String description;
        ItemStack itemStack;
        ItemMeta itemMeta;

        if (player == null)
            return null;
        displayName = getDisplayName(player);
        if (displayName == null)
            return null;
        description = getDescription(player);
        if (description == null)
            return null;
        itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null)
            return null;
        itemStack = this.itemStack.clone();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(description.split("\n")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Set<Player> getPlayers() {
        return new HashSet<Player>(this.players);
    }

    public int getNumberPlayer() {
        return this.players.size();
    }

    @Override
    public void accept(Button button, HumanEntity humanEntity) {
        Player player;
        KitManager kitManager;
        Kit[] oldKits;

        if (button == null || !(humanEntity instanceof Player))
            return;
        player = (Player) humanEntity;
        kitManager = HungerGamesAPI.getGame().getKitManager();
        oldKits = kitManager.getKitByPlayer(player);

        if (!canTakeKit(player)) {
            cantTakeKitReason(player);
            return;
        }
        if (addPlayer(player)) {
            player.closeInventory();
            if (oldKits != null) {
                for (Kit kit : oldKits) {
                    if (!kit.equals(this))
                        kit.removePlayer(player);
                }
            }
            player.sendMessage(Lang.getMessage(player, "kit.select", "&6You took the &r%s &6kit.", true, getDisplayName(player)));
        }
    }

    public abstract void update();

    @Override
    public int hashCode() {
        return (this.name != null) ? this.name.hashCode() : 0;
    }
}