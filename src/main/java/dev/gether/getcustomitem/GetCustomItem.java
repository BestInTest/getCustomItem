package dev.gether.getcustomitem;

import dev.gether.getconfig.ConfigManager;
import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.bstats.Metrics;
import dev.gether.getcustomitem.cmd.CustomItemCommand;
import dev.gether.getcustomitem.cmd.arg.CustomItemArg;
import dev.gether.getcustomitem.cmd.handler.NoPermissionHandler;
import dev.gether.getcustomitem.cmd.handler.UsageCmdHandler;
import dev.gether.getcustomitem.config.Config;
import dev.gether.getcustomitem.config.LangConfig;
import dev.gether.getcustomitem.cooldown.CooldownManager;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.customize.CobwebGrenade;
import dev.gether.getcustomitem.item.manager.BearFurReducedManager;
import dev.gether.getcustomitem.item.manager.FrozenManager;
import dev.gether.getcustomitem.item.ItemManager;
import dev.gether.getcustomitem.item.manager.MagicTotemManager;
import dev.gether.getcustomitem.listener.*;
import dev.gether.getcustomitem.listener.global.PlayerQuitListener;
import dev.gether.getcustomitem.tasks.CobwebAutoRemoveTask;
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
    private CobwebAutoRemoveTask cobwebAutoRemoveTask;

    private ItemManager itemManager;

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

        // managers
        itemManager = new ItemManager(config);
        itemManager.initItems();

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
                new PlayerQuitListener(bearFurReducedManager, cooldownManager, frozenManager),
                new SnowballTeleport(itemManager, cooldownManager, config)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        // register command
        registerCommand(itemManager);

        // register bstats
        new Metrics(this, 21420);

        // cobweb autoremove task
        int removeAfter = 0;
        for (CustomItem customItem : config.getCustomItems()) {
            if (customItem instanceof CobwebGrenade cobwebGrenade) {
                removeAfter = cobwebGrenade.getAutoRemoveAfter();
            }
        }
        if (removeAfter > 0) {
            MessageUtil.logMessage("", "Starting cobweb auto remove task...");
            this.cobwebAutoRemoveTask = new CobwebAutoRemoveTask(removeAfter);
            this.cobwebAutoRemoveTask.runTaskTimer(this, 100, 60 * 20L); // every 1 min
        } else {
            MessageUtil.logMessage("", "Cobweb auto remove is disabled");
        }
    }

    private void registerCommand(ItemManager itemManager) {
        this.liteCommands = LiteBukkitFactory.builder("getCustomItem", this)
                .commands(
                        new CustomItemCommand(this)
                )
                .invalidUsage(new UsageCmdHandler())
                .missingPermission(new NoPermissionHandler())
                .argument(CustomItem.class, new CustomItemArg(itemManager))
                .build();
    }

    @Override
    public void onDisable() {

        //save cobweb locations
        if(this.cobwebAutoRemoveTask != null) {
            cobwebAutoRemoveTask.cancel();
            this.cobwebAutoRemoveTask.getCobwebLocations().saveToFile();
        }

        // unregister cmd
        if(this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        HandlerList.unregisterAll(this);

    }

    public void reload() {
        config.load();
        itemManager.initItems();
    }

    public static GetCustomItem getInstance() {
        return instance;
    }

    public CobwebAutoRemoveTask getCobwebAutoRemoveTask() {
        return cobwebAutoRemoveTask;
    }

    public LangConfig getLang() {
        return config.getLangConfig();
    }

}
