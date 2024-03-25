package dev.gether.getcustomitem.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getconfig.utils.ItemUtil;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.GetCustomItem;
import dev.gether.getcustomitem.item.customize.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HookItem.class, name = "hook"),
        @JsonSubTypes.Type(value = CrossBowItem.class, name = "crossbow"),
        @JsonSubTypes.Type(value = CobwebGrenade.class, name = "cobweb_grenade"),
        @JsonSubTypes.Type(value = EffectRadiusItem.class, name = "stick_levitation"),
        @JsonSubTypes.Type(value = FrozenSword.class, name = "frozen_sword"),
        @JsonSubTypes.Type(value = AntyCobweb.class, name = "anty_cobweb"),
        @JsonSubTypes.Type(value = BearFurItem.class, name = "bear_fur"),
        @JsonSubTypes.Type(value = MagicTotemItem.class, name = "magic_totem"),
        @JsonSubTypes.Type(value = HitEffectItem.class, name = "hit_effect"),
})
public class CustomItem {
    @JsonIgnore
    protected NamespacedKey namespacedKey;
    private boolean enabled = true;
    private String key;
    private String categoryName;
    private boolean cooldownCategory;
    protected int usage;
    private Item item;
    private ItemType itemType;
    private int cooldown; // time in seconds
    private String permissionBypass;
    private SoundConfig soundConfig;
    private List<String> notifyYourself;
    private List<String> notifyOpponents;

    public CustomItem() {
        this.namespacedKey = new NamespacedKey(GetCustomItem.getInstance(), key+"_usage");
    }

    public CustomItem(String key,
                      String categoryName,
                      boolean cooldownCategory,
                      int usage,
                      Item item,
                      ItemType itemType,
                      int cooldown,
                      String permissionBypass,
                      SoundConfig soundConfig,
                      List<String> notifyYourself,
                      List<String> notifyOpponents) {
        this.namespacedKey = new NamespacedKey(GetCustomItem.getInstance(), key+"_usage");
        this.key = key;
        this.categoryName = categoryName;
        this.cooldownCategory = cooldownCategory;
        this.usage = usage;
        this.item = item;
        this.itemType = itemType;
        this.cooldown = cooldown;
        this.permissionBypass = permissionBypass;
        this.soundConfig = soundConfig;
        this.notifyYourself = notifyYourself;
        this.notifyOpponents = notifyOpponents;
    }

    public void playSound(Location location) {
        // check sound is enabled
        if(!soundConfig.isEnable())
            return;

        location.getWorld().playSound(location, soundConfig.getSound(), 1F, 1F);
    }

    @JsonIgnore
    public ItemStack getItemStack() {
        ItemStack itemStack = item.getItemStack().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {

            itemMeta.setUnbreakable(true);

            // set usage to persistent data
            itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, usage);

            List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore())
                lore.addAll(itemMeta.getLore());

            lore.replaceAll(line -> line
                    .replace("{usage}", usage > 0 ? String.valueOf(usage) : GetCustomItem.getInstance().getLang().getNoLimit())
            );
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getUsage(ItemMeta itemMeta) {
        if (itemMeta == null)
            return 0;

        Integer value = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
        return value != null ? value : 0;
    }

    public void takeAmount(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return;

        Integer usage = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
        if (usage == null)
            return;

        // ignore verify usage value because in other case im verify the number of usage
        // and if the number is lower than 1 im just remove it
        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.INTEGER, usage - 1);
        itemStack.setItemMeta(itemMeta);

    }

    public void updateItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return;

        Integer usage = itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
        if (usage == null)
            return;

        // default original item
        ItemStack originalItem = item.getItemStack().clone();
        ItemMeta originalMeta = originalItem.getItemMeta();

        if (originalMeta == null || !originalMeta.hasLore())
            return;

        List<String> lore = new ArrayList<>(originalMeta.getLore());
        lore.replaceAll(line -> line
                .replace("{usage}", String.valueOf(usage))
        );

        itemMeta.setLore(ColorFixer.addColors(lore));
        itemStack.setItemMeta(itemMeta);
    }

    public void takeUsage(Player player, ItemStack itemStack, EquipmentSlot equipmentSlot) {
        int usage = getUsage(itemStack.getItemMeta());
        if(usage != -1) {
            if(usage == 1) {
                if(equipmentSlot == EquipmentSlot.OFF_HAND)
                    player.getInventory().setItemInOffHand(null);
                else
                    player.getInventory().setItemInMainHand(null);
            } else {
                takeAmount(itemStack);
                updateItem(itemStack);
            }
        }
    }

    public void takeUsage(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if(ItemUtil.sameItem(mainHand, getItemStack())) {
            takeUsage(player, mainHand, EquipmentSlot.HAND);
        } else {
            takeUsage(player, player.getInventory().getItemInOffHand(), EquipmentSlot.OFF_HAND);
        }
    }

    public void notifyYourself(Player player) {
        if(notifyYourself.isEmpty())
            return;

        MessageUtil.sendMessage(player, String.join("\n", notifyYourself));
    }

    public void notifyOpponents(Player player) {
        if(notifyOpponents.isEmpty())
            return;

        MessageUtil.sendMessage(player, String.join("\n", notifyOpponents));
    }
}
