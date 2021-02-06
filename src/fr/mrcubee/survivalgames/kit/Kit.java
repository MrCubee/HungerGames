package fr.mrcubee.survivalgames.kit;

import java.util.*;

import fr.mrcubee.langlib.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Kit implements Listener {

    private final String name;
    private final String nameId;
    private final String descriptionId;
    private final ItemStack itemStack;
    private final Set<Player> players;

    protected Kit(String name, String nameId, String descriptionId, ItemStack itemStack) {
        this.name = name;
        this.nameId = nameId;
        this.descriptionId = descriptionId;
        this.itemStack = itemStack;
        this.players = new HashSet<Player>();
    }

    public void addPlayer(Player player) {
        if (player == null || !player.isOnline() || !canTakeKit(player))
            return;
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        if ((player == null) || (!this.players.contains(player)))
            return;
        this.players.remove(player);
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

    public String getNameId() {
        return this.nameId;
    }

    public abstract String getDisplayName(Player player);

    public String getDescriptionId() {
        return this.descriptionId;
    }

    public abstract String getDescription(Player player);

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public ItemStack getItemStack(Player player) {
        ItemStack itemStack;
        ItemMeta itemMeta;
        String name;
        String description;

        if (player == null)
            return null;
        itemMeta = this.itemStack.getItemMeta();
        if (itemMeta == null)
            return null;
        itemStack = this.itemStack.clone();
        itemMeta.setDisplayName(getDisplayName(player));
        itemMeta.setLore(Arrays.asList(getDescription(player).split("\n")));
        this.itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Set<Player> getPlayers() {
        return new HashSet<Player>(this.players);
    }

    public int getNumberPlayer() {
        return this.players.size();
    }

    public abstract void update();

    @Override
    public int hashCode() {
        return ((this.nameId == null) ? 0 : this.nameId.hashCode());
    }
}