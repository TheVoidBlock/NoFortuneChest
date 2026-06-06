package io.github.thevoidblock.nofortunechest.gui;

import io.github.thevoidblock.nofortunechest.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static io.github.thevoidblock.nofortunechest.NoFortuneChest.MOD_ID;

public class ConfigMenu {
    public static Screen getScreen(Screen parent) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable(String.format("nofortunechestconfig.%s.config", MOD_ID)));

        builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(ModConfig.class).save());

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable(String.format("category.%s.general", MOD_ID)));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable(String.format("option.%s.mod_toggle", MOD_ID)), config.modEnabled)
                .setDefaultValue(new ModConfig().modEnabled)
                .setSaveConsumer(newValue -> config.modEnabled = newValue)
                .build()
        );

        //Warning config
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable(String.format("option.%s.warning_toggle", MOD_ID)), config.warningEnabled)
                .setDefaultValue(new ModConfig().warningEnabled)
                .setSaveConsumer(newValue -> config.warningEnabled = newValue)
                .build()
        );

        general.addEntry(entryBuilder.startStrField(Component.translatable(String.format("option.%s.warning_message", MOD_ID)), config.warningMessage)
                .setDefaultValue(new ModConfig().warningMessage)
                .setTooltip(Component.translatable(String.format("tooltip.%s.warning_message", MOD_ID)))
                .setSaveConsumer(newValue -> config.warningMessage = newValue)
                .build()
        );

        general.addEntry(entryBuilder.startColorField(Component.translatable(String.format("option.%s.warning_color", MOD_ID)), config.warningColor)
                .setDefaultValue(new ModConfig().warningColor)
                .setTooltip(Component.translatable(String.format("tooltip.%s.warning_color", MOD_ID)))
                .setSaveConsumer(newValue -> config.warningColor = newValue)
                .build()
        );

        //Title config
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable(String.format("option.%s.title_toggle", MOD_ID)), config.titleEnabled)
                .setDefaultValue(new ModConfig().titleEnabled)
                .setSaveConsumer(newValue -> config.titleEnabled = newValue)
                .build()
        );

        general.addEntry(entryBuilder.startStrField(Component.translatable(String.format("option.%s.title_message", MOD_ID)), config.titleMessage)
                .setDefaultValue(new ModConfig().titleMessage)
                .setTooltip(Component.translatable(String.format("tooltip.%s.title_message", MOD_ID)))
                .setSaveConsumer(newValue -> config.titleMessage = newValue)
                .build()
        );

        general.addEntry(entryBuilder.startColorField(Component.translatable(String.format("option.%s.title_color", MOD_ID)), config.warningColor)
                .setDefaultValue(new ModConfig().titleColor)
                .setTooltip(Component.translatable(String.format("tooltip.%s.title_color", MOD_ID)))
                .setSaveConsumer(newValue -> config.titleColor = newValue)
                .build()
        );

        return builder.build();
    }
}
