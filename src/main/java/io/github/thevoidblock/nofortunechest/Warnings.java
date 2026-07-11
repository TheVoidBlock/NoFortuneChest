package io.github.thevoidblock.nofortunechest;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Warnings {
    private static boolean warningShown = false;
    private static boolean titleShown = false;

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(Warnings::showWarnings);
    }

    private static void showWarnings(Minecraft minecraft) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if(!config.enabled) return;

        if(config.actionBar && shouldShowWarning(config, minecraft)) {
            minecraft.gui.hud.setOverlayMessage(Component.literal(config.actionBarMessage).withColor(config.actionBarColor), false);
            warningShown = true;
        } else if(warningShown) {
            minecraft.gui.hud.setOverlayMessage(Component.empty(), false);
            warningShown = false;
        }

        if(config.title && shouldShowTitle(config, minecraft)) {
            if(!titleShown) minecraft.gui.hud.setTitle(Component.literal(config.titleMessage).withColor(config.titleColor));
            titleShown = true;
        } else if(titleShown) {
            minecraft.gui.hud.clearTitles();
            titleShown = false;
        }
    }

    private static boolean shouldShowWarning(ModConfig config, Minecraft minecraft) {
        if(minecraft.player == null) return false;
        GameType gameMode = minecraft.player.gameMode();
        if(gameMode == null || !gameMode.isSurvival()) return false;

        return isWarningItem(config, minecraft.player) && isTargetingEnderChest(minecraft);
    }

    private static boolean shouldShowTitle(ModConfig config, Minecraft minecraft) {
        return shouldShowWarning(config, minecraft) && minecraft.options.keyAttack.isDown();
    }

    private static boolean isTargetingEnderChest(Minecraft minecraft) {
        if(minecraft.level == null) return false;

        HitResult target = minecraft.hitResult;
        if(!(target instanceof BlockHitResult targetBlock)) return false;

        return minecraft.level.getBlockState(targetBlock.getBlockPos()).getBlock() == Blocks.ENDER_CHEST;
    }

    private static boolean isWarningItem(ModConfig config, Player player) {
        return player != null &&
                player.getDestroySpeed(Blocks.ENDER_CHEST.defaultBlockState()) >= config.destroySpeedThreshold &&
                player.getMainHandItem().getEnchantments().keySet().stream().noneMatch(enchantment -> enchantment.is(Enchantments.SILK_TOUCH));
    }
}
