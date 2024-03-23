package dev.gether.getcustomitem.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LangConfig {
    private String hasCooldown = "#ff2137 × Please wait {time} before you can do this again.";
    private String crossbowTeleport = "#b042ff × Successfully hit the player and teleporting to you.";
    private String youAreFrozen = "#b042ff • You have been frozen by {player}!";
    private String frozenPlayer = "#b042ff • You froze the {player}!";

}
