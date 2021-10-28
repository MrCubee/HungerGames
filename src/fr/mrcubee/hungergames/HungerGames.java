package fr.mrcubee.hungergames;

import fr.mrcubee.bukkit.packet.GenericListenerManager;
import fr.mrcubee.hungergames.command.CommandRegister;
import fr.mrcubee.langlib.Lang;
import fr.mrcubee.hungergames.command.LangCommand;
import fr.mrcubee.hungergames.listeners.RegisterListeners;
import fr.mrcubee.hungergames.step.StepManager;
import fr.mrcubee.hungergames.step.steps.FeastStep;
import fr.mrcubee.hungergames.step.steps.GameStep;
import fr.mrcubee.hungergames.step.steps.PvpStep;
import fr.mrcubee.hungergames.world.BiomeReplacer;
import fr.mrcubee.util.FileUtil;
import fr.mrcubee.world.WorldSpawnSetup;

import java.io.File;

import net.arkadgames.hungergames.sql.DataBaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class HungerGames extends JavaPlugin {

	private Game game;
	private Timer timer;
	private GenericListenerManager genericListenerManager;

	@Override
	public void onLoad() {
		saveDefaultConfig();

		BiomeReplacer.removeOcean();
		this.game = new Game(this);
		File worldFile = new File("./" + this.game.getGameSetting().getWorldName());
		File netherFile = new File("./" + this.game.getGameSetting().getWorldName() + "_nether");
		File endFile = new File("./" + this.game.getGameSetting().getWorldName() + "_the_end");
		FileUtil.delete(worldFile);
		FileUtil.delete(netherFile);
		FileUtil.delete(endFile);
		this.genericListenerManager = GenericListenerManager.create("SurvivalGames");
	}

	@Override
	public void onEnable() {
		LangCommand langCommand = new LangCommand();
		PluginCommand pluginCommand;
		StepManager stepManager;

		Lang.setDefaultLang("EN_us");
		this.game.init();

		// **Commands** //
		CommandRegister.registerCommand(this);

		// **STEPS** //
		stepManager = this.game.getStepManager();
		stepManager.registerStep(PvpStep.create(this.game));
		stepManager.registerStep(GameStep.create(this.game));
		stepManager.registerStep(FeastStep.create(this.game));
		// **END STEPS** //

		this.timer = new Timer(this);

		// **WORLD SETUP** //
		if (!WorldSpawnSetup.setup(this.game.getGameWorld(), this.game.getGameSetting().getLoadSize(), this.getLogger())) {
			getServer().shutdown();
			return;
		}
		this.game.getSpawnTerrainForming().runTaskTimer(this, 0L, 10L);
		// **END WORLD SETUP**//

		RegisterListeners.register(this);
		/*
		try {
			dataBase = new DataBase(this);
		} catch (SQLException e) {
			this.getLogger().severe("Error to connect to DataBase !");
			e.printStackTrace();
			this.getServer().shutdown();
			return;
		}boolean
		*/
		getGame().setGameStats(GameStats.WAITING);
		this.timer.runTaskTimer(this, 0L, 20L);
	}

	@Override
	public void onDisable() {
		File logsFile = new File("./logs");
		FileUtil.delete(logsFile);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (getGame().getGameStats() == GameStats.WAITING)
			getGame().forceStart();
		if (getGame().getGameStats() == GameStats.DURING)
			getGame().forcePvp();
		return true;
	}

	public DataBaseManager getDataBaseManager() {
		return this.game.getDataBaseManager();
	}

	public Game getGame() {
		return this.game;
	}

	public GenericListenerManager getGenericListenerManager() {
		return this.genericListenerManager;
	}
}
