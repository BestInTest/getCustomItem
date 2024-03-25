package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("bear_fur")
public class BearFurItem extends CustomItem {

    private double reducedDamage;
    private int seconds;
    public BearFurItem() {}

    public BearFurItem(String key, String categoryName, boolean cooldownCategory, int usage, Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, List<String> notifyYourself, List<String> notifyOpponents, double reducedDamage, int seconds) {
        super(key, categoryName, cooldownCategory, usage, item, itemType, cooldown, permissionBypass, soundConfig, notifyYourself, notifyOpponents);
        this.reducedDamage = reducedDamage;
        this.seconds = seconds;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = getItem().getItemStack().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setUnbreakable(true);

            // set usage to persistent data
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, usage);

            List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore())
                lore.addAll(itemMeta.getLore());

            lore.replaceAll(line -> line
                    .replace("{reduced-damage}", String.valueOf(reducedDamage))
                    .replace("{seconds}", String.valueOf(seconds))
                    .replace("{usage}", String.valueOf(usage))
            );
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
