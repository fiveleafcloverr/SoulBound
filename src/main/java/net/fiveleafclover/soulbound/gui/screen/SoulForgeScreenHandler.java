package net.fiveleafclover.soulbound.gui.screen;

import net.fiveleafclover.soulbound.SoulBound;
import net.fiveleafclover.soulbound.block.entity.SoulForgeBlockEntity;
import net.fiveleafclover.soulbound.compat.SoulForgeCategory;
import net.fiveleafclover.soulbound.gui.screen.slot.SoulForgeFuelSlot;
import net.fiveleafclover.soulbound.gui.screen.slot.SoulForgeOutputSlot;
import net.fiveleafclover.soulbound.gui.screen.slot.SoulForgeSlot;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class SoulForgeScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final SoulForgeBlockEntity blockEntity;

    public SoulForgeScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(2));
    }

    public SoulForgeScreenHandler(int syncId, PlayerInventory plrInventory, BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(ModScreenHandlers.SOUL_FORGE_SCREEN_HANDLER, syncId);
        checkSize(((Inventory) blockEntity), 5);
        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(plrInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((SoulForgeBlockEntity) blockEntity);

        this.addSlot(new SoulForgeSlot(inventory, 0, 62, 11));
        this.addSlot(new SoulForgeSlot(inventory, 1, 80, 11));
        this.addSlot(new SoulForgeSlot(inventory, 2, 98, 11));
        this.addSlot(new SoulForgeFuelSlot(inventory, 3, 30, 11));
        this.addSlot(new SoulForgeOutputSlot(inventory, 4, 80, 59));

        addPlayerInventory(plrInventory);
        addPlayerHotbar(plrInventory);

        addProperties(arrayPropertyDelegate);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 26;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invslot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invslot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invslot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(originalStack, 0, this.inventory.size() - 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }


    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }


    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        //this.propertyDelegate.set(0, this.propertyDelegate.get(0) + 1);
        this.blockEntity.hammerHit();
        return true;
    }
}
