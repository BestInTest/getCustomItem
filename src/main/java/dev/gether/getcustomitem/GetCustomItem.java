package dev.gether.getcustomitem;

import dev.gether.getconfig.ConfigManager;
import dev.gether.getconfig.selector.SelectorAddon;
import dev.gether.getcustomitem.cmd.CustomItemCommand;
import dev.gether.getcustomitem.cmd.arg.CustomItemArg;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.config.LangConfig;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.manager.BearFurReducedManager;
import dev.gether.getcustomitem.item.manager.FrozenManager;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.manager.MagicTotemManager;
import dev.gether.getcustomitem.listener.*;
import dev.gether.getcustomitem.listener.global.PlayerQuitListener;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.stream.Stream;

public final class GetCustomItem extends JavaPlugin {

    private static GetCustomItem instance;
    private LiteCommands<CommandSender> liteCommands;
    private Config config;
    //
    private SelectorAddon selectorAddon;

    @Override
    public void onLoad() {
        instance = this;

        this.config = ConfigManager.create(Config.class, it -> {
            it.file(new File(getDataFolder(), "config.yml"));
            it.load();
        });
    }
    @Override
    public void onEnable() {
        // init selector
        this.selectorAddon = new SelectorAddon();
        this.selectorAddon.enable(this);

        // managers
        ItemManager itemManager = new ItemManager(config);
        CooldownManager cooldownManager = new CooldownManager(config);
        FrozenManager frozenManager = new FrozenManager();
        BearFurReducedManager bearFurReducedManager = new BearFurReducedManager();
        MagicTotemManager magicTotemManager = new MagicTotemManager();

        // listeners
        Stream.of(
                new CobwebGrenadeListener(itemManager, cooldownManager, config),
                new FishRodListener(itemManager, cooldownManager, config),
                new CrossbowListener(this, itemManager, cooldownManager, config),
                new EffectRadiusListener(itemManager, cooldownManager, config),
                new FrozenSwordListener(itemManager, cooldownManager, config, frozenManager),
                new AntyCobwebListener(itemManager, cooldownManager, config),
                new MagicTotemListener(itemManager, cooldownManager, config, magicTotemManager),
                new BearFurListener(itemManager, cooldownManager, bearFurReducedManager, config),
                new HitEffectListener(itemManager, cooldownManager, config),
                new PlayerQuitListener(bearFurReducedManager, cooldownManager, frozenManager)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        // register command
        registerCommand(itemManager);
    }

    private void registerCommand(ItemManager itemManager) {
        this.liteCommands = LiteBukkitFactory.builder("getCustomItem", this)
                .commands(
                        new CustomItemCommand(this)
                )
                .argument(CustomItem.class, new CustomItemArg(itemManager))
                .build();
    }

    @Override
    public void onDisable() {

        // unregister cmd
        if(this.liteCommands != null) {
            this.liteCommands.unregister();
        }
        // unregister selector
        if(this.selectorAddon != null) {
            this.selectorAddon.disable();
        }

        HandlerList.unregisterAll(this);

    }

    public SelectorAddon getSelectorAddon() {
        return selectorAddon;
    }

    public static GetCustomItem getInstance() {
        return instance;
    }

    public LangConfig getLang() {
        return config.getLangConfig();
    }
}
