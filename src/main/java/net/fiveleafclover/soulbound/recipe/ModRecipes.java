package net.fiveleafclover.soulbound.recipe;

import net.fiveleafclover.soulbound.SoulBound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

    public static void registerRecipes() {
        SoulBound.LOGGER.info(SoulBound.MOD_ID + " is registering recipes :3");

        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(SoulBound.MOD_ID, SoulForgeRecipe.Serializer.ID),
                SoulForgeRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, new Identifier(SoulBound.MOD_ID, SoulForgeRecipe.Type.ID),
                SoulForgeRecipe.Type.INSTANCE);
    }
}
