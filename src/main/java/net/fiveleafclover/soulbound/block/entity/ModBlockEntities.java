package net.fiveleafclover.soulbound.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fiveleafclover.soulbound.SoulBound;
import net.fiveleafclover.soulbound.block.ModBlocks;
import net.fiveleafclover.soulbound.block.custom.SoulForgeBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<SoulForgeBlockEntity> SOUL_FORGE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(SoulBound.MOD_ID, "soul_forge_be"),
                    FabricBlockEntityTypeBuilder.create(SoulForgeBlockEntity::new,
                            ModBlocks.SOUL_FORGE).build());

    public static void registerBlockEntities() {
        SoulBound.LOGGER.info(SoulBound.MOD_ID + " is registering block entities :3");
    }

}
