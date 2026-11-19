package com.fahmi.luckytest;

import net.minecraft.references.BlockItemId;

public class ModBlockItemIds {
    public static final BlockItemId FOOD_BLOCK = create("food_block");

    private static BlockItemId create(String name) {
        // Blocks with an item need two keys: one for the block, one for its item.
        // We use the same name for both, so `food_block` and `food_block` line up.
        return BlockItemId.create(LuckyTest.id(name), LuckyTest.id(name));
    }
}
