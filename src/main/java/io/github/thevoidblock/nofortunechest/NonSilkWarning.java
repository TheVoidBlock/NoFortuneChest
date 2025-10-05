package io.github.thevoidblock.nofortunechest;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class NonSilkWarning {
    public static void initializeDetection() {
        AtomicBoolean hasAttacked = new AtomicBoolean(false);
        AtomicBoolean hasLooked = new AtomicBoolean(false);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

            if(
                    config.modEnabled &&
                    client.player != null
            ) {

                AtomicBoolean isSuitable = new AtomicBoolean(true);

                ItemStack itemStack = client.player.getInventory().getSelectedStack();

                Set<RegistryEntry<Enchantment>> enchantments = itemStack.getEnchantments().getEnchantments();
                Item item = itemStack.getItem();

                if(
                        (
                            item.equals(Items.NETHERITE_PICKAXE) ||
                            item.equals(Items.DIAMOND_PICKAXE)
                        ) &&
                        enchantments.stream().noneMatch(enchantmentRegistryEntry -> enchantmentRegistryEntry.getKey().get().getValue().getPath().equalsIgnoreCase("silk_touch"))
                ) {
                    isSuitable.set(false);
                }

                BlockHitResult result = null;

                if(
                        client.crosshairTarget != null &&
                        client.crosshairTarget.getType() == HitResult.Type.BLOCK
                ) result = (BlockHitResult) client.crosshairTarget;

                assert client.world != null;

                if (
                        !isSuitable.get() &&
                        result != null && result.getType() == BlockHitResult.Type.BLOCK &&
                        client.world.getBlockState(result.getBlockPos()).getBlock() == Blocks.ENDER_CHEST
                ) {
                    hasLooked.set(true);
                    if(config.warningEnabled) client.player.sendMessage(Text.literal(config.warningMessage).withColor(config.warningColor), true);

                    if(
                            config.titleEnabled &&
                            client.options.attackKey.isPressed() &&
                            !hasAttacked.get()
                    ) {
                        client.inGameHud.setTitle(Text.literal(config.titleMessage).withColor(config.titleColor));
                        hasAttacked.set(true);
                    }

                } else if(hasLooked.get()) {
                    client.inGameHud.clearTitle();
                    client.inGameHud.setOverlayMessage(Text.empty(), false);
                    hasAttacked.set(false);
                    hasLooked.set(false);
                }
            }
        });
    }
}
