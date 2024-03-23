package dev.gether.getcustomitem.config.addon;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Particle;

@Getter
@Setter
@Builder
public class ParticleConfig {
    private boolean enable;
    private Particle particle;

    public ParticleConfig() {}

    public ParticleConfig(boolean enable, Particle particle) {
        this.enable = enable;
        this.particle = particle;
    }
}
