package com.molybdenum.alloyed.common.util;

import com.molybdenum.alloyed.common.compat.create.CreateAlloyedBlocks;
import com.molybdenum.alloyed.common.registry.ModBlocks;
import com.zurrtum.create.content.decoration.encasing.EncasedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class EncasingHelper {

    protected static Supplier<? extends EncasedBlock> getSteelEncasedVariant(EncaseType type) {
        if (type == EncaseType.SHAFT) return CreateAlloyedBlocks.STEEL_ENCASED_SHAFT;
        if (type == EncaseType.SMALL_COG) return CreateAlloyedBlocks.STEEL_ENCASED_COGWHEEL;
        if (type == EncaseType.LARGE_COG) return CreateAlloyedBlocks.STEEL_ENCASED_LARGE_COGWHEEL;
        throw new IllegalStateException("How did we get here?");
        //return ModBlocks.STEEL_ENCASED_FLUID_PIPE; //TODO: eventually get this working
    }

    protected static Supplier<? extends EncasedBlock> getBronzeEncasedVariant(EncaseType type) {
        if (type == EncaseType.SHAFT) return CreateAlloyedBlocks.BRONZE_ENCASED_SHAFT;
        if (type == EncaseType.SMALL_COG) return CreateAlloyedBlocks.BRONZE_ENCASED_COGWHEEL;
        if (type == EncaseType.LARGE_COG) return CreateAlloyedBlocks.BRONZE_ENCASED_LARGE_COGWHEEL;
        throw new IllegalStateException("How did we get here?");
        //return ModBlocks.STEEL_ENCASED_FLUID_PIPE; //TODO: eventually get this working
    }

    public static InteractionResult tryEncase(EncaseType type, BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand,
                                                  BlockHitResult ray) {

        if (CreateAlloyedBlocks.STEEL_CASING.isIn(heldItem))
            return tryEncaseWithSteel(type, state, level, pos,  heldItem, player, hand, ray);
        if (CreateAlloyedBlocks.BRONZE_CASING.isIn(heldItem))
            return tryEncaseWithBronze(type, state, level, pos,  heldItem, player, hand, ray);
        else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
    }

    public static InteractionResult tryEncaseWithSteel(EncaseType type, BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand,
                                                           BlockHitResult ray) {
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        getSteelEncasedVariant(type).get().handleEncasing(state, level, pos, heldItem, player, hand, ray);
        return InteractionResult.SUCCESS;
    }

    public static InteractionResult tryEncaseWithBronze(EncaseType type, BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand,
                                                           BlockHitResult ray) {
        if (level.isClientSide())
            return InteractionResult.SUCCESS;

        getBronzeEncasedVariant(type).get().handleEncasing(state, level, pos, heldItem, player, hand, ray);
        return InteractionResult.SUCCESS;
    }

    public enum EncaseType {
        SHAFT,
        SMALL_COG,
        LARGE_COG;
        //PIPE;

        public static EncaseType fromCogSize(boolean isLarge) {
            if (isLarge) return LARGE_COG;
            return SMALL_COG;
        }
    }
}
