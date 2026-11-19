package com.fahmi.luckytest;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LuckyCoinItem extends Item {
    public LuckyCoinItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        // Only shift + right-click on a Food Block does something.
        boolean shiftClickOnFoodBlock = player != null
                && player.isSecondaryUseActive()
                && state.getBlock() instanceof FoodBlock;

        if (!shiftClickOnFoodBlock) {
            return super.useOn(context);
        }

        if (state.getValue(FoodBlock.LUCKY)) {
            if (!level.isClientSide()) {
                player.sendSystemMessage(Component.literal("This Food Block is already lucky"));
            }
            return InteractionResult.SUCCESS;
        }

        FoodBlock.applyLucky(state, level, pos, player, context.getItemInHand());
        return InteractionResult.SUCCESS;
    }
}
