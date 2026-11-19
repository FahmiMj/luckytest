package com.fahmi.luckytest;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class ModEvents {
    public static void initialize() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (!(state.getBlock() instanceof FoodBlock)) {
                return;
            }
            if (!(level instanceof ServerLevel serverLevel)) {
                return;
            }

            // A little burst of happy-villager sparkles where the block was.
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    20, 0.4, 0.4, 0.4, 0.1);

            // Saturation 10 (amplifier 10) for 2 seconds (40 ticks).
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 40, 10));
        });
    }
}
