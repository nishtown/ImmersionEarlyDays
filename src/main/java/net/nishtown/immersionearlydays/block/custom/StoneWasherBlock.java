package net.nishtown.immersionearlydays.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.block.entity.StoneWasherBlockEntity;
import org.jetbrains.annotations.Nullable;

public class StoneWasherBlock extends BaseEntityBlock {

    //public static final VoxelShape SHAPE = Block.box(0,0,0,16,12,16);
    public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 14, 15);
    public static final BooleanProperty WASHING = BooleanProperty.create("washing");
    public StoneWasherBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((((BlockState)this.stateDefinition.any()).setValue(WASHING, false)));
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
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof StoneWasherBlockEntity) {
                ((StoneWasherBlockEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof StoneWasherBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer) pPlayer), (StoneWasherBlockEntity) entity, pPos);
            } else {
                throw new IllegalStateException("Our container provider is missing");
            }
        }


        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new StoneWasherBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.STONE_WASHER_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1, pBlockEntity));
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {

        super.animateTick(pState, pLevel, pPos, pRandom);

        if ((Boolean)pState.getValue(WASHING)) {
            double $$4 = (double)pPos.getX() + (double)0.5F;
            double $$5 = (double)pPos.getY();
            double $$6 = (double)pPos.getZ() + (double)0.5F;
            if (pRandom.nextDouble() < 0.02) {
                pLevel.playLocalSound($$4, $$5, $$6, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.3F, 3F, false);
            }

            if (pRandom.nextFloat() < 0.80f) { // 80% chance
                for (int i = 0; i < 8; i++) {
                    double x = pPos.getX() + pRandom.nextDouble();
                    double y = pPos.getY() + 0.8;
                    double z = pPos.getZ() + pRandom.nextDouble();

                    // Reduce the vertical velocity to limit the height
                    double velocityY = 0.05; // Lower value for less height

                    pLevel.addParticle(ParticleTypes.SPLASH, x, y, z, 0.0, velocityY, 0.0);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(WASHING);
    }

}
