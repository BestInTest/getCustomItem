package dev.gether.getcustomitem.config;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class RegionConfig {
    private List<String> disallowedRegions = new ArrayList<>(Collections.singletonList("spawn"));
}
