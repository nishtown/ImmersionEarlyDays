package net.nishtown.immersionearlydays.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.nishtown.immersionearlydays.ImmersionEarlyDays;
import net.nishtown.immersionearlydays.block.ModBlocks;
import net.nishtown.immersionearlydays.block.custom.BambooTapBlock;


public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ImmersionEarlyDays.MOD_ID, exFileHelper);
    }


    @Override
    protected void registerStatesAndModels() {

        blockWithItem(ModBlocks.CRUSHED_STONE_BLOCK);
        blockWithItem(ModBlocks.COARSE_GRAVEL_BLOCK);
        blockWithItem(ModBlocks.DAMP_GRAVEL_BLOCK);

        simpleBlockWithItem(ModBlocks.STONE_WASHER_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/stone_washer")));

        simpleBlockWithItem(ModBlocks.WATER_TANK_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/water_tank")));

        simpleBlockWithItem(ModBlocks.SPRINKLER_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/sprinkler")));

        bambooTapBlock(ModBlocks.BAMBOO_TAP_BLOCK.get());
        simpleBlockItem(ModBlocks.BAMBOO_TAP_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/bamboo_tap_empty")));


    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void bambooTapBlock(Block block) {
        // Define models for each state
        ModelFile emptyModel = new ModelFile.UncheckedModelFile(modLoc("block/bamboo_tap_empty"));
        ModelFile runningModel = new ModelFile.UncheckedModelFile(modLoc("block/bamboo_tap_running"));
        ModelFile fullModel = new ModelFile.UncheckedModelFile(modLoc("block/bamboo_tap_full"));

        // Use getVariantBuilder to define states
        getVariantBuilder(block)
                .partialState().with(BambooTapBlock.STATE, BambooTapBlock.TapState.EMPTY)
                .modelForState().modelFile(emptyModel).addModel()
                .partialState().with(BambooTapBlock.STATE, BambooTapBlock.TapState.RUNNING)
                .modelForState().modelFile(runningModel).addModel()
                .partialState().with(BambooTapBlock.STATE, BambooTapBlock.TapState.FULL)
                .modelForState().modelFile(fullModel).addModel();
    }
}
