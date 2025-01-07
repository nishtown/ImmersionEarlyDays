package net.nishtown.immersionearlydays.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.block.entity.SprinklerBlockEntity;
import net.nishtown.immersionearlydays.block.entity.WaterTankBlockEntity;
import org.jetbrains.annotations.Nullable;

public class SprinklerBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Shapes.or(
            // VerticalFrame
            Block.box(7, 0, 7, 9, 5, 9),

            // TopFrame
            Block.box(6, 4, 6, 10, 7, 10),

            // N-Nozzle
            Block.box(7.5, 5.25, 3, 8.5, 6.25, 6),

            // N-Spout
            Block.box(7, 4.75, 2, 9, 6.75, 3),

            // E-Nozzle
            Block.box(10, 5.25, 7.5, 13, 6.25, 8.5),

            // E-Spout
            Block.box(13, 4.75, 7, 14, 6.75, 9),

            // S-Nozzle
            Block.box(7.5, 5.25, 10, 8.5, 6.25, 13),

            // S-Spout
            Block.box(7, 4.75, 13, 9, 6.75, 14),

            // W-Nozzle
            Block.box(3, 5.25, 7.5, 6, 6.25, 8.5),

            // W-Spout
            Block.box(2, 4.75, 7, 3, 6.75, 9)
    );


    public SprinklerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pStage, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext){
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide()) { // Server-side only
            return createTickerHelper(blockEntityType, ModBlockEntities.SPRINKLER_BE.get(),
                    (lvl, pos, st, blockEntity) -> blockEntity.sprinkle(lvl, pos, st, (SprinklerBlockEntity) blockEntity));
        }
        return null; // No client-side ticking
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SprinklerBlockEntity(pos, state);
    }



}
