package dev.gether.getcustomitem.item.customize;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.gether.getconfig.domain.Item;
import dev.gether.getconfig.domain.config.particles.ParticleConfig;
import dev.gether.getconfig.domain.config.sound.SoundConfig;
import dev.gether.getconfig.utils.ColorFixer;
import dev.gether.getconfig.utils.ParticlesUtil;
import dev.gether.getcustomitem.GetCustomItem;
import dev.gether.getcustomitem.item.CustomItem;
import dev.gether.getcustomitem.item.ItemType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Arrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonTypeName("crossbow")
public class CrossBowItem extends CustomItem {
    private ParticleConfig particleConfig;
    private String permissionIgnoreEffect = "getcustomitem.crossbow.effect.bypass";
    private double chance = 30;
    public CrossBowItem() {}

    public CrossBowItem(Item item, ItemType itemType, int cooldown, String permissionBypass, SoundConfig soundConfig, ParticleConfig particleConfig) {
        super(item, itemType, cooldown, permissionBypass, soundConfig);
        this.particleConfig = particleConfig;
    }


    @Override
    public Item getItem() {
        ItemStack itemStack = super.getItem().getItemStack().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta != null) {
            List<String> lore = new ArrayList<>();
            if (itemMeta.hasLore())
                lore.addAll(itemMeta.getLore());

            lore.replaceAll(line -> line.replace("{chance}", String.valueOf(chance)));
            itemMeta.setLore(ColorFixer.addColors(lore));
        }
        itemStack.setItemMeta(itemMeta);
        return Item.builder()
                .itemStack(itemStack)
                .build();
    }

    public void runParticles(Arrow arrow) {

        // check the particles is enabled
        if(!particleConfig.isEnable())
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                // check if the arrow has landed or is removed
                if (arrow.isOnGround() || !arrow.isValid()) {
                    this.cancel();
                    return;
                }

                ParticlesUtil.spawnParticles(arrow, particleConfig);
            }
        }.runTaskTimerAsynchronously(GetCustomItem.getInstance(), 0L, 0L);
    }
}
