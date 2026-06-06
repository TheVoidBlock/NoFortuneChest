package io.github.thevoidblock.nofortunechest;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class NonSilkWarning {
    public static void initializeDetection() {
        AtomicBoolean hasAttacked = new AtomicBoolean(false);
        AtomicBoolean hasLooked = new AtomicBoolean(false);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

            if(
                    config.enabled &&
                    client.player != null &&
                    client.gameMode != null &&
                    client.gameMode.getPlayerMode() == GameType.SURVIVAL
            ) {

                AtomicBoolean isSuitable = new AtomicBoolean(true);

                ItemStack itemStack = client.player.getInventory().getSelectedItem();

                Set<Holder<Enchantment>> enchantments = itemStack.getEnchantments().keySet();
                Item item = itemStack.getItem();

                if(
                        (
                            item.equals(Items.NETHERITE_PICKAXE) ||
                            item.equals(Items.DIAMOND_PICKAXE)
                        ) &&
                        enchantments.stream().noneMatch(enchantment -> enchantment.is(Enchantments.SILK_TOUCH))
                ) {
                    isSuitable.set(false);
                }

                BlockHitResult result = null;

                if(
                        client.hitResult != null &&
                        client.hitResult.getType() == HitResult.Type.BLOCK
                ) result = (BlockHitResult) client.hitResult;

                assert client.level != null;

                if (
                        !isSuitable.get() &&
                        result != null && result.getType() == HitResult.Type.BLOCK &&
                        client.level.getBlockState(result.getBlockPos()).getBlock() == Blocks.ENDER_CHEST
                ) {
                    hasLooked.set(true);
                    if(config.actionBar) client.player.sendOverlayMessage(Component.literal(config.actionBarMessage).withColor(config.actionBarColor));

                    if(
                            config.title &&
                            client.options.keyAttack.isDown() &&
                            !hasAttacked.get()
                    ) {
                        client.gui.setTitle(Component.literal(config.titleMessage).withColor(config.titleColor));
                        hasAttacked.set(true);
                    }

                } else if(hasLooked.get()) {
                    client.gui.clearTitles();
                    client.gui.setOverlayMessage(Component.empty(), false);
                    hasAttacked.set(false);
                    hasLooked.set(false);
                }
            }
        });
    }
}
