package dev.gether.getcustomitem.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getcustomitem.item.customize.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HookItem.class, name = "hook"),
        @JsonSubTypes.Type(value = CrossBowItem.class, name = "crossbow"),
        @JsonSubTypes.Type(value = CobwebGrenade.class, name = "cobweb_grenade"),
        @JsonSubTypes.Type(value = StickLevitation.class, name = "stick_levitation"),
        @JsonSubTypes.Type(value = FrozenSword.class, name = "frozen_sword"),
        @JsonSubTypes.Type(value = AntyCobweb.class, name = "anty_cobweb"),
})
public class CustomItem {
    private boolean enabled = true;
    private Item item;
    private ItemType itemType;
    private int cooldown; // time in second
    private String permissionBypass;

    private SoundConfig soundConfig;

    public CustomItem() {}

    public CustomItem(Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig) {
        this.item = item;
        this.itemType = itemType;
        this.cooldown = cooldown;
        this.permissionBypass = permissionBypass;
        this.soundConfig = soundConfig;
    }

    public void playSound(Location location) {
        // check sound is enabled
        if(!soundConfig.isEnable())
            return;

        location.getWorld().playSound(location, soundConfig.getSound(), 1F, 1F);
    }

    @JsonIgnore
    public ItemStack getItemStack() {
        return item.getItemStack();
    }
}
