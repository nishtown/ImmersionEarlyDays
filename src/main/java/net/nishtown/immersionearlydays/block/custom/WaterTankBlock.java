package net.nishtown.immersionearlydays.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.block.entity.WaterTankBlockEntity;
import org.jetbrains.annotations.Nullable;

public class WaterTankBlock extends BaseEntityBlock {

    private static final int CAPACITY = 10000; // Water capacity in millibuckets (10 buckets)
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);



    public WaterTankBlock(Properties properties) {
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            ItemStack heldItem = player.getItemInHand(hand);
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof WaterTankBlockEntity tankEntity) {
                IFluidHandler tank = tankEntity.getFluidHandler();

                // Determine the fluid amount before interaction
                int fluidBefore = tank.getFluidInTank(0).getAmount();

                // Handle filled water bottle interaction (adding water to the tank)
                if (heldItem.getItem() == Items.POTION && PotionUtils.getPotion(heldItem) == Potions.WATER) {
                    if (tank.fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.SIMULATE) > 0) {
                        // Add water from the bottle to the tank
                        tank.fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);

                        // Replace the water bottle with an empty glass bottle
                        ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                        if (!player.getInventory().add(emptyBottle)) {
                            player.drop(emptyBottle, false);
                        }
                        heldItem.shrink(1); // Consume the water bottle

                        // Update block state and display a message
                        tankEntity.setChanged();
                        level.setBlockAndUpdate(pos, state);
                        player.displayClientMessage(
                                Component.translatable("block.immersionearlydays.water_tank.info", tank.getFluidInTank(0).getAmount(), tank.getTankCapacity(0)),
                                true
                        );
                        return InteractionResult.SUCCESS;
                    } else {
                        player.displayClientMessage(Component.literal("Tank is full!"), true);
                        return InteractionResult.FAIL;
                    }
                }

                // Handle glass bottle interaction
                if (heldItem.getItem() == Items.GLASS_BOTTLE) {
                    if (fluidBefore >= 250) { // Check if the tank has enough water
                        // Fill the bottle and replace the empty one
                        tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                        ItemStack waterBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);

                        if (!player.getInventory().add(waterBottle)) {
                            player.drop(waterBottle, false);
                        }
                        heldItem.shrink(1); // Consume the empty bottle

                        // Update block state and display a message
                        tankEntity.setChanged();
                        level.setBlockAndUpdate(pos, state);
                        player.displayClientMessage(
                                Component.translatable("block.immersionearlydays.water_tank.info", tank.getFluidInTank(0).getAmount(), tank.getTankCapacity(0)),
                                true
                        );
                        return InteractionResult.SUCCESS;
                    } else {
                        // Not enough water to fill a bottle
                        player.displayClientMessage(Component.literal("Not enough water to fill a bottle!"), true);
                        return InteractionResult.FAIL;
                    }
                }

                // Handle bucket and other fluid interactions
                boolean interactionSuccessful = FluidUtil.interactWithFluidHandler(player, hand, tank);
                if (interactionSuccessful) {
                    tankEntity.setChanged();
                    level.setBlockAndUpdate(pos, state);

                    // Display the updated tank state
                    player.displayClientMessage(
                            Component.translatable("block.immersionearlydays.water_tank.info", tank.getFluidInTank(0).getAmount(), tank.getTankCapacity(0)),
                            true
                    );
                    return InteractionResult.SUCCESS;
                } else {
                    // If no interaction occurred, display the current tank state
                    player.displayClientMessage(
                            Component.translatable("block.immersionearlydays.water_tank.info", tank.getFluidInTank(0).getAmount(), tank.getTankCapacity(0)),
                            true
                    );
                    return InteractionResult.CONSUME;
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }




    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.WATER_TANK_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1, pBlockEntity));
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterTankBlockEntity(pos, state, CAPACITY);
    }



}
