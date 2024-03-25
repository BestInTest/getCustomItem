package dev.gether.getcustomitem.config;

import dev.gether.getconfig.GetConfig;
import dev.gether.getconfig.annotation.Comment;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.particles.DustOptions;
import dev.gether.getconfig.domain.config.particles.ParticleConfig;
import dev.gether.getconfig.domain.config.potion.PotionEffectConfig;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
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

    @Comment({
            "",
            "author - https://www.spigotmc.org/resources/authors/gethertv.571046/",
            "discord: https://dc.gether.dev",
            "",
            "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html",
            "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html",
            "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html",
            ""
    })
    private Set<CustomItem> customItems = new HashSet<>(
            Set.of(
                    new HookItem(
                            "hook",
                            "hook_category",
                            -1,
                            Item.builder()
                                    .material(Material.FISHING_ROD)
                                    .displayname("#f2ff69Magic fishing rod!")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#beff69× Use right click!",
                                                    "&7",
                                                    "&7• Usage: #beff69{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.HOOK,
                            5,
                            "getcustomitem.hook.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.UI_BUTTON_CLICK)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            4,
                            1.7
                    ),
                    new CrossBowItem(
                            "crossbow",
                            "crossbow_category",
                            -1,
                            Item.builder()
                                    .material(Material.CROSSBOW)
                                    .displayname("#40ffe9Teleporting crossbow")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#85fff1× Hit a player and move him to you",
                                                    "#85fff1× Chance: #c2fff8{chance}%",
                                                    "&7",
                                                    "&7&7• Usage: #85fff1{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.CROSSBOW,
                            10,
                            "getcustomitem.crossbow.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_TNT_PRIMED)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            ParticleConfig.builder()
                                    .enable(true)
                                    .particle(Particle.HEART)
                                    .build(),
                            "getcustomitem.crossbow.ignore",
                            50
                    ),
                    new CobwebGrenade(
                            "cobweb_grenade",
                            "cobweb_category",
                            5,
                            Item.builder()
                                    .material(Material.SPLASH_POTION)
                                    .displayname("#ff004cCobweb grenade")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#ff175c× Throw the grande to create",
                                                    "#ff175c  a trap with cobweb &7(&f{radius-x}&7x&f{radius-y}&8) ",
                                                    "&7",
                                                    "&7• Usage: #ff004c{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.COBWEB_GRENADE,
                            10,
                            "getcustomitem.grenadecobweb.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_WITHER_SHOOT)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            ParticleConfig.builder()
                                    .enable(true)
                                    .dustOptions(new DustOptions(210, 255, 97, 5))
                                    .particle(Particle.REDSTONE)
                                    .build(),
                            2,
                            2,
                            0.95,
                            3
                    ),
                    new EffectRadiusItem(
                            "stick_levitation",
                            "levitation_category",
                            5,
                            Item.builder()
                                    .material(Material.BLAZE_ROD)
                                    .displayname("#ff9436Stick of levitation")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#ffba61× Use this item to give",
                                                    "#ffba61  the levitation effect on X seconds",
                                                    "#ffba61  to near players &7(&f{radius}&7x&f{radius}&7)",
                                                    "&7",
                                                    "&7• Usage: #ffba61{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.EFFECT_RADIUS,
                            10,
                            "getcustomitem.sticklevitation.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_VILLAGER_TRADE)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            true,
                            true,
                            5,
                            new ArrayList<>(
                                    List.of(
                                            new PotionEffectConfig(
                                                    "LEVITATION",
                                                    3,
                                                    1
                                            )
                                    )
                            ),
                            new ArrayList<>()
                    ),
                    new EffectRadiusItem(
                            "yeti_eye",
                            "yeti_eye_category",
                            5,
                            Item.builder()
                                    .material(Material.SPIDER_EYE)
                                    .displayname("#0015ffYeti eye")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#2e3fff× Use this item to give",
                                                    "#2e3fff  the weakness effect on X seconds",
                                                    "#2e3fff  to near players &7(&f{radius}&7x&f{radius}&7)",
                                                    "&7",
                                                    "&7• Usage: #ffba61{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.EFFECT_RADIUS,
                            10,
                            "getcustomitem.yetieye.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_POLAR_BEAR_HURT)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            true,
                            true,
                            5,
                            new ArrayList<>(
                                    List.of(
                                            new PotionEffectConfig(
                                                    "WEAKNESS",
                                                    3,
                                                    1
                                            )
                                    )
                            ),
                            new ArrayList<>()
                    ),
                    new EffectRadiusItem(
                            "air_filter",
                            "air_filter_category",
                            1,
                            Item.builder()
                                    .material(Material.FLINT)
                                    .displayname("#608a71Air filter")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#96b0a0× Use this item to clean",
                                                    "#96b0a0  a negative effect from yourself",
                                                    "&7",
                                                    "&7• Usage: #96b0a0{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.EFFECT_RADIUS,
                            10,
                            "getcustomitem.airfilter.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ITEM_TOTEM_USE)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            true,
                            false,
                            2,
                            new ArrayList<>(),
                            new ArrayList<>(
                                    List.of(
                                            "WEAKNESS"
                                    )
                            )
                    ),
                    new EffectRadiusItem(
                            "ice_rod",
                            "ice_rod_category",
                            3,
                            Item.builder()
                                    .material(Material.TRIDENT)
                                    .displayname("#737d9cIce rod")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#9aa1b8× Use this item to remove",
                                                    "#9aa1b8  all positive effects from",
                                                    "#9aa1b8  players within a &f{radius}&7x&f{radius}#9aa1b8 radius",
                                                    "&7",
                                                    "&7• Usage: #9aa1b8{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.EFFECT_RADIUS,
                            10,
                            "getcustomitem.icerod.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.BLOCK_SNOW_HIT)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            false,
                            true,
                            5,
                            new ArrayList<>(),
                            new ArrayList<>(
                                    List.of(
                                            "SPEED",
                                            "INCREASE_DAMAGE",
                                            "JUMP"
                                    )
                            )
                    ),
                    new FrozenSword(
                            "frozen_sword",
                            "frozen_sword_category",
                            -1,
                            Item.builder()
                                    .material(Material.OXEYE_DAISY)
                                    .displayname("#3366ffFrozen sword")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#527dff× Hit the player to have",
                                                    "#527dff  a {chance}% chance of freezing",
                                                    "#527dff  them for {seconds} seconds!",
                                                    "&7",
                                                    "&7• Usage: #527dff{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.FROZEN_SWORD,
                            10,
                            "getcustomitem.frozensword.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.BLOCK_SNOW_HIT)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            2,
                            20
                    ),
                    new AntyCobweb(
                            "anty_cobweb",
                            "anty_cobweb_category",
                            1,
                            Item.builder()
                                    .material(Material.SOUL_LANTERN)
                                    .displayname("#1aff00Anty-cobweb")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#78ff69× Use this item to",
                                                    "#78ff69  remove all cobweb",
                                                    "#78ff69  in radius &7(&f{radius-x}&7x&f{radius-y}&7)",
                                                    "&7",
                                                    "&7• Usage: #78ff69{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.ANTY_COBWEB,
                            10,
                            "getcustomitem.antycobweb.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            2,
                            2
                    ),
                    new MagicTotemItem(
                            "magic_totem",
                            "magic_totem_category",
                            1,
                            Item.builder()
                                    .material(Material.TOTEM_OF_UNDYING)
                                    .displayname("#ff6a00Magic totem")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#ffb03b× Use this item to",
                                                    "#ffb03b  preserve {chance}% of your",
                                                    "#ffb03b  inventory upon dying",
                                                    "&7",
                                                    "&7• Usage: #ffb03b{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.MAGIC_TOTEM,
                            10,
                            "getcustomitem.magictotem.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            10
                    ),
                    new BearFurItem(
                            "bear_fur",
                            "bear_fur_category",
                            10,
                            Item.builder()
                                    .material(Material.PHANTOM_MEMBRANE)
                                    .displayname("#85f2ffBear fur")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#c7f9ff× Use this item to reduced",
                                                    "#c7f9ff  your damage by {reduced-damage}%",
                                                    "#c7f9ff  for {seconds} seconds",
                                                    "&7",
                                                    "&7• Usage: #c7f9ff{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.BEAR_FUR,
                            10,
                            "getcustomitem.bearfur.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.BLOCK_ANVIL_USE)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            50,
                            5
                    ),
                    new HitEffectItem(
                            "wizard_staff",
                            "wizard_staff_category",
                            -1,
                            Item.builder()
                                    .material(Material.REDSTONE_TORCH)
                                    .displayname("#8c19ffWizard's staff")
                                    .lore(new ArrayList<>(
                                            List.of(
                                                    "&7",
                                                    "#a74fff× Hit the player",
                                                    "#a74fff  and give him custom effect",
                                                    "#a74fff  Chance: &f{chance}%",
                                                    "&7",
                                                    "&7• Usage: #a74fff{usage}",
                                                    "&7"
                                            )
                                    ))
                                    .unbreakable(true)
                                    .glow(true)
                                    .build(),
                            ItemType.HIT_EFFECT,
                            10,
                            "getcustomitem.wizardstaff.bypass",
                            SoundConfig.builder()
                                    .enable(true)
                                    .sound(Sound.BLOCK_ENCHANTMENT_TABLE_USE)
                                    .build(),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example YOURSELF!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of("&7",
                                            "#78ff69 × Example OPPONENTS!",
                                            "&7")
                            ),
                            new ArrayList<>(
                                    List.of(
                                            new PotionEffectConfig("SPEED", 5, 1)
                                    )
                            ),
                            50
                    )
            )
    );

    private LangConfig langConfig = new LangConfig();

}
