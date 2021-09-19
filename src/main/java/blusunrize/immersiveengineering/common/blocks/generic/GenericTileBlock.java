/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.blocks.generic;

import blusunrize.immersiveengineering.common.blocks.BlockItemIE;
import blusunrize.immersiveengineering.common.blocks.IETileProviderBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class GenericTileBlock<T extends BlockEntity> extends IETileProviderBlock
{
	private final RegistryObject<BlockEntityType<T>> tileType;

	public GenericTileBlock(String name, RegistryObject<BlockEntityType<T>> tileType, Properties blockProps)
	{
		this(name, tileType, blockProps, BlockItemIE::new);
	}

	public GenericTileBlock(String name, RegistryObject<BlockEntityType<T>> tileType, Properties blockProps,
							BiFunction<Block, Item.Properties, Item> itemBlock)
	{
		super(name, blockProps, itemBlock);
		this.tileType = tileType;
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(@Nonnull BlockState state, @Nonnull BlockGetter world)
	{
		return tileType.get().create();
	}
}
