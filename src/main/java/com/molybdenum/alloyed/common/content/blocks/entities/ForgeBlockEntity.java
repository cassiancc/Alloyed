package com.molybdenum.alloyed.common.content.blocks.entities;

//? fabric {

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
//?}
import com.molybdenum.alloyed.common.handler.ItemStackHandler;
import com.molybdenum.alloyed.common.handler.RecipeWrapper;
import com.molybdenum.alloyed.common.content.blocks.ForgeBlock;
import com.molybdenum.alloyed.common.content.recipes.AbstractForgingRecipe;
import com.molybdenum.alloyed.common.content.recipes.ShapelessForgingRecipe;
import com.molybdenum.alloyed.common.content.recipes.ShapedForgingRecipe;
import com.molybdenum.alloyed.common.content.recipes.ModRecipes;
import com.molybdenum.alloyed.common.registry.ModBlockEntities;
import com.molybdenum.alloyed.common.screen.ForgeMenu;
import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public class ForgeBlockEntity extends BlockEntity implements
		//? fabric
		ExtendedMenuProvider<BlockPos>
		//? neoforge
		/*MenuProvider*/
		, WorldlyContainer, StackedContentsCompatible {

	private static final int RESULT_SLOT_INDEX = 10;
	protected final ContainerData data;
	private int progress = 0;
	private int maxProgress = 72;
	private int litTime = 0;
	private int fuelAmount = 0;
	private static final int[] INGREDIENT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
	static int countOutput = 1;
	private ContainerOpenersCounter openersCounter;
	private AbstractForgingRecipe currentRecipe = null;

	private final ItemStackHandler itemHandler = new ItemStackHandler(11) {
		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
			if (slot < 9) {
				resetProgress();
			}
		}

		@Override
		public IntList getInputSlotIndexes() {
			return IntList.of(0, 1, 2, 3, 4, 5, 6, 7, 8);
		}

	};


	private final RecipeManager.CachedCheck<RecipeWrapper, ShapelessForgingRecipe> quickCheck = RecipeManager.createCheck(ModRecipes.SHAPELESS_FORGING_TYPE.get());
	private final RecipeManager.CachedCheck<RecipeWrapper, ShapedForgingRecipe> quickShapedCheck = RecipeManager.createCheck(ModRecipes.SHAPED_FORGING_TYPE.get());

	public ForgeBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModBlockEntities.FORGE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
		this.data = new ContainerData() {
			public int get(int index) {
				return switch (index) {
					case 0 -> ForgeBlockEntity.this.progress;
					case 1 -> ForgeBlockEntity.this.maxProgress;
					case 2 -> ForgeBlockEntity.this.litTime;
					case 3 -> ForgeBlockEntity.this.fuelAmount;
					default -> 0;
				};
			}

			public void set(int index, int value) {
				switch(index) {
					case 0: ForgeBlockEntity.this.progress = value; break;
					case 1: ForgeBlockEntity.this.maxProgress = value; break;
					case 2: ForgeBlockEntity.this.litTime = value; break;
				}
			}

			public int getCount() {
				return 4;
			}
		};
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("block.alloyed.forge");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
		return new ForgeMenu(pContainerId, pInventory, this, this.data);
	}

	@Override
	protected void saveAdditional(@NotNull ValueOutput tag) {
		itemHandler.serialize(tag);
		tag.putInt("forge.progress", progress);
		tag.putInt("forge.lit_time", litTime);
		tag.putInt("forge.max_progress", maxProgress);
		tag.putInt("forge.fuel_amount", fuelAmount);
		super.saveAdditional(tag);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		itemHandler.deserialize(input);
		progress = input.getIntOr("forge.progress", 0);
		litTime = input.getIntOr("forge.lit_time", 0);
		maxProgress = input.getIntOr("forge.max_progress", 0);
		fuelAmount = input.getIntOr("forge.fuel_amount", 0);
	}

	public void drops() {
		SimpleContainer inventory = new SimpleContainer(getContainerSize());
		for (int i = 0; i < getContainerSize(); i++) {
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}

		Containers.dropContents(this.level, this.worldPosition, inventory);
	}


	public static void tick(Level pLevel, BlockPos pPos, BlockState pState, ForgeBlockEntity pBlockEntity) {


		if (isFueled(pBlockEntity, pPos, pLevel)) {
			pBlockEntity.litTime--;
		} else {
			pBlockEntity.litTime = 0;
		}

		if (hasRecipe(pBlockEntity)) {
			pBlockEntity.progress++;
			pBlockEntity.setChanged(pLevel, pPos, pState, true);
			if (pBlockEntity.progress == pBlockEntity.maxProgress) {
				craftItem(pBlockEntity);
			}
		} else {
			pBlockEntity.resetProgress();
		}
	}

	private void setChanged(Level pLevel, BlockPos pPos, BlockState pState, boolean b) {
		pLevel.setBlock(pPos, pState.setValue(ForgeBlock.LIT, b), 3);
		super.setChanged();
	}

	private static boolean hasRecipe(ForgeBlockEntity entity) {
		Level level = entity.level;
		BlockPos pos = entity.getBlockPos();

		RecipeWrapper inventory = new RecipeWrapper(entity.itemHandler);

		if (!inventory.getItem(10).isEmpty() && !inventory.getItem(10).isStackable()) {
			return false;
		}

		if (entity.currentRecipe != null) {
			return startCraftIfFueled(entity, pos, level, entity.currentRecipe.getCookTime());
		}

		// Check for ForgeRecipe
		if (level instanceof ServerLevel serverLevel) {
			Optional<RecipeHolder<ShapedForgingRecipe>> shapedRecipeRecipeHolder = entity.quickShapedCheck.getRecipeFor(inventory, serverLevel);
			if (shapedRecipeRecipeHolder.isPresent()) {
				entity.currentRecipe = shapedRecipeRecipeHolder.get().value();
				return startCraftIfFueled(entity, pos, level, shapedRecipeRecipeHolder.get().value().getCookTime());
			}
			Optional<RecipeHolder<ShapelessForgingRecipe>> recipeMatch = entity.quickCheck.getRecipeFor(inventory, serverLevel);
			if (recipeMatch.isPresent()) {
				entity.currentRecipe = recipeMatch.get().value();
				return startCraftIfFueled(entity, pos, level, recipeMatch.get().value().getCookTime());
			}
		}
		entity.currentRecipe = null;
		return false;
	}

	static boolean startCraftIfFueled(ForgeBlockEntity entity, BlockPos pos, Level level, int progress) {
		if (!isFueled(entity, pos, level)) {
			if (!entity.burnFuel())
				return false;
		}
		entity.maxProgress = progress;
		return true;
	}

	static boolean isFueled(ForgeBlockEntity entity, BlockPos pos, Level level) {
		if (level.isClientSide()) return false;
		if (entity.litTime > 0) {
			entity.setChanged(level, pos, entity.getBlockState(), true);
			return true;
		}
		else {
			entity.setChanged(level, pos, entity.getBlockState(), false);
			return false;
		}
	}

	private boolean burnFuel() {
		if (this.level instanceof ServerLevel) {
			var fuel = this.itemHandler.getStackInSlot(9).copy();
			if (isFuel(level, fuel) && this.litTime == 0) {
				this.fuelAmount = getBurnTime(level, fuel);
				this.litTime = getBurnTime(level, fuel);
				if (fuel.getCount() > 1) {
					fuel.setCount(fuel.getCount()-1);
					this.itemHandler.setStackInSlot(9, fuel);
				} else {
					this.itemHandler.setStackInSlot(9, remainder(fuel));
				}
				return true;
			}
		}
		return false;
	}

	public static int getBurnTime(Level level, ItemStack fuel) {
		if (level == null) return 0;
		//? fabric
		return level.fuelValues().burnDuration(fuel);
		//? neoforge
		/*return fuel.getBurnTime(null, level.fuelValues());*/
	}

	public static boolean isFuel(Level level, ItemStack fuel) {
		return level.fuelValues().isFuel(fuel);
	}

	private static void craftItem(ForgeBlockEntity entity) {

		SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots().size());
		for (int i = 0; i < entity.itemHandler.getSlots().size(); i++) {
			inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
		}

		AbstractForgingRecipe currentRecipe = entity.currentRecipe;
		if (currentRecipe != null) {
			ItemStack result = currentRecipe.assemble(new RecipeWrapper(entity.itemHandler));
			for(int i = 0; i < 9; ++i) {
				ItemStack slotStack = entity.itemHandler.getStackInSlot(i);
				if (slotStack.getCraftingRemainder() != null) {
					Direction direction = entity.getBlockState().getValue(ForgeBlock.FACING).getCounterClockWise();
					double x = (double)entity.worldPosition.getX() + 0.5 + (double)direction.getStepX() * 0.25;
					double y = (double)entity.worldPosition.getY() + 0.7;
					double z = (double)entity.worldPosition.getZ() + 0.5 + (double)direction.getStepZ() * 0.25;
					spawnItemEntity(entity.level, remainder(entity.itemHandler.getStackInSlot(i)), x, y, z, (float)direction.getStepX() * 0.08F, 0.25, (float)direction.getStepZ() * 0.08F);
				}
			}

			for (int i = 0; i < 9; ++i) {
				entity.itemHandler.extractItem(i, 1, false);
			}
			ItemStack currentItemInResultSlot = entity.getItem(RESULT_SLOT_INDEX);
			if (ItemStack.isSameItemSameComponents(currentItemInResultSlot, result)) {
				entity.setItem(RESULT_SLOT_INDEX, currentItemInResultSlot.copyWithCount(currentItemInResultSlot.getCount() + result.count()));
			} else {
				entity.setItem(RESULT_SLOT_INDEX, result);
			}

			entity.resetProgress();

		}
	}

	private static ItemStack remainder(ItemStack slotStack) {
		ItemStackTemplate recipeRemainder = slotStack.getCraftingRemainder();
		if (recipeRemainder == null) return ItemStack.EMPTY;
		return recipeRemainder.create();
	}

	public static void spawnItemEntity(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
		ItemEntity entity = new ItemEntity(level, x, y, z, stack);
		entity.setDeltaMovement(xMotion, yMotion, zMotion);
		level.addFreshEntity(entity);
	}

	private int getTheCount (ItemStack itemIn) {
		return itemIn.getCount();
	}

	private void resetProgress() {
		this.progress = 0;
		this.maxProgress = 72;
		this.currentRecipe = null;
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		if (direction == Direction.UP) {
			return INGREDIENT_SLOTS;
		} else {
			return new int[]{direction == Direction.DOWN ? 10 : 9};
		}
	}

	public boolean canPlaceItem(int slot, ItemStack itemStack) {
		if (slot == 10) {
			return false;
		} else if (slot == 9) {
			return getBurnTime(level, itemStack) > 0;
		} else {
			return true;
		}
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
		return canPlaceItem(slot, itemStack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
		return true;
	}

	@Override
	public int getContainerSize() {
		return this.itemHandler.getSlots().size();
	}

	@Override
	public boolean isEmpty() {
		for(int i = 0; i < getContainerSize(); ++i) {
			ItemStack itemStack = this.itemHandler.getStackInSlot(i);
			if (!itemStack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.itemHandler.getStackInSlot(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return this.itemHandler.extractItem(slot, amount, false);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return this.itemHandler.extractItem(slot, 1, false);
	}

	@Override
	public void setItem(int slot, ItemStack itemStack) {
		this.itemHandler.setStackInSlot(slot, itemStack);
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clearContent() {
		for (int i = 0; i < getContainerSize(); i++) {
			this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public void fillStackedContents(StackedItemContents pHelper) {
		for (int i = 0; i < this.getContainerSize(); i++) {
			ItemStack stack = this.getItem(i);
			pHelper.accountStack(stack);
		}
	}

	public ItemStackHandler getItemHandler() {
		return itemHandler;
	}

	//? fabric {
	@Override
	public BlockPos getScreenOpeningData(ServerPlayer player) {
		return worldPosition;
	}
	//?}
}