package net.fiveleafclover.soulbound.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlazesteelKhopeshItem extends SwordItem {

    public BlazesteelKhopeshItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.soulbound.blazesteel_khopesh").formatted(Formatting.GRAY).append(Text.literal("N/A").formatted(Formatting.DARK_GRAY)));
        super.appendTooltip(stack, world, tooltip, context);
    }

}
