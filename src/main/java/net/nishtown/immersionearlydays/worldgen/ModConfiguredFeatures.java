package net.nishtown.immersionearlydays.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;

public class ModConfiguredFeatures {
    public  static final ResourceKey<ConfiguredFeature<?,?>> GIANT_BIRCH_KEY = registerKey("giantbirch");
    public  static final ResourceKey<ConfiguredFeature<?,?>> GIANT_OAK_KEY = registerKey("giantoak");
    public  static final ResourceKey<ConfiguredFeature<?,?>> GIANT_ACACIA_KEY = registerKey("giantacacia");

    public  static void bootstrap(BootstapContext<ConfiguredFeature<?,?>> context){
        register(context, GIANT_BIRCH_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.BIRCH_LOG),
                new GiantTrunkPlacer(8,3,2),
                BlockStateProvider.simple(Blocks.BIRCH_LEAVES),
                new BlobFoliagePlacer(ConstantInt.of(2),ConstantInt.of(1), 3),
                new TwoLayersFeatureSize(1,0,1)).build());

        register(context, GIANT_OAK_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG),
                new GiantTrunkPlacer(15,5,2),
                BlockStateProvider.simple(Blocks.OAK_LEAVES),
                new BlobFoliagePlacer(ConstantInt.of(4),ConstantInt.of(2), 4),
                new TwoLayersFeatureSize(2,0,2)).build());

        register(context, GIANT_ACACIA_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.ACACIA_LOG),
                new GiantTrunkPlacer(5,4,3),
                BlockStateProvider.simple(Blocks.ACACIA_LEAVES),
                new BlobFoliagePlacer(ConstantInt.of(3),ConstantInt.of(2), 3),
                new TwoLayersFeatureSize(1,0,2)).build());

    }

    public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ImmersionEarlyDays.MOD_ID, name));
    }


    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
