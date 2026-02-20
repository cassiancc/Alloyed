package com.molybdenum.alloyed.common.entity.ai.goal;

import com.molybdenum.alloyed.common.content.blocks.ForgeBlock;
import com.molybdenum.alloyed.common.registry.ModBlocks;
import com.molybdenum.alloyed.common.util.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

public class FoxSitOnBlockGoal extends MoveToBlockGoal {
	private final Fox fox;

	public FoxSitOnBlockGoal(final Fox fox, final double speedModifier) {
		super(fox, speedModifier, 8);
		this.fox = fox;
	}

	@Override
	public boolean canUse() {
		return super.canUse();
	}

	@Override
	public void start() {
		super.start();

	}

	@Override
	public void stop() {
		super.stop();
		this.fox.stopSleeping();
	}

	@Override
	public void tick() {
		super.tick();
		if (isReachedTarget()) {
			fox.setSitting(false);
			fox.setIsCrouching(false);
			fox.setIsInterested(false);
			fox.setJumping(false);
			fox.setSleeping(true);
			fox.getNavigation().stop();
			fox.getMoveControl().setWantedPosition(fox.getX(), fox.getY(), fox.getZ(), (double)0.0F);
		}
	}

	@Override
	public double acceptedDistance() {
		return .8f;
	}

	@Override
	protected boolean isValidTarget(final LevelReader level, final BlockPos pos) {
		if (!level.isEmptyBlock(pos.above())) {
			return false;
		} else {
			BlockState blockState = level.getBlockState(pos);
			return blockState.is(ModBlocks.FORGE.get()) && blockState.getValue(ForgeBlock.LIT);
		}
	}
}
