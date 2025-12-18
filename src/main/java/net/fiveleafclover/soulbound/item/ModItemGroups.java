package net.fiveleafclover.soulbound.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fiveleafclover.soulbound.SoulBound;
import net.fiveleafclover.soulbound.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup SOULBOUND_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(SoulBound.MOD_ID, "soulbound"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.soulbound"))
                    .icon(() -> new ItemStack(ModItems.BLAZESTEEL_KHOPESH)).entries((displayContext, entries) -> {

                        entries.add(ModItems.BLAZESTEEL_KHOPESH);
                        entries.add(ModItems.SOUL);

                        entries.add(ModBlocks.SOUL_FORGE);

                    }).build());


    public static void registerItemGroups() {
        SoulBound.LOGGER.info(SoulBound.MOD_ID + " is registering item groups :3");
    }
}
