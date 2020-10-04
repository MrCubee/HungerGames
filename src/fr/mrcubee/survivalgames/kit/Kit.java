package fr.mrcubee.survivalgames.kit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Kit implements Listener {
	private String       name;
	private String       description;
	private ItemStack    itemStack;
	private List<Player> players;

	public Kit(String name, String description, ItemStack itemStack) {
		this.name = name;
		this.description = description;
		this.itemStack = itemStack;
		this.players = new ArrayList<Player>();
	}

	public void addPlayer(Player player) {
		if ((player == null) || (!player.isOnline()) || (this.players.contains(player)))
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

	public boolean canPlayerTakeKit(Player player) {
		return true;
	}

	protected void postGivePlayersKit(Player player) {
		if ((player != null) && (this.players.contains(player)))
			givePlayerKit(player);
	}

	public void givePlayerKit(Player player) {
		
	}

	protected void removePlayersKit() {
		for (Player player : this.players)
			if ((player != null) && (player.isOnline()))
				removePlayerKit(player);
	}

	protected void postRemovePlayersKit(Player player) {
		if ((player != null) && (this.players.contains(player)))
			removePlayerKit(player);
	}

	public void removePlayerKit(Player player) {
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public ItemStack getItemStack() {
		return this.itemStack.clone();
	}

	public List<Player> getPlayers() {
		List<Player> copy = new ArrayList(this.players.size());
		try {
			Collections.copy(copy, this.players);
		} catch (Exception localException) {}
		return this.players;
	}
}