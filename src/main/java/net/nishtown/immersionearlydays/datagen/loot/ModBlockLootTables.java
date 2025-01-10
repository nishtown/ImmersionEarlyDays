package net.nishtown.immersionearlydays.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.registries.RegistryObject;
import net.nishtown.immersionearlydays.block.ModBlocks;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.dropSelf(ModBlocks.STONE_WASHER_BLOCK.get());
        this.dropSelf(ModBlocks.CRUSHED_STONE_BLOCK.get());
        this.dropSelf(ModBlocks.COARSE_GRAVEL_BLOCK.get());
        this.dropSelf(ModBlocks.DAMP_GRAVEL_BLOCK.get());
        this.dropSelf(ModBlocks.BAMBOO_TAP_BLOCK.get());
        this.dropSelf(ModBlocks.ANIMAL_GATE_BLOCK.get());
        this.dropSelf(ModBlocks.GIANT_BIRCH_SAPLING.get());
        this.dropSelf(ModBlocks.GIANT_OAK_SAPLING.get());
        this.dropSelf(ModBlocks.GIANT_ACACIA_SAPLING.get());
        this.dropSelf(ModBlocks.WATER_TANK_BLOCK.get());
        this.dropSelf(ModBlocks.SPRINKLER_BLOCK.get());


    }


    protected LootTable.Builder createGravelLikeDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(
                pBlock,
                AlternativesEntry.alternatives(
                        // Drop gravel with Fortune
                        LootItem.lootTableItem(item)
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(
                                        Enchantments.BLOCK_FORTUNE,
                                        new float[]{0.1F, 0.14285715F, 0.25F, 1.0F}
                                )),
                        // Drop gravel with a base 10% chance (no Fortune)
                        LootItem.lootTableItem(item)
                                .when(LootItemRandomChanceCondition.randomChance(0.1F)),
                        // Otherwise, drop the block itself
                        LootItem.lootTableItem(pBlock)
                )
        );
    }


    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
