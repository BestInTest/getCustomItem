package dev.gether.getcustomitem.item;

import dev.gether.getconfig.utils.ItemUtil;
import dev.gether.getcustomitem.config.Config;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ItemManager {

    private final Config config;
    public ItemManager(Config config) {
        this.config = config;
    }

    public Optional<CustomItem> findCustomItemByType(ItemType itemType, ItemStack itemStack) {
        return config.getCustomItems().stream()
                .filter(item -> item.getItemType() == itemType)
                .filter(item -> ItemUtil.sameItemName(item.getItemStack(), itemStack)).findFirst();
    }

    public Optional<CustomItem> findCustomItemByKey(String key) {
        return config.getCustomItems().stream()
                .filter(item -> item.getKey().equalsIgnoreCase(key)).findFirst();
    }

    public SuggestionResult getAllItemKey() {
        return config.getCustomItems().stream()
                .map(CustomItem::getKey).collect(SuggestionResult.collector());
    }

    public void initItems() {
        config.getCustomItems().forEach(CustomItem::init);
    }


    /*
    public Optional<ItemStack> findItemStackByType(ItemType itemType) {
        return config.getCustomItems().stream()
                .filter(item -> item.getItemType() == itemType)
                .map(item -> item.getItem().getItemStack()).findFirst();
    }
    */




}
