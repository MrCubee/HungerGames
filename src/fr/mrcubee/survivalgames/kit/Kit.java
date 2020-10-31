package fr.mrcubee.survivalgames.kit;

import java.util.*;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class Kit implements Listener {

    private final String name;
    private final String description;
    private final ItemStack itemStack;
    private final Set<Player> players;

    protected Kit(String name, String description, ItemStack itemStack) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return this.description;
    }

    public ItemStack getItemStack() {
        return this.itemStack.clone();
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
        return ((this.name == null) ? 0 : this.name.hashCode());
    }
}