package dev.gether.getcustomitem.config;

import dev.gether.getconfig.GetConfig;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.utils.ItemBuilder;
import dev.gether.getcustomitem.config.addon.ParticleConfig;
import dev.gether.getcustomitem.config.addon.SoundConfig;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.CrossBowItem;
import dev.gether.getcustomitem.item.customize.HookItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class Config extends GetConfig {

    private Set<CustomItem> customItems = new HashSet<>(
            Set.of(
                   new HookItem(
                           Item.builder()
                                   .itemStack(ItemBuilder.create(
                                           Material.FISHING_ROD,
                                           "#f2ff69Magic fishing rod!",
                                           new ArrayList<>(
                                                   List.of(
                                                           "&7",
                                                           "#beff69× Use right click!",
                                                           "&7"
                                                   )
                                           ),
                                           true
                                   ))
                                   .build(),
                           ItemType.HOOK,
                           5,
                           "getcustomitem.hook.bypass",
                           4,
                           1.7
                    ),
                    new CrossBowItem(
                            Item.builder()
                                    .itemStack(ItemBuilder.create(
                                            Material.CROSSBOW,
                                            "#40ffe9Teleporting crossbow",
                                            new ArrayList<>(
                                                    List.of(
                                                            "&7",
                                                            "#85fff1× Hit a player and move him to you",
                                                            "#85fff1× Chance: #c2fff8{chance}%",
                                                            "&7"
                                                    )
                                            ),
                                            true
                                    ))
                                    .build(),
                            ItemType.CROSSBOW,
                            10,
                            "getcustomitem.crossbow.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_TNT_PRIMED)
                                    .build(),
                            ParticleConfig.builder()
                                    .enable(true)
                                    .particle(Particle.HEART)
                                    .build()
                    )
            )
    );

    private LangConfig langConfig = new LangConfig();

}
