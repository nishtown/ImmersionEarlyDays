package net.nishtown.immersionearlydays.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class DampGravelBlock extends Block {
    public DampGravelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true; // Enable random ticking for this block
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.isAreaLoaded(pPos, 1)) {
            // Drying chance (e.g., 65%)
            if (pRandom.nextFloat() < 0.65f) {
                // Replace with regular Gravel
                pLevel.setBlock(pPos, Blocks.GRAVEL.defaultBlockState(), 3); // Update the block state
            }
        }
    }

}
