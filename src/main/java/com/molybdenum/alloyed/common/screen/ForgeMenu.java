package com.molybdenum.alloyed.common.screen;


import com.molybdenum.alloyed.common.handler.ItemStackHandler;
import com.molybdenum.alloyed.common.content.blocks.entities.ForgeBlockEntity;
import com.molybdenum.alloyed.common.registry.ModBlocks;
import com.molybdenum.alloyed.common.screen.slot.ModFuelSlot;
import com.molybdenum.alloyed.common.screen.slot.ModInputSlot;
import com.molybdenum.alloyed.common.screen.slot.ModResultSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ForgeMenu extends AbstractContainerMenu {

	private final ForgeBlockEntity blockEntity;
	private final Level level;
	private final ContainerData data;

	public ForgeMenu(int pContainerId, Inventory inv, BlockPos extraData) {
		this(pContainerId, inv, inv.player.level().getBlockEntity(extraData), new SimpleContainerData(4));
	}

	public ForgeMenu(int pContainerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
		this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
	}

	public ForgeMenu(int pContainerId, Inventory pPlayerInventory, BlockEntity entity, ContainerData data) {
		super(ModMenuTypes.FORGE_MENU.get(), pContainerId);
		checkContainerSize(pPlayerInventory, 11);
		blockEntity = ((ForgeBlockEntity) entity);
		this.level = pPlayerInventory.player.level();
		this.data = data;

		ItemStackHandler handler = blockEntity.getItemHandler();  // Assuming getItemHandler() returns ItemStackHandler
		this.addSlot(new ModInputSlot(handler, 0, 30, 17));    // Slot 0
		this.addSlot(new ModInputSlot(handler, 1, 48, 17));    // Slot 1
		this.addSlot(new ModInputSlot(handler, 2, 66, 17));    // Slot 2
		this.addSlot(new ModInputSlot(handler, 3, 30, 35));    // Slot 3
		this.addSlot(new ModInputSlot(handler, 4, 48, 35));    // Slot 4
		this.addSlot(new ModInputSlot(handler, 5, 66, 35));    // Slot 5
		this.addSlot(new ModInputSlot(handler, 6, 30, 53));    // Slot 6
		this.addSlot(new ModInputSlot(handler, 7, 48, 53));    // Slot 7
		this.addSlot(new ModInputSlot(handler, 8, 66, 53));    // Slot 8
		this.addSlot(new ModFuelSlot(handler, level.fuelValues(), 9, 93, 53));  // Fuel slot
		this.addSlot(new ModResultSlot(handler, 10, 124, 18));  // Output slot

		// inventory
		for (int x = 0; x < 3; ++x) {
			for (int y = 0; y < 9; ++y) {
				this.addSlot(new Slot(pPlayerInventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
			}
		}

		// hotbar
		for (int x = 0; x < 9; ++x) {
			this.addSlot(new Slot(pPlayerInventory, x, 8 + x * 18, 142));
		}

		addDataSlots(data);
	}

	public boolean isCrafting() {
		return data.get(0) > 0;
	}

	public boolean isFueled() {
		return blockEntity.getBlockState().getValue(BlockStateProperties.LIT);
	}

	public int getScaledProgress() {
		int progress = this.data.get(0);
		int maxProgress = this.data.get(1);  // Max Progress
		int progressArrowSize = 26; // This is the height in pixels of your arrow

		return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
	}

	public int getLitTime() {
		int litTime = this.data.get(2);
		int fuel = this.data.get(3);
		float percentage = (float) litTime / fuel;
		percentage = percentage * 17;
		return (int) percentage;
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		Slot sourceSlot = slots.get(pIndex);
		if (!sourceSlot.hasItem()) return ItemStack.EMPTY;

		ItemStack sourceStack = sourceSlot.getItem();
		ItemStack sourceStackCopy = sourceStack.copy();

		if (pIndex == 10) {
			if (!this.moveItemStackTo(sourceStack, 11, 47, true)) {
				return ItemStack.EMPTY;
			}
			sourceSlot.onQuickCraft(sourceStack, sourceStackCopy);
		} else if (pIndex < 10) {
			if (!moveItemStackTo(sourceStack, 11, 47, false)) {
				return ItemStack.EMPTY;
			}
		} else {
			boolean isFuel = ForgeBlockEntity.isFuel(level, sourceStack);
			if (isFuel && !moveItemStackTo(sourceStack, 9, 10, false)) {
				if (!moveItemStackTo(sourceStack, 0, 9, false)) {
					return ItemStack.EMPTY;
				}
			}
			if (!moveItemStackTo(sourceStack, 0, 9, false)) {
				return ItemStack.EMPTY;
			}
		}

		if (sourceStack.getCount() == 0) sourceSlot.set(ItemStack.EMPTY);
		else sourceSlot.setChanged();

		sourceSlot.onTake(pPlayer, sourceStack);
		return sourceStackCopy;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
				pPlayer, ModBlocks.FORGE.get());
	}

	public BlockEntity getBlockEntity() {
		return this.blockEntity;
	}

}