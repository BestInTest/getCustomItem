package dev.gether.getcustomitem.item;

import dev.gether.getcustomitem.GetCustomItem;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.config.addon.ParticleConfig;
import dev.gether.getcustomitem.config.addon.SoundConfig;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class ItemManager {

    private final Config config;
    private final GetCustomItem plugin;

    public ItemManager(Config config, GetCustomItem plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    public Optional<CustomItem> findCustomItemByType(ItemType itemType) {
        return config.getCustomItems().stream().filter(item -> item.getItemType() == itemType).findFirst();
    }


    /*
    public Optional<ItemStack> findItemStackByType(ItemType itemType) {
        return config.getCustomItems().stream()
                .filter(item -> item.getItemType() == itemType)
                .map(item -> item.getItem().getItemStack()).findFirst();
    }
    */


    public void playSound(Location location, SoundConfig soundConfig) {
        // check sound is enabled
        if(!soundConfig.isEnable())
            return;

        location.getWorld().playSound(location, soundConfig.getSound(), 1F, 1F);
    }

    public void startArrowTrail(Arrow arrow, ParticleConfig particleConfig) {

        // check the particles is enabled
        if(!particleConfig.isEnable())
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                // check if the arrow has landed or is removed
                if (arrow.isOnGround() || !arrow.isValid()) {
                    this.cancel();
                    return;
                }

                arrow.getWorld().spawnParticle(particleConfig.getParticle(), arrow.getLocation(), 5, 0.1, 0.1, 0.1, 0.01);
            }
        }.runTaskTimer(plugin, 0L, 1L);

    }
}
