/*
MIT License

Copyright (c) 2020 vectorwing

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

=============================================================================
This software includes code from the NeoForge project
https://github.com/neoforged/NeoForge/
For specifically InvWrapper and ItemStackHandler logic.

NeoForge is licensed under the LGPL 2.1 license.
You may read the license here:
https://github.com/neoforged/NeoForge/blob/1.21.x/LICENSE.txt

Some files may include others' licenses. Please read the top of the file
for information on other licenses.
=============================================================================
*/

package com.molybdenum.alloyed.common.handler;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.ArrayList;
import java.util.List;

public class RecipeWrapper implements RecipeInput {
	private final StackedItemContents stackedContents;
	private final int ingredientAmount;
	//? fabric {
	private final ItemHandler handler;
	private final ArrayList<ItemStack> stacks = new ArrayList<>();

	public RecipeWrapper(ItemHandler handler) {
		this.handler = handler;
		this.stackedContents = new StackedItemContents();
		int ingredientAmount = 0;

		for (int value : handler.getInputSlotIndexes()) {
			ItemStack itemstack = handler.getStackInSlot(value);
			if (!itemstack.isEmpty()) {
				++ingredientAmount;
				this.stackedContents.accountStack(itemstack, 1);
			}
			this.stacks.add(itemstack);
		}

		this.ingredientAmount = ingredientAmount;
	}
	//?} else {
	/*private final ItemStackHandler handler;
	private final ArrayList<ItemStack> stacks = new ArrayList<>();
	public RecipeWrapper(ItemStackHandler handler) {
		this.handler = handler;
		this.stackedContents = new StackedItemContents();
		int ingredientAmount = 0;

		for (int value : handler.getInputSlotIndexes()) {
			ItemStack itemstack = handler.getStackInSlot(value);
			if (!itemstack.isEmpty()) {
				++ingredientAmount;
				this.stackedContents.accountStack(itemstack, 1);
			}
			this.stacks.add(itemstack);
		}

		this.ingredientAmount = ingredientAmount;
	}
	*///?}

	public StackedItemContents stackedContents() {
		return this.stackedContents;
	}

	public int ingredientAmount() {
		return this.ingredientAmount;
	}

	public ItemStack getItem(int slot) {
		return this.handler.getStackInSlot(slot);
	}

	public int size() {
		return this.handler.getSlotCount();
	}

	public List<ItemStack> stacks() {
		return stacks;
	}
}
