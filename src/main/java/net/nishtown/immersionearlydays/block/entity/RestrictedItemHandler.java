package net.nishtown.immersionearlydays.block.entity;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.nishtown.immersionearlydays.item.ModItems;
import org.jetbrains.annotations.NotNull;

public class RestrictedItemHandler implements IItemHandlerModifiable {
    private final ItemStackHandler handler;

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int OUTPUT_SLOT2 = 2;


    public RestrictedItemHandler(ItemStackHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        // Allow modification only if the slot is valid
        if (slot >= 0 && slot < handler.getSlots()) {
            handler.setStackInSlot(slot, stack);
        }
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        // Restrict insertion to the input slot
        if (slot == INPUT_SLOT) {
            // Check output slots for matching items
            if (stack.getItem() == ModItems.COPPER_NODULE.get() || stack.getItem() == ModItems.IRON_NODULE.get())
            {
                return handler.insertItem(OUTPUT_SLOT2, stack, simulate);
            }
            else {
                return handler.insertItem(slot, stack, simulate);
            }
        }
        return stack; // Deny insertion for output slots
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (simulate) {        // Allow extraction from output slots
            if (slot == OUTPUT_SLOT || slot == OUTPUT_SLOT2) {
                return handler.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY; // Default for invalid slots
        }
        else
        {
            return handler.extractItem(slot, amount, simulate);
        }
    }
    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }


    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        // Restrict valid items to the input slot
        return slot == INPUT_SLOT && handler.isItemValid(slot, stack);
    }

    public IItemHandler getUnderlyingHandler() {
        return  handler;
    }
}