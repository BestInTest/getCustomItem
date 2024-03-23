package dev.gether.getcustomitem.listener;

import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.HookItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Optional;

public class FishRodListener implements Listener {

    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    private final Config config;

    public FishRodListener(ItemManager itemManager, CooldownManager cooldownManager, Config config) {
        this.itemManager = itemManager;
        this.cooldownManager = cooldownManager;
        this.config = config;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.HOOK);
        if(customItemByType.isEmpty() || !(customItemByType.get() instanceof HookItem hookItem))
            return;

        ItemStack customFishingRod = hookItem.getItem().getItemStack();

        // main-hand and off-hand is the same what customFishingRod - if not than cancel it / return
        if(!customFishingRod.isSimilar(mainHand) && !customFishingRod.isSimilar(offHand))
            return;

        // check the item is enabled
        if(!hookItem.isEnabled())
            return;

        PlayerFishEvent.State state = event.getState();
        if(state == PlayerFishEvent.State.IN_GROUND ||
            state == PlayerFishEvent.State.CAUGHT_ENTITY ||
            state == PlayerFishEvent.State.REEL_IN) {

            double cooldownSeconds = cooldownManager.getCooldownSecond(player, hookItem);
            String permissionBypass = hookItem.getPermissionBypass();
            if(cooldownSeconds <= 0 || player.hasPermission(permissionBypass)) {
                Location hooklocation = event.getHook().getLocation();
                Location playerLocation = player.getLocation();

                Vector direction = hooklocation.toVector().subtract(playerLocation.toVector());
                direction.normalize();

                Vector velocity = direction.multiply(hookItem.getMultiply());
                velocity.setY(velocity.getY()/hookItem.getDivideHeight());
                player.setVelocity(velocity);

                // set cooldown
                cooldownManager.setCooldown(player, hookItem);
            } else {
                MessageUtil.sendMessage(player, config.getLangConfig().getHasCooldown().replace("{time}", String.valueOf(cooldownSeconds)));
            }

        }
    }
}
