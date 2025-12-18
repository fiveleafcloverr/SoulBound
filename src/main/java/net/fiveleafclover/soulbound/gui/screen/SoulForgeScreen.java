package net.fiveleafclover.soulbound.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fiveleafclover.soulbound.SoulBound;
import net.fiveleafclover.soulbound.gui.widget.SoulForgeHammerWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SoulForgeScreen extends HandledScreen<SoulForgeScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(SoulBound.MOD_ID, "textures/gui/soul_forge_gui.png");
    private SoulForgeHammerWidget hammerButton;

    public SoulForgeScreen(SoulForgeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.hammerButton = new SoulForgeHammerWidget(64, 64, button -> {
            this.client.interactionManager.clickButton(this.handler.syncId, 0);
        });
        this.addDrawableChild(hammerButton);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrow(context, x, y);
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        if (handler.isCrafting()) {
            context.drawTexture(TEXTURE, x + 85, y + 30, 176, 0, 8, handler.getScaledProgress());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
