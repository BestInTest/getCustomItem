package dev.gether.getcustomitem.listener;

import dev.gether.getconfig.utils.ItemUtil;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.ItemType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class EntityShootBowListener {

    private final ItemManager itemManager;
    private final CooldownManager cooldownManager;
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player shooter &&
                event.getProjectile() instanceof Arrow) {

            ItemStack bow = event.getBow();
            Optional<CustomItem> customItemByType = itemManager.findCustomItemByType(ItemType.CROSSBOW);
            if(customItemByType.isEmpty())
                return;

            CustomItem customItem = customItemByType.get();
            if (ItemUtil.sameItem(customItem.getItem().getItemStack(), bow)) {
                double cooldownSecond = cooldownManager.getCooldownSecond(shooter, customItem);
                if(cooldownSecond > 0) {
                    event.setCancelled(true);

                    return;
                }
                onPlayerShoot((Arrow) event.getProjectile());
            }
        }
    }
}
