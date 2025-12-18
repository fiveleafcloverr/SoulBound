package net.fiveleafclover.soulbound.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fiveleafclover.soulbound.block.custom.SoulForgeBlock;
import net.fiveleafclover.soulbound.gui.screen.SoulForgeScreenHandler;
import net.fiveleafclover.soulbound.item.ModItems;
import net.fiveleafclover.soulbound.recipe.SoulForgeRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SoulForgeBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);

    private static final int INPUT_SLOT1 = 0;
    private static final int INPUT_SLOT2 = 1;
    private static final int INPUT_SLOT3 = 2;
    private static final int FUEL_SLOT = 3;
    private static final int OUTPUT_SLOT = 4;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 1200;


    public SoulForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOUL_FORGE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 -> SoulForgeBlockEntity.this.progress;
                    case 1 -> SoulForgeBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index) {
                    case 0 -> SoulForgeBlockEntity.this.progress = value;
                    case 1 -> SoulForgeBlockEntity.this.maxProgress = value;
                };
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public ItemStack getRenderStack() {
        return this.getStack(OUTPUT_SLOT);
    }

    @Override
    public void markDirty() {
        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        super.markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("soul_forge.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("soul_forge.progress");
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.soulbound.soul_forge");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new SoulForgeScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }

        boolean heating = world.getBlockState(pos).get(SoulForgeBlock.HEATING);
        boolean hammering = world.getBlockState(pos).get(SoulForgeBlock.HAMMERING);

        if (isOutputSlotEmptyOrReceivable()) {
            if (this.hasRecipe() && this.getStack(FUEL_SLOT).isOf(Items.LAVA_BUCKET)) {
                this.increaseCraftProgress();
                this.setStack(FUEL_SLOT, Items.BUCKET.getDefaultStack());
                this.setMaxProgress();
                heating = true;
                markDirty(world, pos, state);
            }
            if (this.progress > 0 && this.hasRecipe()) {
                this.increaseCraftProgress();
                heating = true;
                markDirty(world, pos, state);

                if (hasCraftingFinished()) {
                    hammering = true;
                    heating = false;
                    this.craftItem();
                    this.resetProgress();
                }
            } else {
                heating = false;
                this.resetProgress();
            }
            hammering = false;
        } else {
            hammering = true;
            this.resetProgress();
            markDirty(world, pos, state);
        }

        world.setBlockState(pos, state.with(SoulForgeBlock.HEATING, heating).with(SoulForgeBlock.HAMMERING, hammering));
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void setMaxProgress() {
        Optional<SoulForgeRecipe> recipe = getCurrentRecipe();

        this.maxProgress = recipe.get().heatTime;
    }

    private void craftItem() {
        Optional<SoulForgeRecipe> recipe = getCurrentRecipe();

        this.removeStack(INPUT_SLOT1, 1);
        this.removeStack(INPUT_SLOT2, 1);
        this.removeStack(INPUT_SLOT3, 1);

        ItemStack output = new ItemStack(recipe.get().getOutput(null).getItem(),
                getStack(OUTPUT_SLOT).getCount() + recipe.get().getOutput(null).getCount());
        NbtCompound nbt = output.getNbt();
        if (nbt == null) {
            nbt = createNbt();
        }
        nbt.putInt("soulbound.hammer_hits", recipe.get().hammerHits);

        output.setNbt(nbt);
        this.setStack(OUTPUT_SLOT, output);
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    public void increaseCraftProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        Optional<SoulForgeRecipe> recipe = getCurrentRecipe();

        return recipe.isPresent() &&
                canInsertAmountIntoOutputSlot(recipe.get().getOutput(null)) &&
                canInsertItemIntoOutputSlot(recipe.get().getOutput(null).getItem());
    }

    private Optional<SoulForgeRecipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());

        for (int i = 0; i < this.size(); i++) {
            inv.setStack(i, this.getStack(i));
        }

        return getWorld().getRecipeManager().getFirstMatch(SoulForgeRecipe.Type.INSTANCE, inv, getWorld());

    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.getStack(OUTPUT_SLOT).getItem() == item || this.getStack(OUTPUT_SLOT).isEmpty();
    }

    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
        return this.getStack(OUTPUT_SLOT).getCount() + result.getCount() <= getStack(OUTPUT_SLOT).getMaxCount();
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() || this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount();
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void hammerHit() {

        ItemStack stack = this.getStack(OUTPUT_SLOT);
        NbtCompound nbt = stack.getNbt();

        int hits = nbt.getInt("soulbound.hammer_hits");
        hits = Math.max(hits - 1, 0);
        nbt.putInt("soulbound.hammer_hits", hits);

        stack.setNbt(nbt);

    }
}
