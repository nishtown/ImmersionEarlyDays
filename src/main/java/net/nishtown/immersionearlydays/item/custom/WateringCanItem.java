package net.nishtown.immersionearlydays.item.custom;

import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.nishtown.immersionearlydays.util.ModTags;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public class WateringCanItem extends Item {
    private final int MAX_CAPACITY; // Maximum water capacity
    private static final int USE_WATER_PER_ACTION = 1; // Water consumed per use
    private static final int PARTICLE_COUNT = 10;
    private static final int STARTUP_TIME = 10; // Number of ticks before watering starts
    private final double GROWTH_BOOST;
    private final int RANGE;


    public WateringCanItem(int capacity,int range, double growthBoost, Properties properties) {
        super(properties.stacksTo(1));
        this.MAX_CAPACITY = capacity;
        this.GROWTH_BOOST = growthBoost;
        this.RANGE = range;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidHandler(stack, MAX_CAPACITY);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                .map(handler -> handler.getFluidInTank(0).getAmount() < MAX_CAPACITY)
                .orElse(false);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM)
                .map(handler -> (int) ((float) handler.getFluidInTank(0).getAmount() / MAX_CAPACITY * 13))
                .orElse(0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x3F76E4; // Water blue color
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000; // Long duration for holding
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        return heldItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).map(fluidHandler -> {
            if (fluidHandler.getFluidInTank(0).getAmount() < this.MAX_CAPACITY) {
                BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = hitResult.getBlockPos();
                    BlockState state = level.getBlockState(pos);
                    // Handle cauldrons and other BucketPickup blocks
                    if (state.getFluidState().getType() == Fluids.WATER && state.getBlock() instanceof BucketPickup pickup && level.getFluidState(pos).is(FluidTags.WATER)) {
                        if (!level.isClientSide) {
                            int amountToFill = Math.min(MAX_CAPACITY - fluidHandler.getFluidInTank(0).getAmount(), 1000);
                            FluidStack waterStack = new FluidStack(Fluids.WATER, amountToFill);

                            int filled = fluidHandler.fill(waterStack, IFluidHandler.FluidAction.EXECUTE);

                            if (filled > 0) {
                                pickup.pickupBlock(level, pos, state);
                                level.playSound(null, pos, SoundEvents.BUCKET_FILL, player.getSoundSource(), 0.5F, 1.0F);
                            } else {
                                player.displayClientMessage(Component.literal("The watering can is already full!"), true);
                            }
                        }
                        return InteractionResultHolder.sidedSuccess(heldItem, level.isClientSide);

                    }

                }
            }

            if (!fluidHandler.getFluidInTank(0).isEmpty()) {
                // Begin watering action
                if (!(player instanceof FakePlayer)) {
                    player.startUsingItem(hand);
                }
                return InteractionResultHolder.consume(heldItem);
            }

            return InteractionResultHolder.pass(heldItem);
        }).orElse(InteractionResultHolder.pass(heldItem));
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        int useTicks = 72000 - remainingUseDuration; // Calculate elapsed ticks
        final int RANDOM_TICK_COOLDOWN = 2; // Number of ticks between watering actions


        if (!level.isClientSide && livingEntity instanceof Player player) {
            stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluidHandler -> {
                int currentWater = fluidHandler.getFluidInTank(0).getAmount();

                if (useTicks >= STARTUP_TIME && currentWater >= USE_WATER_PER_ACTION) {
                    BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        BlockPos targetPos = hitResult.getBlockPos();

                        int remainingWater = fluidHandler.getFluidInTank(0).getAmount();

                        if ((remainingWater - USE_WATER_PER_ACTION * ((2 * RANGE + 1) * (2 * RANGE + 1)) >= 0)) {
                            for (int dx = -RANGE; dx <= RANGE; dx++) {
                                for (int dz = -RANGE; dz <= RANGE; dz++) {

                                    BlockPos pos = targetPos.offset(dx, 0, dz);
                                    BlockState targetBlock = level.getBlockState(pos);
                                    // Perform watering action with growth boost
                                    if (level.random.nextDouble() < GROWTH_BOOST) { // Apply boost chance
                                        if ((useTicks % RANDOM_TICK_COOLDOWN == 0) && (targetBlock.is(BlockTags.CROPS) || targetBlock.is(BlockTags.SAPLINGS) || targetBlock.is(Blocks.GRASS_BLOCK) || targetBlock.getBlock() instanceof BonemealableBlock || targetBlock.getBlock() instanceof SugarCaneBlock)) {
                                            // Print the block being interacted with
                                            System.out.println("Interacting with block: " + targetBlock.getBlock().getName() + " at " + pos);

                                            targetBlock.randomTick((ServerLevel) level, pos, level.getRandom());
                                        }
                                    }

                                    // Particle effects
                                    for (int i = 0; i < PARTICLE_COUNT; i++) {
                                        double x = pos.getX() + level.random.nextDouble();
                                        double y = pos.getY() + 1;
                                        double z = pos.getZ() + level.random.nextDouble();
                                        ((ServerLevel) level).sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0.0, 0.0, 0.0, 0.1);
                                    }
                                }
                            }
                        }
                        else {
                            BlockState targetBlock = level.getBlockState(targetPos);
                            // Perform watering action with growth boost
                            if (level.random.nextDouble() < GROWTH_BOOST) { // Apply boost chance
                                if ((useTicks % RANDOM_TICK_COOLDOWN == 0) && (targetBlock.is(BlockTags.CROPS) || targetBlock.is(BlockTags.SAPLINGS) || targetBlock.is(Blocks.GRASS_BLOCK) || targetBlock.getBlock() instanceof BonemealableBlock || targetBlock.getBlock() instanceof SugarCaneBlock)) {
                                    targetBlock.randomTick((ServerLevel) level, targetPos, level.getRandom());
                                }
                            }

                            // Particle effects
                            for (int i = 0; i < PARTICLE_COUNT; i++) {
                                double x = targetPos.getX() + level.random.nextDouble();
                                double y = targetPos.getY() + 1;
                                double z = targetPos.getZ() + level.random.nextDouble();
                                ((ServerLevel) level).sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0.0, 0.0, 0.0, 0.1);
                            }
                        }


                            remainingWater = fluidHandler.getFluidInTank(0).getAmount();
                            if (player != null) {
                                player.displayClientMessage(
                                        Component.literal("Remaining water: " + remainingWater + " mB"), true
                                );
                            }
                            ((FluidHandler) fluidHandler).drain(USE_WATER_PER_ACTION * ((2 * RANGE + 1) * (2 * RANGE + 1)));
                            // Play sound every 20 ticks after STARTUP_TIME
                            if ((useTicks - STARTUP_TIME) % 20 == 0) {
                                level.playSound(null, targetPos, SoundEvents.WEATHER_RAIN, player.getSoundSource(), 0.5F, 1.0F);
                            }

                    }
                }
            });
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (!level.isClientSide && timeCharged > STARTUP_TIME) {
            level.playSound(((Player) livingEntity), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                    SoundEvents.EMPTY, ((Player) livingEntity).getSoundSource(), 0.6F, 0.7F);
        }
    }

    // Custom fluid handler for the watering can
    private static class FluidHandler extends FluidHandlerItemStack {
        public FluidHandler(ItemStack container, int capacity) {
            super(container, capacity);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return fluid.getFluid() == Fluids.WATER; // Only water can be added
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluid) {
            return false; // Prevent draining to other containers
        }

        public void drain(int usageAmount) {
            var contained = getFluid();
            int drainAmount = Math.min(contained.getAmount(), usageAmount);

            contained.shrink(drainAmount);

            if (contained.isEmpty()) {
                setContainerToEmpty();
            } else {
                setFluid(contained);
            }
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ClientExtensions.INSTANCE);
    }

    private enum ClientExtensions implements IClientItemExtensions {
        INSTANCE;

        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProgress, float swingProgress) {
            if (player.isUsingItem()) {
                var useTicks = 72000 - 1 - player.getUseItemRemainingTicks();
                var step = useTicks + partialTick;
                var startProgress = easeOutCubic(Math.min(step, STARTUP_TIME) / STARTUP_TIME);

                poseStack.translate(-0.2 * startProgress, -0.2 * startProgress, 0);

                if (startProgress == 1.0f) {
                    var sin = Mth.sin(0.35f * (step - 10f));
                    poseStack.rotateAround(Axis.XP.rotationDegrees(10 * sin), 0f, 0f, -0.2f);
                    //poseStack.translate(0, 0.2 * sin, 0);
                }

                var rotate = Mth.lerp(startProgress, 0, Mth.DEG_TO_RAD);

                poseStack.rotateAround(Axis.ZP.rotation(rotate * 15f), -0.75f, 0f, 0);

                int i = arm == HumanoidArm.RIGHT ? 1 : -1;
                poseStack.translate((float) i * 0.56F, -0.52F + (player.isUsingItem() ? 0 : equipProgress) * -0.6F, -0.72F);

                return true;
            }


            return false;
        }

        // https://easings.net/#easeOutCubic
        private static float easeOutCubic(float progress) {
            var opposite = 1 - progress;
            return 1 - opposite * opposite * opposite;
        }
    }

}
