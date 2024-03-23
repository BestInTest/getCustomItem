package dev.gether.getcustomitem.item;

import dev.gether.getcustomitem.GetCustomItem;
import dev.gether.getcustomitem.config.Config;

import java.util.Optional;

public class ItemManager {

    private final Config config;
    private final GetCustomItem plugin;

    public ItemManager(Config config, GetCustomItem plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    public Optional<CustomItem> findCustomItemByType(ItemType itemType) {
        return config.getCustomItems().stream().filter(item -> item.getItemType() == itemType).findFirst();
    }


    /*
    public Optional<ItemStack> findItemStackByType(ItemType itemType) {
        return config.getCustomItems().stream()
                .filter(item -> item.getItemType() == itemType)
                .map(item -> item.getItem().getItemStack()).findFirst();
    }
    */




}
