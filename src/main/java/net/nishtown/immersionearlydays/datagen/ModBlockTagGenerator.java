package net.nishtown.immersionearlydays.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.block.ModBlocks;
import net.nishtown.immersionearlydays.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {


    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ImmersionEarlyDays.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.CRUSHED_STONE_BLOCK.get())
                .add(ModBlocks.WATER_TANK_BLOCK.get())
                .add(ModBlocks.BAMBOO_TAP_BLOCK.get())
                .add(ModBlocks.SPRINKLER_BLOCK.get());


        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.COARSE_GRAVEL_BLOCK.get())
                .add(ModBlocks.DAMP_GRAVEL_BLOCK.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.WATER_TANK_BLOCK.get())
                .add(ModBlocks.BAMBOO_TAP_BLOCK.get())
                .add(ModBlocks.SPRINKLER_BLOCK.get());

//        this.tag(BlockTags.NEEDS_IRON_TOOL)
//                .add(ModBlocks.SAPPHIRE_BLOCK.get());
//
//        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
//                .add(ModBlocks.RAW_SAPPHIRE_BLOCK.get());
//
//        this.tag(BlockTags.NEEDS_STONE_TOOL)
//                .add(ModBlocks.NETHER_SAPPHIRE_ORE.get());
//
//        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
//                .add(ModBlocks.END_STONE_SAPPHIRE_ORE.get());
    }
}
