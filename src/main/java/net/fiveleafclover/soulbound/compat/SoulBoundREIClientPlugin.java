package net.fiveleafclover.soulbound.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fiveleafclover.soulbound.SoulBound;
import net.fiveleafclover.soulbound.block.ModBlocks;
import net.fiveleafclover.soulbound.gui.screen.SoulForgeScreen;
import net.fiveleafclover.soulbound.recipe.SoulForgeRecipe;

public class SoulBoundREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new SoulForgeCategory());

        registry.addWorkstations(SoulForgeCategory.SOUL_FORGE, EntryStacks.of(ModBlocks.SOUL_FORGE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(SoulForgeRecipe.class, SoulForgeRecipe.Type.INSTANCE,
                SoulForgeDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(75, 30, 20, 30), SoulForgeScreen.class,
                SoulForgeCategory.SOUL_FORGE);
    }

}
