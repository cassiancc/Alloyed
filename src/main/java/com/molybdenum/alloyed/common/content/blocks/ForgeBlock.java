package com.molybdenum.alloyed.common.content.blocks;

import com.mojang.serialization.MapCodec;
import com.molybdenum.alloyed.client.registry.ModSoundEvents;
import com.molybdenum.alloyed.common.content.blocks.entities.ForgeBlockEntity;
import com.molybdenum.alloyed.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;


public class ForgeBlock extends BaseEntityBlock {
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static BooleanProperty LIT = BlockStateProperties.LIT;

	public ForgeBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(ForgeBlock::new);
	}

	/* FACING */

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(LIT, Boolean.FALSE);
	}

	@Override
	public BlockState rotate(BlockState pState, Rotation pRotation) {
		return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState pState, Mirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
		if (state.getValue(LIT)) {
			double x = (double)pos.getX() + (double)0.5F;
			double y = pos.getY();
			double z = (double)pos.getZ() + (double)0.5F;
			if (randomSource.nextInt(10) == 0) {
				level.playLocalSound((double)pos.getX() + (double)0.5F, (double)pos.getY() + (double)0.5F, (double)pos.getZ() + (double)0.5F, ModSoundEvents.FORGE_CRACKLE.get(), SoundSource.BLOCKS, 0.5F + randomSource.nextFloat(), randomSource.nextFloat() * 0.7F + 0.6F, false);
			}
			Direction direction = state.getValue(FACING);
			Direction.Axis axis = direction.getAxis();
			double r1 = randomSource.nextDouble() * 0.6 - 0.3;
			double r2 = axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52 : r1;
			double r3 = randomSource.nextDouble() * (double)6.0F / (double)16.0F;
			double r4 = axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52 : r1;
			level.addParticle(ParticleTypes.SMOKE, x + r2, y + r3, z + r4, 0.0F, 0.0F, 0.0F);
			level.addParticle(ParticleTypes.FLAME, x + r2, y + r3, z + r4, 0.0, 0.0F, 0.0F);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(FACING, LIT);
	}

	/* BLOCK ENTITY */

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof ForgeBlockEntity forgeBlockEntity) {
			forgeBlockEntity.drops();
		}
		super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide()) {
			BlockEntity entity = level.getBlockEntity(pos);
			if(entity instanceof ForgeBlockEntity forgeBlockEntity) {
				//? fabric
				player.openMenu(forgeBlockEntity);
				//? neoforge
				/*player.openMenu(forgeBlockEntity, pos);*/
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}

		return InteractionResult.SUCCESS_SERVER;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new ForgeBlockEntity(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return createTickerHelper(pBlockEntityType, ModBlockEntities.FORGE_BLOCK_ENTITY.get(),
				ForgeBlockEntity::tick);
	}
}