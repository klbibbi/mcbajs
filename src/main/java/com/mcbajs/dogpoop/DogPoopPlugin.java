package com.mcbajs.dogpoop;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class DogPoopPlugin extends JavaPlugin {
    
    private int schedulerIntervalMinutes;
    private int poopChance;
    private int cocoaBeansAmount;
    private double playerDistance;
    private Random random;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        random = new Random();

        // Start scheduler
        long intervalTicks = schedulerIntervalMinutes * 60 * 20L; // Convert minutes to ticks

        Bukkit.getScheduler().runTaskTimer(this, this::checkDogs, intervalTicks, intervalTicks);

        getLogger().info("DogPoop plugin enabled!");
        getLogger().info("Scheduler runs every " + schedulerIntervalMinutes + " minute(s)");
        getLogger().info("Probability: 1/" + poopChance);
    }

    @Override
    public void onDisable() {
        getLogger().info("DogPoop plugin disabled!");
    }
    
    private void loadConfig() {
        schedulerIntervalMinutes = getConfig().getInt("scheduler-interval-minutes", 1);
        poopChance = getConfig().getInt("poop-chance", 30);
        cocoaBeansAmount = getConfig().getInt("cocoa-beans-amount", 2);
        playerDistance = getConfig().getDouble("player-distance", 50.0);
    }
    
    private void checkDogs() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Wolf wolf) {
                    // Only tamed dogs can poop
                    if (wolf.isTamed()) {
                        // Check if the dog is near a player
                        if (isNearPlayer(wolf)) {
                            // Random chance if the dog should poop
                            if (random.nextInt(poopChance) == 0) {
                                makeDogPoop(wolf);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isNearPlayer(Wolf wolf) {
        Location wolfLocation = wolf.getLocation();
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(wolf.getWorld())) {
                if (player.getLocation().distance(wolfLocation) <= playerDistance) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void makeDogPoop(Wolf wolf) {
        Location location = wolf.getLocation();

        // Drop cocoa beans
        ItemStack cocoaBeans = new ItemStack(Material.COCOA_BEANS, cocoaBeansAmount);
        wolf.getWorld().dropItemNaturally(location, cocoaBeans);

        // Play sound
        // Uses custom sound key defined in resource pack
        wolf.getWorld().playSound(location, "custom.dog_poop", 1.0f, 1.0f);

        getLogger().info("A dog pooped at " +
            String.format("X:%.1f Y:%.1f Z:%.1f", location.getX(), location.getY(), location.getZ()));
    }
}

