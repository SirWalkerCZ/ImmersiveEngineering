/*
 * BluSunrize
 * Copyright (c) 2020
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 *
 */

package blusunrize.immersiveengineering.common.crafting.serializers;

import blusunrize.immersiveengineering.common.crafting.RevolverAssemblyRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;

public class RevolverAssemblyRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RevolverAssemblyRecipe>
{
	@Nonnull
	@Override
	public RevolverAssemblyRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		ShapedRecipe basic = RecipeSerializer.SHAPED_RECIPE.fromJson(recipeId, json);
		RevolverAssemblyRecipe recipe = new RevolverAssemblyRecipe(recipeId, basic.getGroup(), basic.getWidth(), basic.getHeight(),
				basic.getIngredients(), basic.getResultItem());
		if(GsonHelper.isValidNode(json, "copy_nbt"))
		{
			if(GsonHelper.isArrayNode(json, "copy_nbt"))
			{
				JsonArray jArray = GsonHelper.getAsJsonArray(json, "copy_nbt");
				int[] array = new int[jArray.size()];
				for(int i = 0; i < array.length; i++)
					array[i] = jArray.get(i).getAsInt();
				recipe.setNBTCopyTargetRecipe(array);
			}
			else
				recipe.setNBTCopyTargetRecipe(GsonHelper.getAsInt(json, "copy_nbt"));
		}
		return recipe;
	}

	@Nonnull
	@Override
	public RevolverAssemblyRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer)
	{
		ShapedRecipe basic = RecipeSerializer.SHAPED_RECIPE.fromNetwork(recipeId, buffer);
		RevolverAssemblyRecipe recipe = new RevolverAssemblyRecipe(recipeId, basic.getGroup(), basic.getWidth(), basic.getHeight(),
				basic.getIngredients(), basic.getResultItem());
		if(buffer.readBoolean())
			recipe.allowQuarterTurn();
		if(buffer.readBoolean())
			recipe.allowEighthTurn();
		int[] array = buffer.readVarIntArray();
		if(array.length > 0)
		{
			recipe.setNBTCopyTargetRecipe(array);
			if(buffer.readBoolean())
				recipe.setNBTCopyPredicate(buffer.readUtf(512));

		}
		return recipe;
	}

	@Override
	public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RevolverAssemblyRecipe recipe)
	{
		RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe.toVanilla());
		buffer.writeBoolean(recipe.isQuarterTurn());
		buffer.writeBoolean(recipe.isEightTurn());
		int[] copying = recipe.getCopyTargets();
		if(copying==null)
			copying = new int[0];
		buffer.writeVarIntArray(copying);
		if(copying.length > 0)
		{
			if(recipe.hasCopyPredicate())
			{
				buffer.writeBoolean(true);
				buffer.writeUtf(recipe.getBufferPredicate());
			}
			else
				buffer.writeBoolean(false);
		}
	}
}