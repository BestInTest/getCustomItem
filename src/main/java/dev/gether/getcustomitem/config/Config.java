package dev.gether.getcustomitem.config;

import dev.gether.getconfig.GetConfig;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.particles.DustOptions;
import dev.gether.getconfig.domain.config.particles.ParticleConfig;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ItemBuilder;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import dev.gether.getcustomitem.item.customize.*;
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
                           SoundConfig.builder()
                                   .enable(true)
                                   .sound(Sound.UI_BUTTON_CLICK)
                                   .build(),
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
                    ),
                    new CobwebGrenade(
                            Item.builder()
                                    .itemStack(ItemBuilder.create(
                                            Material.SPLASH_POTION,
                                            "#ff004cCobweb grenade",
                                            new ArrayList<>(
                                                    List.of(
                                                            "&7",
                                                            "#ff175c× Throw the grande to create",
                                                            "#ff175c  a trap with cobweb &7(&f{radius-x}&7x&f{radius-y}&8) ",
                                                            "&7",
                                                            "&7• Usage: #ff004c{usage}",
                                                            "&7"
                                                    )
                                            ),
                                            true
                                    ))
                                    .build(),
                            ItemType.COBWEB_GRENADE,
                            10,
                            "getcustomitem.grenadecobweb.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_WITHER_SHOOT)
                                    .build(),
                            ParticleConfig.builder()
                                    .enable(true)
                                    .dustOptions(new DustOptions(210, 255, 97, 5))
                                    .particle(Particle.REDSTONE)
                                    .build(),
                            2,
                            2,
                            0.55,
                            5
                    ),
                    new StickLevitation(
                            Item.builder()
                                    .itemStack(ItemBuilder.create(
                                            Material.BLAZE_ROD,
                                            "#ff9436Stick of levitation",
                                            new ArrayList<>(
                                                    List.of(
                                                            "&7",
                                                            "#ffba61× Use this item to",
                                                            "#ffba61  give the levitation effect",
                                                            "#ffba61  to near players &7(&f{radius}&7x&f{radius}&7)",
                                                            "&7"
                                                    )
                                            ),
                                            true
                                    ))
                                    .build(),
                            ItemType.STICK_LEVITATION,
                            10,
                            "getcustomitem.sticklevitation.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_VILLAGER_TRADE)
                                    .build(),
                            "LEVITATION",
                            3,
                            1,
                            5
                    ),
                    new FrozenSword(
                            Item.builder()
                                    .itemStack(ItemBuilder.create(
                                            Material.OXEYE_DAISY,
                                            "#3366ffFrozen sword",
                                            new ArrayList<>(
                                                    List.of(
                                                            "&7",
                                                            "#527dff× Hit the player to have",
                                                            "#527dff  a {chance}% chance of freezing",
                                                            "#527dff  them for {seconds} seconds!",
                                                            "&7"
                                                    )
                                            ),
                                            true
                                    ))
                                    .build(),
                            ItemType.FROZEN_SWORD,
                            10,
                            "getcustomitem.frozensword.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.BLOCK_SNOW_HIT)
                                    .build(),
                            2,
                            20
                    ),
                    new AntyCobweb(
                            Item.builder()
                                    .itemStack(ItemBuilder.create(
                                            Material.SOUL_LANTERN,
                                            "#1aff00Anty-cobweb",
                                            new ArrayList<>(
                                                    List.of(
                                                            "&7",
                                                            "#78ff69× Use this item to",
                                                            "#78ff69  remove all cobweb",
                                                            "#78ff69  in radius &7(&f{radius}&7x&f{radius}&7)",
                                                            "&7"
                                                    )
                                            ),
                                            true
                                    ))
                                    .build(),
                            ItemType.ANTY_COBWEB,
                            10,
                            "getcustomitem.antycobweb.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
                                    .build(),
                            2,
                            2
                    )
            )
    );

    private LangConfig langConfig = new LangConfig();

}
