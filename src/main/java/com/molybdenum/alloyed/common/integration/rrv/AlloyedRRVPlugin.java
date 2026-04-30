package com.molybdenum.alloyed.common.integration.rrv;

import cc.cassian.rrv.api.ReliableRecipeViewerPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.client.recipe.ClientRecipeManager;
import cc.cassian.rrv.common.recipe.ServerRecipeManager;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;
import com.molybdenum.alloyed.common.content.recipes.ModRecipes;

import java.util.HashMap;

public class AlloyedRRVPlugin implements ReliableRecipeViewerPlugin {

	@Override
	public void onIntegrationInitialize() {
		ServerRecipeManager.INSTANCE.synchronizeRecipeType(ModRecipes.SHAPELESS_FORGING_SERIALIZER.get(), ModRecipes.SHAPELESS_FORGING_TYPE.get());
		ServerRecipeManager.INSTANCE.synchronizeRecipeType(ModRecipes.SHAPED_FORGING_SERIALIZER.get(), ModRecipes.SHAPED_FORGING_TYPE.get());

		ItemView.addClientRecipeProvider(recipeList -> {
			ClientRecipeManager.INSTANCE.getRecipesForType(ModRecipes.SHAPELESS_FORGING_TYPE.get()).forEach(recipeHolder -> {
				var recipe = recipeHolder.value();
				recipeList.add(new ForgingClientRecipe(recipe.getIngredients(), recipe.getResultItem(), recipe.getCookTime()));
			});
			ClientRecipeManager.INSTANCE.getRecipesForType(ModRecipes.SHAPED_FORGING_TYPE.get()).forEach(recipeHolder -> {
				var recipe = recipeHolder.value();
				HashMap<Integer, SlotContent> ingredients = new HashMap<>();

				int i = 0;
				for (int y = 0; y < 3; y++) {
					for (int x = 0; x < 3; x++) {

						if (x >= recipe.getWidth() || y >= recipe.getHeight()) {
							continue;
						}

						if (recipe.getIngredients().get(i).isPresent())
							ingredients.put(x + y * 3, SlotContent.of(recipe.getIngredients().get(i).get()));

						i++;
					}
				}
				recipeList.add(new ForgingClientRecipe(recipe.getWidth(), recipe.getHeight(), ingredients, recipe.getResultItem(), recipe.getCookTime()));
			});
		});
	}
}
