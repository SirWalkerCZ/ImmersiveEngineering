/*
 * BluSunrize
 * Copyright (c) 2021
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 *
 */

package blusunrize.immersiveengineering.client.render.tooltip;

import blusunrize.immersiveengineering.client.gui.RevolverScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;

public record RevolverClientTooltip(RevolverServerTooltip data) implements ClientTooltipComponent
{
	@Override
	public int getHeight()
	{
		return 40;
	}

	@Override
	public int getWidth(Font pFont)
	{
		return 40;
	}

	@Override
	public void renderImage(Font font, int mouseX, int mouseY, PoseStack transform, ItemRenderer itemRender)
	{
		transform.pushPose();
		transform.translate(mouseX, mouseY, 0);
		transform.scale(.5f, .5f, 1);
		RevolverScreen.drawExternalGUI(data.bullets(), data.bulletCount(), transform);
		transform.popPose();
	}
}
