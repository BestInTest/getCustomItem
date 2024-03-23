package dev.gether.getcustomitem.listener;

import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.AntyCobweb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class AntyCobwebListener implements Listener {

    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    private final Config config;

    public AntyCobwebListener(ItemManager itemManager, CooldownManager cooldownManager, Config config) {
        this.itemManager = itemManager;
        this.cooldownManager = cooldownManager;
        this.config = config;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem(); // using item

        Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.ANTY_COBWEB);
        if(customItemByType.isEmpty() || !(customItemByType.get() instanceof AntyCobweb antyCobweb))
            return;

        if(itemStack == null || !itemStack.isSimilar(antyCobweb.getItemStack()))
            return;

        event.setCancelled(true);

        Action action = event.getAction();
        if(action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) {
            return;
        }

        double cooldownSeconds = cooldownManager.getCooldownSecond(player, antyCobweb);
        if(cooldownSeconds <= 0 || player.hasPermission(antyCobweb.getPermissionBypass())) {
            // set cooldown
            cooldownManager.setCooldown(player, antyCobweb);

            // particles and sound
            antyCobweb.playSound(player.getLocation()); // play sound

            // clean cobweb
            antyCobweb.cleanCobweb(player.getLocation());


        } else {
            MessageUtil.sendMessage(player, config.getLangConfig().getHasCooldown().replace("{time}", String.valueOf(cooldownSeconds)));
        }

    }

}
