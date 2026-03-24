package com.molybdenum.alloyed.client.screen;

import com.molybdenum.alloyed.Alloyed;
import com.molybdenum.alloyed.common.screen.ForgeMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class ForgeScreen extends AbstractContainerScreen<ForgeMenu> {
	private static final Identifier TEXTURE = Alloyed.asResource("textures/gui/forge_gui.png");

	public ForgeScreen(ForgeMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
		// Get the position where the GUI is to be drawn
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;

		// Render the background texture
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

		if (menu.isCrafting()) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 89, y + 17, 176, 14, menu.getScaledProgress(), 17, 256, 256);
		}

		if (menu.isFueled()) {
			float currentHeight = menu.getLitTime();
			int offset = (int) (15- currentHeight);
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x + 94, y + 36+offset, 176, offset, 14, (int) currentHeight, 256, 256);
		}
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
		this.extractBackground(guiGraphics, mouseX, mouseY, delta);
		super.extractRenderState(guiGraphics, mouseX, mouseY, delta);
		this.extractTooltip(guiGraphics, mouseX, mouseY);
	}

}