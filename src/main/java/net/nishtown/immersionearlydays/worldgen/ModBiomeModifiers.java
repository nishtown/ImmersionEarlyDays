package net.nishtown.immersionearlydays.worldgen;

import io.netty.bootstrap.Bootstrap;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;

import java.security.PublicKey;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_TREE_GIANT_BIRCH = registerKey("add_tree_giant_birch");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
//        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
//        var biomes = context.lookup(Registries.BIOME);

        //This adds it to the world gen.
//        context.register(ADD_TREE_GIANT_BIRCH, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
//                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
//                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.GIANT_BIRCH_PLACED_KEY)),
//                GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(ImmersionEarlyDays.MOD_ID, name));
    }
}
