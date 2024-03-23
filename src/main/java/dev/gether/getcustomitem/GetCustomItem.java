package dev.gether.getcustomitem;

import dev.gether.getconfig.ConfigManager;
import dev.gether.getconfig.selector.SelectorAddon;
import dev.gether.getcustomitem.cmd.CustomItemCommand;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.FrozenManager;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.listener.*;
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
    private ItemManager itemManager;
    private SelectorAddon selectorAddon;
    private CooldownManager cooldownManager;
    private FrozenManager frozenManager;

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
        this.itemManager = new ItemManager(this.config, this);
        this.cooldownManager = new CooldownManager();
        this.frozenManager = new FrozenManager();

        Stream.of(
                new CobwebGrenadeListener(itemManager, cooldownManager, config),
                new FishRodListener(itemManager, cooldownManager, config),
                new CrossbowListener(this, itemManager, cooldownManager, config),
                new StickLevitationListener(itemManager, cooldownManager, config),
                new FrozenSwordListener(itemManager, cooldownManager, config, frozenManager),
                new AntyCobwebListener(itemManager, cooldownManager, config)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        // register command
        registerCommand();
    }

    private void registerCommand() {
        this.liteCommands = LiteBukkitFactory.builder("getCustomItem", this)
                .commands(
                        new CustomItemCommand(this.itemManager, this)
                )
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
}
