package net.nishtown.immersionearlydays.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.block.custom.StoneWasherBlock;
import net.nishtown.immersionearlydays.recipe.RecipeOutput;
import net.nishtown.immersionearlydays.recipe.StoneWashingRecipe;
import net.nishtown.immersionearlydays.screen.StoneWasherMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StoneWasherBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3);
    private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new SidedItemHandler(itemHandler, Direction.UP));
    private final LazyOptional<IItemHandler> outputHandler = LazyOptional.of(() -> new SidedItemHandler(itemHandler, Direction.DOWN));



    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int OUTPUT_SLOT2 = 2;


    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public StoneWasherBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.STONE_WASHER_BE.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> StoneWasherBlockEntity.this.progress;
                    case 1 -> StoneWasherBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> StoneWasherBlockEntity.this.progress = pValue;
                    case 1 -> StoneWasherBlockEntity.this.maxProgress = pValue;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.of(() -> new RestrictedItemHandler(itemHandler)).cast();
        }
        return super.getCapability(cap, side);
    }



    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        inputHandler.invalidate();
        outputHandler.invalidate();
    }


    public void drops(){
        SimpleContainer inventory = new SimpleContainer((itemHandler.getSlots()));

        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private boolean isWashing() {
        return this.progress > 0;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.immersionearlydays.stone_washer_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContaineId, Inventory pPlayerInventory, Player pPlayer) {
        return new StoneWasherMenu(pContaineId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("stone_washing.progress", progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("stone_washing.progress");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState, StoneWasherBlockEntity pBlockEntity) {
        if(hasRecipe()) {
            if (pBlockEntity.isWashing()){
                pState = (BlockState)pState.setValue(StoneWasherBlock.WASHING, pBlockEntity.isWashing());
                pLevel.setBlock(pPos, pState, 3);
            }
            increaseCraftingProgress();
            setChanged(pLevel,pPos,pState);
            if(hasProgressFinished()){
                craftItem();
                resetProgress();
                pState = (BlockState)pState.setValue(StoneWasherBlock.WASHING, false);
                pLevel.setBlock(pPos, pState, 3);
            }
        } else {
            resetProgress();
        }

    }

    private void craftItem() {

        Optional<StoneWashingRecipe> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) return;

        NonNullList<RecipeOutput> results = recipe.get().getResultItems(null);

        // Extract input item
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        // Primary output (OUTPUT_SLOT)
        ItemStack primaryResult = results.get(0).rollOutput();
        ItemStack primaryStack = this.itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (!primaryStack.isEmpty() && primaryStack.getItem() == primaryResult.getItem()) {
            this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(primaryResult.getItem(),
                    this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + primaryResult.getCount()));
        } else if (primaryStack.isEmpty()){
            this.itemHandler.setStackInSlot(OUTPUT_SLOT, primaryResult.copy());
        }

        if (results.stream().count() > 1) {
            // Secondary output (OUTPUT_SLOT2)
            ItemStack secondaryResult = results.get(1).rollOutput();
            ItemStack existingStack = this.itemHandler.getStackInSlot(OUTPUT_SLOT2);

            if (!existingStack.isEmpty() && existingStack.getItem() == secondaryResult.getItem()) {
                // If the existing stack matches the secondary result, increase the count
                this.itemHandler.setStackInSlot(OUTPUT_SLOT2, new ItemStack(secondaryResult.getItem(),
                        existingStack.getCount() + secondaryResult.getCount()));
            } else if (existingStack.isEmpty()) {
                // If the slot is empty, set the secondary result
                this.itemHandler.setStackInSlot(OUTPUT_SLOT2, secondaryResult.copy());
            } else {
                // Handle cases where the existing item in OUTPUT_SLOT2 does not match
                dropItemToWorld(secondaryResult);
            }
        }

    }

    private void dropItemToWorld(ItemStack stack) {
        if (stack.isEmpty() || this.level == null || this.level.isClientSide) return;

        Containers.dropItemStack(this.level, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, stack);
    }

    private boolean hasRecipe() {
        Optional<StoneWashingRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());

        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertIntoOutputSlot(result.getItem());
    }

    private Optional<StoneWashingRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++)
        {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(StoneWashingRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void resetProgress() {
        progress = 0;
    }


    public class SidedItemHandler implements IItemHandler {
        private final ItemStackHandler handler;
        private final Direction side;

        public SidedItemHandler(ItemStackHandler handler, Direction side) {
            this.handler = handler;
            this.side = side;
        }

        @Override
        public int getSlots() {
            return handler.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            if (slot >= 0 && slot < handler.getSlots() && isSlotAccessible(slot)) {
                return handler.getStackInSlot(slot);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return handler.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == OUTPUT_SLOT) {
                    ItemStack stack = handler.extractItem(OUTPUT_SLOT, amount, simulate);
                    if (!stack.isEmpty()) {
                        return stack;
                    }
            }
                // Fallback to OUTPUT_SLOT2
            if (slot == OUTPUT_SLOT2) {
                return handler.extractItem(OUTPUT_SLOT2, amount, simulate);
            }

            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return handler.isItemValid(slot, stack);
        }

        private boolean isSlotAccessible(int slot) {
            if (side == Direction.UP && slot == INPUT_SLOT) {
                return true;
            }
            if (side == Direction.DOWN && (slot == OUTPUT_SLOT || slot == OUTPUT_SLOT2)) {
                return true;
            }
            return false;
        }
    }


}
