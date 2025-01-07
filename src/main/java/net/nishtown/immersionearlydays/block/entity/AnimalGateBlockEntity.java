package net.nishtown.immersionearlydays.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.nishtown.immersionearlydays.block.ModBlockEntities;
import net.nishtown.immersionearlydays.block.custom.AnimalGateBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class AnimalGateBlockEntity extends BlockEntity {
    private static final int MAX_ANIMALS = 10; // Threshold to open the gate
    public static final int COOLDOWN_TICKS = 20; // 1 second (20 ticks per second)
    public int cooldownCounter = 0;


    public AnimalGateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ANIMAL_GATE_BE.get(), pos, state);
    }




    public static void tick(Level level, BlockPos pos, BlockState state, AnimalGateBlockEntity entity) {
        if (!level.isClientSide) {

            // Initialize or increment the cooldown counter
            if (entity.cooldownCounter < entity.COOLDOWN_TICKS) {
                entity.cooldownCounter++;
                return; // Skip this tick
            }
            entity.cooldownCounter = 0; // Reset cooldown


            // Get the facing direction of the gate
            Direction facing = state.getValue(AnimalGateBlock.FACING);

            // Calculate perpendicular directions for width
            Direction left = facing.getCounterClockWise();
            Direction right = facing.getClockWise();

            // Define the bounds of the detection area
            BlockPos start = pos.relative(facing.getOpposite(), 0) // 1 block behind the gate
                    .relative(left, 3)
                    .below(1);// Extend 2 blocks to the left
            BlockPos end = pos.relative(facing, 6)             // 4 blocks in front of the gate
                    .relative(right, 3)              // Extend 2 blocks to the right
                    .above(1);                       // 2 blocks above (total height = 3)

            // Create the detection area
            AABB detectionBox = new AABB(start, end);

            // Count all chickens within the detection box
            List<Chicken> chickens = level.getEntitiesOfClass(Chicken.class, detectionBox);
            int count = chickens.size();

            // Open or close the gate based on the chicken count
            if (count > MAX_ANIMALS && !state.getValue(AnimalGateBlock.OPEN)) {
                if (!state.getValue(AnimalGateBlock.OPEN)) {
                    ((AnimalGateBlock) state.getBlock()).toggleGate(level, pos, state);
                    System.out.println("Gate opened at " + pos + " due to excess chickens.");
                }

                // Filter out baby chickens
                List<Chicken> adultChickens = chickens.stream()
                        .filter(chicken -> !chicken.isBaby())
                        .toList();

                // Select a random chicken from the filtered list
                if (!adultChickens.isEmpty()) {
                    Chicken selectedChicken = adultChickens.get(level.random.nextInt(adultChickens.size()));
                    selectedChicken.setNoAi(false);
                    // Set the destination for the selected chicken (through the gate)
                    BlockPos gateDestination = pos.relative(facing.getOpposite(), 4);
                    selectedChicken.setPos(pos.getCenter()); //Teleports the chicken to the gate
                    moveChickenToward(level, selectedChicken, gateDestination, true);

                    for (Chicken chicken : chickens) {
                        if (chicken != selectedChicken) {
                            chicken.setNoAi(true);
                        }
                    }
                }

            } else if (count <= MAX_ANIMALS && state.getValue(AnimalGateBlock.OPEN)) {
                ((AnimalGateBlock) state.getBlock()).toggleGate(level, pos, state);
                System.out.println("Gate closed at " + pos + " as chicken count is within limit.");
                // Unfreeze all chickens
                for (Chicken chicken : chickens) {
                    chicken.setNoAi(false);

                }
            }
        }
    }

    private static void moveChickenToward(Level level, Chicken chicken, BlockPos destination, boolean highlight) {
        double dx = destination.getX() + 0.5 - chicken.getX();
        double dy = destination.getY() - chicken.getY();
        double dz = destination.getZ() + 0.5 - chicken.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 0.1) { // Ensure there's a meaningful distance to move
            double speed = 1; // Adjust speed as needed


            // Optionally, add particles for debugging
            if (highlight) {
                chicken.setGlowingTag(true);
            }

            //chicken.setDeltaMovement(dx / distance * speed, dy / distance * speed, dz / distance * speed);
            chicken.getNavigation().moveTo(dx / distance * speed, dy / distance * speed, dz / distance * speed,speed);


        } else {
            // Stop the chicken if it has reached the destination
            chicken.getNavigation().moveTo(0,0,0,0);
        }
    }


}
