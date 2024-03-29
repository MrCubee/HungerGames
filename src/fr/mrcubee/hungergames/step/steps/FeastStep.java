package fr.mrcubee.hungergames.step.steps;

import fr.mrcubee.annotation.spigot.config.Config;
import fr.mrcubee.langlib.Lang;
import fr.mrcubee.plugin.util.spigot.annotations.PluginAnnotations;
import fr.mrcubee.hungergames.Game;
import fr.mrcubee.hungergames.step.Step;
import fr.mrcubee.hungergames.step.StepUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FeastStep extends Step {

    @Config(path = "game.step.feast.spawnTime")
    private long spawnTime = 600;

    private FeastStep(Game game) {
        super(game, "feast");
        PluginAnnotations.load(game.getPlugin(), this);
        setSecondDuring(this.spawnTime);
    }

    private void playersPlaySound(Sound sound, float volume, float pitch) {
        getGame().getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), sound, volume, pitch);
        });
    }

    @Override
    public void start() {
        super.start();
        playersPlaySound(Sound.ORB_PICKUP, 100, 1);
        getGame().broadcastMessage("step.feastStep.broadcast", "Feast in &c%s", true,
                StepUtil.secondToString(this.spawnTime));
    }

    private ItemStack createItemStack(Random random) {
        Material[] allowMaterials;
        Material selectedMaterial;

        if (random == null)
            return null;
        allowMaterials = new Material[] {
                Material.APPLE,
                Material.COAL,
                Material.IRON_ORE,
                Material.GOLD_ORE,
                Material.DIAMOND_ORE,
                Material.COBBLESTONE,
                Material.STONE,
                Material.EXP_BOTTLE,
                Material.TNT,
                Material.BOW,
                Material.ARROW,
                Material.WOOD,
                Material.DIRT,
                Material.GRILLED_PORK,
                Material.REDSTONE_ORE,
                Material.FLINT,
                Material.STICK,
                Material.BONE,
        };
        selectedMaterial = allowMaterials[random.nextInt(allowMaterials.length)];
        return new ItemStack(selectedMaterial, random.nextInt(selectedMaterial.getMaxStackSize()) + 1);
    }

    private void createFilledChest(Block block, Random random) {
        int items;
        Chest chest;

        if (block == null)
            return;
        if (random == null)
            random = new Random(System.currentTimeMillis());
        items = random.nextInt(27) + 1;
        block.setType(Material.CHEST);
        chest = (Chest) block.getState();
        for (int i = 0; i < items; i++)
            chest.getInventory().addItem(createItemStack(random));
        chest.update(true);
    }

    @Override
    public void complete() {
        Random random = new Random(System.currentTimeMillis());
        Location spawn;

        super.complete();
        spawn = getGame().getSpawn();
        spawn.getBlock().setType(Material.ENCHANTMENT_TABLE);
        createFilledChest(spawn.subtract(2, 0, 2).getBlock(), random);
        createFilledChest(spawn.add(2, 0, 0).getBlock(), random);
        createFilledChest(spawn.add(2, 0, 0).getBlock(), random);
        createFilledChest(spawn.add(0, 0, 2).getBlock(), random);
        createFilledChest(spawn.add(0, 0, 2).getBlock(), random);
        createFilledChest(spawn.subtract(2, 0, 0).getBlock(), random);
        createFilledChest(spawn.subtract(2, 0, 0).getBlock(), random);
        createFilledChest(spawn.subtract(0, 0, 2).getBlock(), random);
    }

    @Override
    public void update() {

    }

    @Override
    public String scoreBoardGameStatus(Player player) {
        return Lang.getMessage(player, "step.feastStep.scoreboard", "&cFeast in &6%s", true,
                StepUtil.secondToString(getEndSeconds()));
    }

    public static FeastStep create(Game game) {
        if (game == null)
            return null;
        return new FeastStep(game);
    }

}
