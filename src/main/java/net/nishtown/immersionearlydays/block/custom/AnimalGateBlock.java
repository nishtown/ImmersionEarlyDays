package net.nishtown.immersionearlydays.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.block.entity.AnimalGateBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.xml.transform.Source;
import java.util.Random;

public class AnimalGateBlock extends FenceGateBlock implements EntityBlock {

    public AnimalGateBlock(Properties properties) {
        super(properties, SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Create and return the block entity for this block
        return new AnimalGateBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Provide the ticker for the block entity
        return type == ModBlockEntities.ANIMAL_GATE_BE.get()
                ? (lvl, pos, st, entity) -> AnimalGateBlockEntity.tick(lvl, pos, st, (AnimalGateBlockEntity) entity)
                : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL; // Ensures rendering like a normal fence gate
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true; // Enable random ticks for this block
    }


    public void toggleGate(Level pLevel, BlockPos pPos, @NotNull BlockState pState) {
        if (pState.getValue(OPEN)) {
            pState = pState.setValue(OPEN, Boolean.valueOf(false));
            pLevel.setBlock(pPos, pState, 10);
        } else {
            pState = pState.setValue(OPEN, Boolean.valueOf(true));
            pLevel.setBlock(pPos, pState, 10);
        }

        boolean flag = pState.getValue(OPEN);
        pLevel.playSound((Player)null, pPos, flag ? SoundEvents.FENCE_GATE_OPEN: SoundEvents.FENCE_GATE_OPEN, SoundSource.BLOCKS, 1.0F, pLevel.getRandom().nextFloat() * 0.1F + 0.9F);
        pLevel.gameEvent((Entity)null, flag ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);
    }
}
