package dev.gether.getcustomitem.listener;

import com.sk89q.worldguard.protection.flags.Flags;
import dev.gether.getconfig.utils.ItemUtil;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.GetCustomItem;
import dev.gether.getcustomitem.utils.WorldGuardUtil;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.CrossBowItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Optional;
import java.util.Random;

public class CrossbowListener implements Listener {

    private final GetCustomItem plugin;
    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    private final String metadataKey = "getcustomitem_arrow";
    private final Random random = new Random();
    private final Config config;

    public CrossbowListener(GetCustomItem plugin, ItemManager itemManager, CooldownManager cooldownManager, Config config) {
        this.plugin = plugin;
        this.itemManager = itemManager;
        this.cooldownManager = cooldownManager;
        this.config = config;
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player shooter &&
                event.getProjectile() instanceof Arrow arrow) {

            ItemStack bow = event.getBow();
            Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.CROSSBOW);
            if(customItemByType.isEmpty() || !(customItemByType.get() instanceof CrossBowItem crossBowItem))
                return;

            if (ItemUtil.sameItem(crossBowItem.getItem().getItemStack(), bow)) {
                double cooldownSeconds = cooldownManager.getCooldownSecond(shooter, crossBowItem);
                Bukkit.broadcastMessage("#"+cooldownSeconds);
                if(cooldownSeconds <= 0 || shooter.hasPermission(crossBowItem.getPermissionBypass())) {
                    itemManager.startArrowTrail(arrow, crossBowItem.getParticleConfig()); // particles
                    itemManager.playSound(arrow.getLocation(), crossBowItem.getSoundConfig()); // play sound
                    // add custom meta to arrow for help with verify custom arrow
                    arrow.setMetadata(metadataKey, new FixedMetadataValue(plugin, true));
                    cooldownManager.setCooldown(shooter, crossBowItem); // set cooldown
                } else {
                    event.setCancelled(true);
                    MessageUtil.sendMessage(shooter, config.getLangConfig().getHasCooldown().replace("{time}", String.valueOf(cooldownSeconds)));
                }

            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow arrow &&
                arrow.hasMetadata(metadataKey) &&
                event.getHitEntity() instanceof Player hitPlayer &&
                arrow.getShooter() instanceof Player shooter) {

            Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.CROSSBOW);
            if(customItemByType.isEmpty() || !(customItemByType.get() instanceof CrossBowItem crossBowItem))
                return;

            // take a permission which ignore received effect
            if(hitPlayer.hasPermission(crossBowItem.getPermissionIgnoreEffect()))
                return;

            /* world-guard section */
            // check if the hit player is in the pvp region
            if(WorldGuardUtil.isInRegion(hitPlayer) &&
                    WorldGuardUtil.isDeniedFlag(hitPlayer.getLocation(), hitPlayer, Flags.PVP)) {
                return;
            }
            // check if the shooter player is in the pvp region
            if(WorldGuardUtil.isInRegion(shooter) &&
                    WorldGuardUtil.isDeniedFlag(shooter.getLocation(), shooter, Flags.PVP)) {
                return;
            }

            // check is not the npc
            boolean isCitizensNPC = hitPlayer.hasMetadata("NPC");
            if(isCitizensNPC) return;

            // look into player who can have a permission which ignore received the effect
            if(hitPlayer.hasPermission(crossBowItem.getPermissionIgnoreEffect()))
                return;

            double winTicket = random.nextDouble() * 100;
            if(winTicket <= crossBowItem.getChance() ) {
                hitPlayer.teleport(shooter);
            }


        }
    }


}
