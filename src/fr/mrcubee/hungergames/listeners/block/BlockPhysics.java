package fr.mrcubee.hungergames.listeners.block;

import fr.mrcubee.hungergames.HungerGames;
import fr.mrcubee.world.SpawnTerrainForming;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class BlockPhysics implements Listener {
	
	private final SpawnTerrainForming spawnTerrainForming;
	
	protected BlockPhysics(HungerGames survivalGames) {
		this.spawnTerrainForming = survivalGames.getGame().getSpawnTerrainForming();
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void blockPhysicsEvent(BlockPhysicsEvent event) {
		if (!spawnTerrainForming.isGrass())
			event.setCancelled(true);
	}
}
