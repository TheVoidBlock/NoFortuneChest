package io.github.thevoidblock.nofortunechest;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static io.github.thevoidblock.nofortunechest.NoFortuneChest.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData {
    public boolean enabled = true;

    public float destroySpeedThreshold = 8.0f;

    public boolean title = true;
    public String titleMessage = "⚠";
    @ConfigEntry.ColorPicker
    public int titleColor = 16776960;

    public boolean actionBar = true;
    public String actionBarMessage = "Warning: you are holding a non Silk Touch pickaxe";
    @ConfigEntry.ColorPicker
    public int actionBarColor = 16711680;
}
