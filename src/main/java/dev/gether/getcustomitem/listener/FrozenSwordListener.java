package dev.gether.getcustomitem.listener;

import com.sk89q.worldguard.protection.flags.Flags;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.FrozenManager;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.FrozenSword;
import dev.gether.getcustomitem.utils.WorldGuardUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.Random;

public class FrozenSwordListener implements Listener {

    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    private final Config config;

    private final FrozenManager frozenManager;
    private final Random random = new Random();

    public FrozenSwordListener(ItemManager itemManager, CooldownManager cooldownManager, Config config, FrozenManager frozenManager) {
        this.itemManager = itemManager;
        this.cooldownManager = cooldownManager;
        this.config = config;
        this.frozenManager = frozenManager;
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        double frozenTime = frozenManager.getFrozenTime(player);
        if(frozenTime > 0) {
            event.setCancelled(true);
        } else {
            frozenManager.cleanCache(player);
        }

    }
    @EventHandler
    public void onPlayerInteract(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player damager &&
                event.getEntity() instanceof Player victim
            ) {

            Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.FROZEN_SWORD);
            if(customItemByType.isEmpty() || !(customItemByType.get() instanceof FrozenSword frozenSword))
                return;

            ItemStack mainHand = damager.getInventory().getItemInMainHand();
            ItemStack offHand = damager.getInventory().getItemInOffHand();

            ItemStack frozenSwordItem = frozenSword.getItemStack();
            // verify the main item or off-hand is the same what frozen item
            if(mainHand.isSimilar(frozenSwordItem) ||
                offHand.isSimilar(frozenSwordItem)) {

                event.setCancelled(true);

                double cooldownSeconds = cooldownManager.getCooldownSecond(damager, frozenSword);
                if(cooldownSeconds <= 0 || damager.hasPermission(frozenSword.getPermissionBypass())) {
                    // set cooldown
                    cooldownManager.setCooldown(damager, frozenSword);

                    // particles and sound
                    frozenSword.playSound(damager.getLocation()); // play sound

                    /* world-guard section */
                    // check the using player is in PVP region
                    if(WorldGuardUtil.isInRegion(damager) &&
                            WorldGuardUtil.isDeniedFlag(damager.getLocation(), damager, Flags.PVP)) {
                        return;
                    }
                    if(WorldGuardUtil.isInRegion(victim) &&
                            WorldGuardUtil.isDeniedFlag(victim.getLocation(), victim, Flags.PVP)) {
                        return;
                    }

                    double winTicket = random.nextDouble() * 100;
                    if(winTicket <= frozenSword.getChanceToFrozen()) {
                        frozenManager.freeze(victim, frozenSword);

                        MessageUtil.sendMessage(damager, config.getLangConfig().getFrozenPlayer()
                                .replace("{player}", victim.getName())); // send info to damager
                        MessageUtil.sendMessage(victim, config.getLangConfig().getYouAreFrozen()
                                .replace("{player}", damager.getName())); // info was sent that he was frozen
                    }

                } else {
                    MessageUtil.sendMessage(damager, config.getLangConfig().getHasCooldown().replace("{time}", String.valueOf(cooldownSeconds)));
                }

            }

        }

    }

    /**
     * cancel place/use custom item
     * @param event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem(); // using item

        Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.FROZEN_SWORD);
        if (customItemByType.isEmpty() || !(customItemByType.get() instanceof FrozenSword frozenSword))
            return;

        if(item != null && item.isSimilar(frozenSword.getItemStack())) {
            event.setCancelled(true);
        }
    }




}
