package net.nishtown.immersionearlydays.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.block.custom.BambooTapBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BambooTapBlockEntity extends BlockEntity {

    private final FluidTank FLUID_TANK = createFluidTank();
    private static final int FILL_INTERVAL_TICKS = 2;

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    private int cycleCounter = 0;


    private FluidTank createFluidTank() {
        return  new FluidTank(1000) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid() == Fluids.WATER;
            }
        };
    }


    public BambooTapBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BAMBOO_TAP_BE.get(), pPos, pBlockState);

    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return lazyFluidHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag = FLUID_TANK.writeToNBT(pTag);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        FLUID_TANK.readFromNBT(pTag);
    }

    public IFluidHandler getFluidHandler(){
        return  FLUID_TANK;
    };

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, BambooTapBlockEntity pBlockEntity) {
        if (pLevel.isClientSide()) return;

        // Increment cycle counter on every tick
        pBlockEntity.cycleCounter++;

        // Only fill the tank after FILL_INTERVAL_TICKS have passed
        if (pBlockEntity.cycleCounter >= FILL_INTERVAL_TICKS) {
            pBlockEntity.cycleCounter = 0; // Reset the counter

            // Add water incrementally
            int amount = 1; // Amount of water to add per interval
            int filled = pBlockEntity.FLUID_TANK.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);

            // Update block state based on tank contents
            if (pBlockEntity.FLUID_TANK.getFluidAmount() == 0) {
                pLevel.setBlock(pPos, pState.setValue(BambooTapBlock.STATE, BambooTapBlock.TapState.EMPTY), 3);
            } else if (pBlockEntity.FLUID_TANK.getFluidAmount() < pBlockEntity.FLUID_TANK.getCapacity()) {
                pLevel.setBlock(pPos, pState.setValue(BambooTapBlock.STATE, BambooTapBlock.TapState.RUNNING), 3);
            } else {
                pLevel.setBlock(pPos, pState.setValue(BambooTapBlock.STATE, BambooTapBlock.TapState.FULL), 3);
            }
        }
    }

    public void updateStateBasedOnTank() {
        if (level == null) return;

        BlockState currentState = level.getBlockState(worldPosition);
        int fluidAmount = FLUID_TANK.getFluidAmount();

        if (fluidAmount == 0) {
            level.setBlock(worldPosition, currentState.setValue(BambooTapBlock.STATE, BambooTapBlock.TapState.EMPTY), 3);
        } else if (fluidAmount < FLUID_TANK.getCapacity()) {
            level.setBlock(worldPosition, currentState.setValue(BambooTapBlock.STATE, BambooTapBlock.TapState.RUNNING), 3);
        } else {
            level.setBlock(worldPosition, currentState.setValue(BambooTapBlock.STATE, BambooTapBlock.TapState.FULL), 3);
        }

        setChanged();
    }

    public void empty()
    {
        FLUID_TANK.drain(FLUID_TANK.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
    }

}


