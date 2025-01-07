package net.nishtown.immersionearlydays.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.block.ModBlocks;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> GIANT_BIRCH_PLACED_KEY = registerKey("giantbirch_placed");
    public static final ResourceKey<PlacedFeature> GIANT_OAK_PLACED_KEY = registerKey("giantoak_placed");
    public static final ResourceKey<PlacedFeature> GIANT_ACACIA_PLACED_KEY = registerKey("giantacacia_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        // Giant Birch Tree Placement
        register(context, GIANT_BIRCH_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.GIANT_BIRCH_KEY),
                List.of(PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)));

        register(context, GIANT_OAK_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.GIANT_OAK_KEY),
                List.of(PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING))
        );

        register(context, GIANT_ACACIA_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.GIANT_ACACIA_KEY),
                List.of()
        );
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ImmersionEarlyDays.MOD_ID, name));
    }


    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));

    }
}
