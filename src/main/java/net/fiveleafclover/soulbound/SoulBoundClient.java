package net.fiveleafclover.soulbound;

import net.fabricmc.api.ClientModInitializer;
import net.fiveleafclover.soulbound.block.entity.ModBlockEntities;
import net.fiveleafclover.soulbound.block.entity.renderer.SoulForgeBlockEntityRenderer;
import net.fiveleafclover.soulbound.gui.screen.ModScreenHandlers;
import net.fiveleafclover.soulbound.gui.screen.SoulForgeScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class SoulBoundClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		HandledScreens.register(ModScreenHandlers.SOUL_FORGE_SCREEN_HANDLER, SoulForgeScreen::new);

		BlockEntityRendererFactories.register(ModBlockEntities.SOUL_FORGE_BLOCK_ENTITY, SoulForgeBlockEntityRenderer::new);

	}

}