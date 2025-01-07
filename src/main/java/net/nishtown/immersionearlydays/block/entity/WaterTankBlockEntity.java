package net.nishtown.immersionearlydays.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.block.custom.SprinklerBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WaterTankBlockEntity extends BlockEntity {

    private final FluidTank tank = new FluidTank(10000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            notifyClient(); // Notify client of changes
        }
    };

    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    public WaterTankBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WATER_TANK_BE.get(), pos, state);
    }

    public WaterTankBlockEntity(BlockPos pos, BlockState state, int capacity) {
        super(ModBlockEntities.WATER_TANK_BE.get(), pos, state);
        tank.setCapacity(capacity);
    }

    public IFluidHandler getFluidHandler(){
        return tank;
    };

    public float getFluidLevelPercentage() {
        int capacity = tank.getTankCapacity(0);
        if (capacity == 0) {
            return 0.0f;
        }
        return (float) tank.getFluidAmount() / capacity;
    }

    public int getFluidLevel() {
        return tank.getFluidAmount();
    }

    public int fillTank(FluidStack fluid, IFluidHandler.FluidAction action) {
        return tank.fill(fluid, action);
    }

    public FluidStack drainTank(int amount, IFluidHandler.FluidAction action) {
        return tank.drain(amount, action);
    }

    private void notifyClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    // Tick logic to push/pull fluid automatically
    public static void tick(Level level, BlockPos pos, BlockState state, WaterTankBlockEntity blockEntity) {
        if (!level.isClientSide) {
            for (Direction direction : Direction.values()) {
                BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
                if (neighbor != null) {
                    neighbor.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).ifPresent(neighborHandler -> {
                        // Push fluid
                        FluidStack extracted = blockEntity.tank.drain(100, IFluidHandler.FluidAction.SIMULATE);
                        int filled = neighborHandler.fill(extracted, IFluidHandler.FluidAction.EXECUTE);
                        blockEntity.tank.drain(filled, IFluidHandler.FluidAction.EXECUTE);

                        // Pull fluid
                        FluidStack inserted = neighborHandler.drain(100, IFluidHandler.FluidAction.SIMULATE);
                        int received = blockEntity.tank.fill(inserted, IFluidHandler.FluidAction.EXECUTE);
                        neighborHandler.drain(received, IFluidHandler.FluidAction.EXECUTE);
                    });
                }
            }
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidHandler.invalidate();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        fluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Tank", tank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        tank.readFromNBT(tag.getCompound("Tank"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("Tank", tank.writeToNBT(new CompoundTag()));
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        tank.readFromNBT(tag.getCompound("Tank"));
    }
}
