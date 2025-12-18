package net.fiveleafclover.soulbound;

import net.fabricmc.api.ModInitializer;

import net.fiveleafclover.soulbound.block.ModBlocks;
import net.fiveleafclover.soulbound.block.entity.ModBlockEntities;
import net.fiveleafclover.soulbound.item.ModItemGroups;
import net.fiveleafclover.soulbound.item.ModItems;
import net.fiveleafclover.soulbound.gui.screen.ModScreenHandlers;
import net.fiveleafclover.soulbound.recipe.ModRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoulBound implements ModInitializer {
	public static final String MOD_ID = "soulbound";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("soulbound says :3");

		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();

		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();

		ModScreenHandlers.registerScreenHandlers();

		ModRecipes.registerRecipes();
	}
}