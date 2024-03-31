package dev.gether.getcustomitem.listener;

import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.HookItem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.stream.Stream;

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

        HookItem hookItem = findHookItem(player); // find item

        if (hookItem == null || !hookItem.isEnabled()) return;

        PlayerFishEvent.State state = event.getState();
        if(state == PlayerFishEvent.State.IN_GROUND ||
            state == PlayerFishEvent.State.CAUGHT_ENTITY ||
            state == PlayerFishEvent.State.REEL_IN) {

            double cooldownSeconds = cooldownManager.getCooldownSecond(player, hookItem);
            String permissionBypass = hookItem.getPermissionBypass();
            if(cooldownSeconds <= 0 || player.hasPermission(permissionBypass)) {
                Location hooklocation = event.getHook().getLocation();
                Location playerLocation = player.getLocation();

                Vector vector = playerLocation.toVector();
                Vector direction = hooklocation.toVector().subtract(vector).normalize();

                double divideGliding = hookItem.getDivideGliding();
                double multiply = hookItem.getMultiply();
                if(player.isGliding() && divideGliding != 0)
                    multiply /= divideGliding;

                Vector velocity = direction.multiply(multiply);
                velocity.setY(velocity.getY()/hookItem.getDivideHeight());
                player.setVelocity(velocity);

                // set cooldown
                cooldownManager.setCooldown(player, hookItem);

                // verify a value to usage of item
                hookItem.takeUsage(player);

                // alert
                hookItem.notifyYourself(player);

            } else {
                MessageUtil.sendMessage(player, config.getLangConfig().getHasCooldown().replace("{time}", String.valueOf(cooldownSeconds)));
            }

        }
    }

    private HookItem findHookItem(Player player) {
        return Stream.of(player.getInventory().getItemInMainHand(), player.getInventory().getItemInOffHand())
                .map(item -> itemManager.findCustomItemByType(ItemType.HOOK, item))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(HookItem.class::isInstance)
                .map(HookItem.class::cast)
                .findFirst()
                .orElse(null);
    }
}
