package dev.gether.getcustomitem.listener;

import com.sk89q.worldguard.protection.flags.Flags;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getconfig.utils.PlayerUtil;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.StickLevitation;
import dev.gether.getcustomitem.utils.WorldGuardUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Optional;

public class StickLevitationListener implements Listener {

    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    private final Config config;

    public StickLevitationListener(ItemManager itemManager, CooldownManager cooldownManager, Config config) {
        this.itemManager = itemManager;
        this.cooldownManager = cooldownManager;
        this.config = config;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem(); // using item

        Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.STICK_LEVITATION);
        if(customItemByType.isEmpty() || !(customItemByType.get() instanceof StickLevitation stickLevitation))
            return;

        if(itemStack == null || !itemStack.isSimilar(stickLevitation.getItemStack()))
            return;

        event.setCancelled(true);

        Action action = event.getAction();
        if(action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) {
            return;
        }

        double cooldownSeconds = cooldownManager.getCooldownSecond(player, stickLevitation);
        if(cooldownSeconds <= 0 || player.hasPermission(stickLevitation.getPermissionBypass())) {
            // set cooldown
            cooldownManager.setCooldown(player, stickLevitation);

            // find near players
            List<Player> nearPlayers = PlayerUtil.findNearPlayers(player.getLocation(), stickLevitation.getRadius());

            // particles and sound
            stickLevitation.playSound(player.getLocation()); // play sound

            /* world-guard section */
            // check the using player is in PVP region
            if(WorldGuardUtil.isInRegion(player) &&
                    WorldGuardUtil.isDeniedFlag(player.getLocation(), player, Flags.PVP)) {
                return;
            }

            // give effect all nears player
            PotionEffect potionEffect = stickLevitation.getPotionEffect();
            // check every player who can't be in the pvp region
            nearPlayers.forEach(p -> {
                if(WorldGuardUtil.isInRegion(p) &&
                        WorldGuardUtil.isDeniedFlag(p.getLocation(), p, Flags.PVP)) {
                    return;
                }
                p.addPotionEffect(potionEffect);
            });

        } else {
            MessageUtil.sendMessage(player, config.getLangConfig().getHasCooldown().replace("{time}", String.valueOf(cooldownSeconds)));
        }

    }



}
