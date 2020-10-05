package fr.mrcubee.survivalgames;

import fr.mrcubee.survivalgames.listeners.RegisterListeners;
import fr.mrcubee.survivalgames.worldmanager.BorderManager;
import fr.mrcubee.util.FileUtil;
import fr.mrcubee.world.WorldLoader;
import net.arkadgames.survivalgame.sql.DataBase;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SurvivalGames extends JavaPlugin {
	
	private DataBase dataBase;
	private Game game;
	private Timer timer;

	public void onLoad() {
		saveDefaultConfig();
		this.game = new Game(this);
		File worldFile = new File("./" + this.game.getGameSetting().getWorldName());
		File netherFile = new File("./" + this.game.getGameSetting().getWorldName() + "_nether");
		File endFile = new File("./" + this.game.getGameSetting().getWorldName() + "_the_end");
		FileUtil.remove(worldFile);
		FileUtil.remove(netherFile);
		FileUtil.remove(endFile);
	}

	public void onEnable() {
		Location spawn;
		
		getGame().init();
		this.timer = new Timer(this);
		this.timer.start();

		World world = game.getGameWorld();
		
		if (world == null) {
			getServer().shutdown();
			return;
		}
		spawn = new Location(world, 0.0D, world.getMaxHeight(), 0.0D);
		
		loadArea(world);
		while (spawn.getBlockY() >= 1) {
			if (spawn.getBlock().getType().equals(Material.AIR))
				spawn.setY(spawn.getBlockY() - 1);
			else {
				spawn.setY(spawn.getBlockY() + 1);
				break;
			}
		}
		if (spawn.getBlock().getBiome().toString().toLowerCase().contains("ocean")) {
			getServer().shutdown();
			return;
		}
		this.game.setSpawn(spawn);
		world.setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockY());
		BorderManager.setCenter(world, 0.0D, 0.0D);
		BorderManager.setWorldBorder(world, getGame().getGameSetting().getMaxBorder());
		RegisterListeners.register(this);
		/*
		try {
			dataBase = new DataBase(this);
		} catch (SQLException e) {
			this.getLogger().severe("Error to connect to DataBase !");
			e.printStackTrace();
			this.getServer().shutdown();
			return;
		}
		*/
		getGame().setGameStats(GameStats.WAITING);
	}

	public void onDisable() {
		File logsFile = new File("./logs");
		FileUtil.remove(logsFile);
	}

	public void loadArea(World world) {
		int total = 441;
		int loaded = 0;
		int percent = 0;
		int newPercent;

		for (int z = -10; z <= 10; z++) {
			for (int x = -10; x <= 10; x++) {
				WorldLoader.loadChunks(world.getChunkAt(x, z));
				loaded++;
				newPercent = loaded * 100 / total;
				if (newPercent != percent) {
					percent = newPercent;
					if (percent % 10 == 0)
						getLogger().warning("Loading ... (" + percent + "%)");
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isOp())
			return false;
		if (getGame().getGameStats() == GameStats.WAITING)
			getGame().forcestart();
		if (getGame().getGameStats() == GameStats.DURING)
			getGame().forcepvp();
		return true;
	}
	
	public DataBase getDataBase() {
		return dataBase;
	}

	public Game getGame() {
		return this.game;
	}

	public Timer getTimer() {
		return this.timer;
	}
}
