package io.github.thevoidblock.nofortunechest;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoFortuneChest implements ClientModInitializer {

    public static final String MOD_ID = "nofortunechest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {

        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        NonSilkWarning.initializeDetection();

        LOGGER.info("{} initialized!", MOD_ID);
    }
}
