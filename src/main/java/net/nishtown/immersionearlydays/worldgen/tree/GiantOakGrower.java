package net.nishtown.immersionearlydays.worldgen.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.nishtown.immersionearlydays.worldgen.ModConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

public class GiantOakGrower extends AbstractTreeGrower {
    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pHasFlowers) {
        return ModConfiguredFeatures.GIANT_BIRCH_KEY;
    }
}
