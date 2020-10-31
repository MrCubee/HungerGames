package fr.mrcubee.survivalgames.listeners.entity;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.mrcubee.survivalgames.SurvivalGames;

public class EntityDamageByEntity implements Listener {

	private SurvivalGames survivalGames;

	public EntityDamageByEntity(SurvivalGames survivalGames) {
		this.survivalGames = survivalGames;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Projectile projectile;

		if (!survivalGames.getGame().isPvpEnable()) {
			if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)
				event.setCancelled(true);
			if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
				projectile = (Projectile) event.getDamager();
				if (projectile.getShooter() instanceof Player)
					event.setCancelled(true);
			}
		}
	}
}
