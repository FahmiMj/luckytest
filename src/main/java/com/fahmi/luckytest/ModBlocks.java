package com.fahmi.luckytest;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

public class ModBlocks {
    public static final Block FOOD_BLOCK = register(
            ModBlockItemIds.FOOD_BLOCK,
            FoodBlock::new,
            BlockBehaviour.Properties.of()
                    // Soft and easy to break by hand; no tool required.
                    .strength(0.5f)
                    .sound(SoundType.WOOL)
                    // A block only drops loot if it is told which loot table to use.
                    .overrideLootTable(Optional.of(FoodBlock.NORMAL_LOOT_TABLE))
    );

    // Overload for blocks that also get an item (so you can hold and place them).
    private static Block register(BlockItemId id,
                                  Function<BlockBehaviour.Properties, Block> blockFactory,
                                  BlockBehaviour.Properties properties) {
        Block block = register(id.block(), blockFactory, properties);

        BlockItem blockItem = new BlockItem(block,
                new Item.Properties()
                        .useBlockDescriptionPrefix()
                        .setId(id.item()));
        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);

        return block;
    }

    // Overload for blocks without an item. The one above calls this one.
    private static Block register(ResourceKey<Block> key,
                                  Function<BlockBehaviour.Properties, Block> blockFactory,
                                  BlockBehaviour.Properties properties) {
        Block block = blockFactory.apply(properties.setId(key));
        Registry.register(BuiltInRegistries.BLOCK, key, block);
        return block;
    }

    public static void initialize() {
        // Put the block item in the Building Blocks tab of the Creative menu
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS)
                .register(tab -> tab.accept(FOOD_BLOCK.asItem()));
    }
}
