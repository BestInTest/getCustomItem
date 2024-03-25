package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.Flags;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.utils.WorldGuardUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("anty_cobweb")
public class AntyCobweb extends CustomItem {

    private int radiusX;
    private int radiusY;

    public AntyCobweb() {}

    public AntyCobweb(String key, String categoryName, boolean cooldownCategory, int usage, Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, List<String> notifyYourself, List<String> notifyOpponents, int radiusX, int radiusY) {
        super(key, categoryName, cooldownCategory, usage, item, itemType, cooldown, permissionBypass, soundConfig, notifyYourself, notifyOpponents);
        this.radiusX = radiusX;
        this.radiusY = radiusY;
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
                    .replace("{radius-x}", String.valueOf(radiusX))
                    .replace("{radius-y}", String.valueOf(radiusY))
                    .replace("{usage}", String.valueOf(usage))
            );
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void cleanCobweb(Location location) {
        for (int x = -radiusX + 1; x < radiusX; x++) {
            for (int y = -radiusY + 1; y < radiusY; y++) {
                for (int z = -radiusX + 1; z < radiusX; z++) {
                    Location tempLoc = location.clone().add(x, y, z);
                    com.sk89q.worldedit.util.Location locWordGuard = BukkitAdapter.adapt(tempLoc);
                    if (WorldGuardUtil.isInRegion(locWordGuard) &&
                            WorldGuardUtil.isDeniedFlag(tempLoc, Flags.BLOCK_BREAK)) {
                        continue;
                    }
                    Block block = tempLoc.getBlock();
                    if (block.getType() == Material.COBWEB) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }
}
