package net.nishtown.immersionearlydays.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.nishtown.immersionearlydays.block.entity.BambooTapBlockEntity;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class BambooTapBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(0,0,0,16,12,16);
    public static final EnumProperty<TapState> STATE = EnumProperty.create("state", TapState.class);

    public BambooTapBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((((BlockState)this.stateDefinition.any()).setValue(STATE, TapState.EMPTY)));
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
            if(blockEntity instanceof BambooTapBlockEntity) {
                ((BambooTapBlockEntity) blockEntity).empty();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BambooTapBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntities.BAMBOO_TAP_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1, pBlockEntity));
    }



    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.animateTick(pState, pLevel, pPos, pRandom);

        // Check if the block is in the RUNNING state
        if (pState.getValue(STATE) == TapState.RUNNING) {
            // Position the particle in the middle of the block
            double x = pPos.getX() + 0.5; // Center of the block
            double y = pPos.getY() + 0.8; // Slightly below the block's top
            double z = pPos.getZ() + 0.6; // Center of the block

            // Add the water drip particle
            pLevel.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0.0, -0.2, 0.0);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {

        super.tick(pState, pLevel, pPos, pRandom);

        if (pState.getValue(STATE) == TapState.EMPTY) {
            pLevel.setBlock(pPos, pState.setValue(STATE, TapState.RUNNING), 3);
        } else if (pState.getValue(STATE) == TapState.RUNNING) {
            pLevel.setBlock(pPos, pState.setValue(STATE, TapState.FULL), 3);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            ItemStack heldItem = player.getItemInHand(hand);
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof BambooTapBlockEntity tapEntity) {
                boolean showMessage = true;

                IFluidHandler tank = tapEntity.getFluidHandler();

                // Determine how much fluid would be taken
                int fluidBefore = tank.getFluidInTank(0).getAmount(); // Fluid amount before interaction

                boolean success = FluidUtil.interactWithFluidHandler(player, hand, tank);
                if (success){
                    int fluidRemoved = fluidBefore - tank.getFluidInTank(0).getAmount();
                    tapEntity.updateStateBasedOnTank();

                    String itemName = heldItem.getHoverName().getString();
                    player.displayClientMessage(Component.literal("Refilled " + itemName + " with " + fluidRemoved + "mB"), true);

                    showMessage = false;
                }


                // Handle glass bottle interaction
                if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                    if (tank.getFluidInTank(0).getAmount() >= 250) {
                        String itemName = heldItem.getHoverName().getString();
                        // Fill the bottle and replace the empty one
                        tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                        ItemStack waterBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                        if (!player.getInventory().add(waterBottle)) {
                            player.drop(waterBottle, false);
                        }
                        heldItem.shrink(1); // Consume the empty bottle
                        showMessage = false;

                        player.displayClientMessage(Component.literal("Refilled " + itemName + " with 250mB"), true);
                        // Update block state
                        tapEntity.updateStateBasedOnTank();
                        return InteractionResult.CONSUME;
                    } else {
                        player.displayClientMessage(Component.literal("Not enough water to fill a bottle!"), true);
                        showMessage = false;
                        return InteractionResult.FAIL;
                    }
                }

                if(showMessage) {
                    // Show fluid capacity if no fluid interaction occurred
                    int currentFluid = tank.getFluidInTank(0).getAmount();
                    //player.displayClientMessage(Component.literal(currentFluid + "mB"), true);
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(STATE);

    }

    public enum TapState implements StringRepresentable {
        EMPTY("empty"),
        RUNNING("running"),
        FULL("full");

        private final String name;

        TapState(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

}
