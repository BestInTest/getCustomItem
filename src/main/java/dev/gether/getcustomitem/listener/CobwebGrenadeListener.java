package dev.gether.getcustomitem.listener;

import dev.gether.getconfig.utils.ConsoleColor;
import dev.gether.getconfig.utils.ItemUtil;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.CobwebGrenade;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Optional;

public class CobwebGrenadeListener implements Listener {

    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    private final Config config;

    public CobwebGrenadeListener(ItemManager itemManager, CooldownManager cooldownManager, Config config) {
        this.itemManager = itemManager;
        this.cooldownManager = cooldownManager;
        this.config = config;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem(); // using item

        Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.COBWEB_GRENADE);
        if(customItemByType.isEmpty() || !(customItemByType.get() instanceof CobwebGrenade cobwebGrenade))
            return;

        if(itemStack == null || !ItemUtil.sameItemName(itemStack, cobwebGrenade.getItem().getItemStack()))
            return;

        event.setCancelled(true);

        Action action = event.getAction();
        if(action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR)
            return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        int usage = cobwebGrenade.getUsage(itemMeta); // get from item amount to usage


        double cooldownSeconds = cooldownManager.getCooldownSecond(player, cobwebGrenade);
        if(cooldownSeconds <= 0 || player.hasPermission(cobwebGrenade.getPermissionBypass())) {
            if(usage >= 1) {
                // set cooldown
                cooldownManager.setCooldown(player, cobwebGrenade);

                // create grenade
                ThrownPotion thrownPotion = (ThrownPotion) player.getWorld().spawnEntity(player.getLocation().clone().add(0, 1.1, 0), EntityType.SPLASH_POTION);
                thrownPotion.setItem(itemStack);

                Location playerLocation = player.getEyeLocation();
                Vector velocity = playerLocation.getDirection().multiply(cobwebGrenade.getMultiply());
                thrownPotion.setVelocity(velocity); // throw grenade

                // particles and sound
                cobwebGrenade.runParticles(thrownPotion); // particles
                cobwebGrenade.playSound(player.getLocation()); // play sound

                // usage over than 1 so just only update the item
                // else case remove item from inventory
                if(usage > 1) {
                    // take one usage
                    cobwebGrenade.takeAmount(itemStack);
                    // update the lore
                    cobwebGrenade.updateItem(itemStack);
                    return;
                }
            }
            EquipmentSlot hand = event.getHand();
            if(hand == null) {
                MessageUtil.logMessage(ConsoleColor.RED, "Something wrong! Cannot remove the grenade item.");
                return;
            }
            // remove item when usage amount is lower than 1
            switch (hand) {
                case OFF_HAND -> player.getInventory().setItemInOffHand(null);
                case HAND -> player.getInventory().setItemInMainHand(null);
            }
        } else {
            MessageUtil.sendMessage(player, config.getLangConfig().getHasCooldown().replace("{time}", String.valueOf(cooldownSeconds)));
        }

    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {

        ThrownPotion potion = event.getPotion();
        Location location = event.getEntity().getLocation();
        ItemStack itemStack = potion.getItem();

        Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.COBWEB_GRENADE);
        if(customItemByType.isEmpty())
            return;

        CobwebGrenade cobwebGrenade = (CobwebGrenade) customItemByType.get();
        if(ItemUtil.sameItemName(itemStack, cobwebGrenade.getItem().getItemStack())) {
            cobwebGrenade.spawnCobweb(location); // spawn cobweb
        }
    }

}
