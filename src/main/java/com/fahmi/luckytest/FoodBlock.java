package com.fahmi.luckytest;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class FoodBlock extends Block {
    // true = a Lucky Coin has been applied, so it drops the richer loot table
    public static final BooleanProperty LUCKY = BooleanProperty.create("lucky");

    public static final ResourceKey<LootTable> NORMAL_LOOT_TABLE =
            ResourceKey.create(Registries.LOOT_TABLE, LuckyTest.id("blocks/food_block"));
    public static final ResourceKey<LootTable> LUCKY_LOOT_TABLE =
            ResourceKey.create(Registries.LOOT_TABLE, LuckyTest.id("blocks/food_block_lucky"));

    public FoodBlock(Properties properties) {
        super(properties);
        // BooleanProperty lists true before false, so stateDefinition.any() would
        // make "lucky" default to true. Pin the default state to false explicitly.
        this.registerDefaultState(this.stateDefinition.any().setValue(LUCKY, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // Adds the "lucky" property, so a placed block remembers whether it was upgraded.
        builder.add(LUCKY);
    }

    // Called by the Lucky Coin when the player shift + right-clicks this block.
    public static void applyLucky(BlockState state, Level level, BlockPos pos, Player player, ItemStack coin) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        level.setBlock(pos, state.setValue(LUCKY, true), Block.UPDATE_ALL);

        if (!player.isCreative()) {
            coin.shrink(1);
        }

        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                12, 0.35, 0.35, 0.35, 0.05);

        player.sendSystemMessage(Component.literal("Lucky Coin Applied"));
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        ResourceKey<LootTable> key = state.getValue(LUCKY) ? LUCKY_LOOT_TABLE : NORMAL_LOOT_TABLE;

        LootParams lootParams = params
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .create(LootContextParamSets.BLOCK);

        LootTable table = lootParams.getLevel().getServer()
                .reloadableRegistries().getLootTable(key);

        return table.getRandomItems(lootParams);
    }
}
