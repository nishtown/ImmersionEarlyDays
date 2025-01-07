package net.nishtown.immersionearlydays.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SprinklerBlockEntity extends BlockEntity {
    private static final int RADIUS = 4; // 5 blocks radius = 11x11 area
    private static final int WATER_CONSUMPTION = 5; // Water consumed per tick (in mB)

    public SprinklerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPRINKLER_BE.get(), pos, state);
    }

    public void sprinkle(Level level, BlockPos pos, BlockState state, SprinklerBlockEntity blockEntity) {
        if (level.getGameTime() % 100 != 0) return; // Run every 5 seconds.

        BlockEntity belowEntity = level.getBlockEntity(pos.below());
        if (belowEntity == null) {
            generateSmokeParticle(level, pos);
            return;
        }

        belowEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(handler -> {
            boolean hasWater = handler.drain(WATER_CONSUMPTION, IFluidHandler.FluidAction.SIMULATE).getAmount() > 0;
            if (!hasWater) {
                generateSmokeParticle(level, pos);
                return;
            }

            List<BlockPos> validCropPositions = new ArrayList<>();
            boolean hydratedFarmland = false;

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            for (int x = -RADIUS; x <= RADIUS; x++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    mutablePos.set(pos.getX() + x, pos.getY() - 1, pos.getZ() + z);

                    BlockState targetState = level.getBlockState(mutablePos);

                    // Convert dirt to farmland
                    if (targetState.is(Blocks.DIRT)) {
                        level.setBlock(mutablePos, Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7), 3);
                        generateSplashParticle(level, mutablePos);
                        hydratedFarmland = true;
                        continue;
                    }


                    // Hydrate farmland
                    if (targetState.is(Blocks.FARMLAND)) {
                        int currentMoisture = targetState.getValue(FarmBlock.MOISTURE);
                        if (currentMoisture < 7) {
                            level.setBlock(mutablePos, targetState.setValue(FarmBlock.MOISTURE, 7), 3);
                            generateSplashParticle(level, mutablePos);
                            hydratedFarmland = true;
                        }

                        // Check for crops above farmland
                        BlockPos cropPos = mutablePos.above();
                        BlockState cropState = level.getBlockState(cropPos);
                        if (cropState.getBlock() instanceof CropBlock) {
                            validCropPositions.add(cropPos.immutable());
                        }
                    }
                }
            }

            // Consume water for farmland hydration
            if (hydratedFarmland) {
                handler.drain(WATER_CONSUMPTION, IFluidHandler.FluidAction.EXECUTE);
            }

            // Randomly grow up to 5 crops
            if (!validCropPositions.isEmpty()) {
                // Use Minecraft's RandomSource to shuffle
                RandomSource randomSource = level.getRandom();
                Collections.shuffle(validCropPositions, new java.util.Random(randomSource.nextLong()));

                int cropsToGrow = Math.min(5, validCropPositions.size());
                for (int i = 0; i < cropsToGrow; i++) {
                    BlockPos cropPos = validCropPositions.get(i);
                    BlockState cropState = level.getBlockState(cropPos);

                    cropState.randomTick((ServerLevel) level, cropPos, randomSource);
                    generateSplashParticle(level, cropPos);
                    handler.drain(WATER_CONSUMPTION, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        });
    }

    private void generateSplashParticle(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            // Generate a splash particle
            serverLevel.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.SPLASH, // Particle type
                    pos.getX() + 0.5, // Center X
                    pos.getY() + 1.0, // Slightly above the block
                    pos.getZ() + 0.5, // Center Z
                    5,               // Particle count
                    0.2, 0.2, 0.2,   // Spread in X, Y, Z
                    0.1              // Particle speed
            );
        }
    }

    private void generateSmokeParticle(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            // Generate a smoke particle
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE, // Smoke particle type
                    pos.getX() + 0.5,    // Center X
                    pos.getY() + 1.5,    // Slightly above the block
                    pos.getZ() + 0.5,    // Center Z
                    5,                   // Particle count
                    0.1, 0.1, 0.1,       // Spread in X, Y, Z
                    0.01                 // Particle speed
            );
        }
    }

}
