package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.Flags;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.particles.ParticleConfig;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getconfig.utils.ParticlesUtil;
import dev.gether.getcustomitem.GetCustomItem;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.utils.WorldGuardUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("cobweb_grenade")
public class CobwebGrenade extends CustomItem {
    @JsonIgnore
    private final NamespacedKey namespacedKey = new NamespacedKey(GetCustomItem.getInstance(), "cobwebgrenade_usage");
    private ParticleConfig particleConfig;
    private int radius;
    private int heightRadius;
    private double multiply;
    private int usage;

    public CobwebGrenade() {
    }
    public CobwebGrenade(Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, ParticleConfig particleConfig, int radius, int heightRadius, double multiply, int usage) {
        super(item, itemType, cooldown, permissionBypass, soundConfig);
        this.particleConfig = particleConfig;
        this.radius = radius;
        this.heightRadius = heightRadius;
        this.multiply = multiply;
        this.usage = usage;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = getItem().getItemStack().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {

            // set usage to persistent data
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, usage);

            List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore())
                lore.addAll(itemMeta.getLore());

            lore.replaceAll(line -> line
                    .replace("{radius-x}", String.valueOf(radius))
                    .replace("{radius-y}", String.valueOf(heightRadius))
                    .replace("{usage}", String.valueOf(usage))
            );
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getUsage(ItemMeta itemMeta) {
        if (itemMeta == null)
            return 0;

        Integer value = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
        return value != null ? value : 0;
    }

    public void takeAmount(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return;

        Integer usage = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
        if (usage == null)
            return;

        // ignore verify usage value because in other case im verify the number of usage
        // and if the number is lower than 1 im just remove it
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, usage - 1);
        itemStack.setItemMeta(itemMeta);

    }

    public void updateItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return;

        Integer usage = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
        if (usage == null)
            return;

        // default original item
        ItemStack originalItem = super.getItem().getItemStack().clone();
        ItemMeta originalMeta = originalItem.getItemMeta();

        if (originalMeta == null || !originalMeta.hasLore())
            return;

        List<String> lore = new ArrayList<>(originalMeta.getLore());
        lore.replaceAll(line -> line
                .replace("{radius-x}", String.valueOf(radius))
                .replace("{radius-y}", String.valueOf(heightRadius))
                .replace("{usage}", String.valueOf(usage))
        );

        itemMeta.setLore(ColorFixer.addColors(lore));
        itemStack.setItemMeta(itemMeta);

    }

    public void spawnCobweb(Location location) {
        for (int x = -radius + 1; x < radius; x++) {
            for (int y = -heightRadius + 1; y < heightRadius; y++) {
                for (int z = -radius + 1; z < radius; z++) {
                    Location tempLoc = location.clone().add(x, y, z);
                    com.sk89q.worldedit.util.Location locWordGuard = BukkitAdapter.adapt(tempLoc);
                    if (WorldGuardUtil.isInRegion(locWordGuard) &&
                            WorldGuardUtil.isDeniedFlag(tempLoc, null, Flags.BUILD)) {
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



}
