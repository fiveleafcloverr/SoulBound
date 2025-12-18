package net.fiveleafclover.soulbound.gui.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fiveleafclover.soulbound.SoulBound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<SoulForgeScreenHandler> SOUL_FORGE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(SoulBound.MOD_ID, "soul_forging"),
                    new ExtendedScreenHandlerType<>(SoulForgeScreenHandler::new));

    public static void registerScreenHandlers() {
        SoulBound.LOGGER.info(SoulBound.MOD_ID + " is registering screen handlers :3");
    }
}
