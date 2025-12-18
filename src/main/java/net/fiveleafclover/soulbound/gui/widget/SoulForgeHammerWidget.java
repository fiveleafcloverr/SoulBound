package net.fiveleafclover.soulbound.gui.widget;

import net.fiveleafclover.soulbound.SoulBound;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SoulForgeHammerWidget extends ButtonWidget {
    private final Identifier TEXTURE = new Identifier(SoulBound.MOD_ID, "textures/block/soul_forge.png");

    public SoulForgeHammerWidget(int x, int y, PressAction action) {
        super(x, y, 16, 16, ScreenTexts.EMPTY, action, DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(TEXTURE, this.getX(), this.getY(), 0, 0, 16, 16);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.BLOCK_ANVIL_PLACE, (float) Math.random() / 10 + 0.95f));
    }
}
