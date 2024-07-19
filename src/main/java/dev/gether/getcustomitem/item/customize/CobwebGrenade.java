package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.TitleMessage;
import dev.gether.getconfig.domain.config.particles.ParticleConfig;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ParticlesUtil;
import dev.gether.getcustomitem.GetCustomItem;
import dev.gether.getcustomitem.config.RegionConfig;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.tasks.data.CobwebLocationRecord;
import dev.gether.getcustomitem.tasks.data.SerializableLocation;
import dev.gether.getcustomitem.utils.WorldGuardUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonTypeName("cobweb_grenade")
public class CobwebGrenade extends CustomItem {
    private ParticleConfig particleConfig;
    private int radius;
    private int heightRadius;
    private double multiply;
    private double heightVelocity;
    private int autoRemoveAfter;
    public CobwebGrenade() {
    }

    public CobwebGrenade(String key,
                         String categoryName,
                         int usage,
                         Item item,
                         ItemType itemType,
                         int cooldown,
                         String permissionBypass,
                         SoundConfig soundConfig,
                         List<String> notifyYourself,
                         List<String> notifyOpponents,
                         TitleMessage titleYourself,
                         TitleMessage titleOpponents,
                         ParticleConfig particleConfig,
                         int radius,
                         int heightRadius,
                         double multiply,
                         double heightVelocity,
                         int autoRemoveAfter) {
        super(key, categoryName, usage, item, itemType, cooldown,
                permissionBypass, soundConfig, notifyYourself,
                notifyOpponents, titleYourself, titleOpponents);

        this.particleConfig = particleConfig;
        this.radius = radius;
        this.heightRadius = heightRadius;
        this.multiply = multiply;
        this.heightVelocity = heightVelocity;
        this.autoRemoveAfter = autoRemoveAfter;
    }
    public void spawnCobweb(Location location, RegionConfig regionConfig) {
        //Save only center of the cobwebs
        SerializableLocation center = new SerializableLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        CobwebLocationRecord record = new CobwebLocationRecord(center, radius, heightRadius, System.currentTimeMillis());
        GetCustomItem.getInstance().getCobwebAutoRemoveTask().getCobwebLocations().addLocation(record);

        for (int x = -radius + 1; x < radius; x++) {
            for (int y = -heightRadius + 1; y < heightRadius; y++) {
                for (int z = -radius + 1; z < radius; z++) {
                    Location tempLoc = location.clone().add(x, y, z);
                    if (WorldGuardUtil.isInDisallowedRegion(tempLoc, regionConfig)) {
                        continue;
                    }
                    if (WorldGuardUtil.isDeniedFlag(tempLoc, null, Flags.BLOCK_PLACE)) {
                        continue;
                    }
                    Block block = tempLoc.getBlock();
                    if (block.getType() == Material.AIR) {
                        block.setType(Material.COBWEB);
                    }
                }
            }
        }
    }

    public void runParticles(ThrownPotion thrownPotion) {

        // check the particles is enabled
        if (!particleConfig.isEnable())
            return;
        new BukkitRunnable() {
            @Override
            public void run() {
                // check if the potion has landed or is removed
                if (thrownPotion.isOnGround() || !thrownPotion.isValid()) {
                    this.cancel();
                    return;
                }

                ParticlesUtil.spawnParticles(thrownPotion, particleConfig);

            }
        }.runTaskTimerAsynchronously(GetCustomItem.getInstance(), 0L, 0L);
    }


    @Override
    protected Map<String, String> replacementValues() {
        return Map.of(
                "{radius-x}", String.valueOf(radius),
                "{radius-y}", String.valueOf(heightRadius)
        );
    }

}
