/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.hacks;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.HandleInputListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.util.BlockPlacer;
import net.wurstclient.util.BlockPlacer.BlockPlacingParams;
import net.wurstclient.util.BlockUtils;
import net.wurstclient.util.InteractionSimulator;

@SearchTags({"plant aura", "plant seeds", "auto plant"})
public final class PlantAuraHack extends Hack implements HandleInputListener
{
	private static final Set<Item> SEEDS = Set.of(Items.WHEAT_SEEDS,
		Items.BEETROOT_SEEDS, Items.PUMPKIN_SEEDS, Items.MELON_SEEDS,
		Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD, Items.CARROT, Items.POTATO);
	
	public PlantAuraHack()
	{
		super("PlantAura");
		setCategory(Category.BLOCKS);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(HandleInputListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(HandleInputListener.class, this);
	}
	
	@Override
	public void onHandleInput()
	{
		if(MC.rightClickDelay > 0 || MC.gameMode.isDestroying()
			|| MC.player.isHandsBusy())
			return;
		
		InteractionHand hand = getSeedHand();
		if(hand == null)
			return;
		
		BlockPos eyesBlock = BlockPos.containing(MC.player.getEyePosition());
		int blockRange = 5;
		double rangeSq = blockRange * blockRange;
		List<BlockPos> farmland =
			BlockUtils.getAllInBoxStream(eyesBlock, blockRange)
				.filter(pos -> pos
					.distToCenterSqr(MC.player.getEyePosition()) <= rangeSq)
				.filter(pos -> BlockUtils.getBlock(pos) == Blocks.FARMLAND)
				.filter(pos -> BlockUtils.getState(pos.above()).isAir())
				.map(BlockPos::above)
				.sorted(Comparator.comparingDouble(
					pos -> pos.distToCenterSqr(MC.player.getEyePosition())))
				.toList();
		
		for(BlockPos pos : farmland)
		{
			BlockPlacingParams params = BlockPlacer.getBlockPlacingParams(pos);
			if(params == null || params.distanceSq() > rangeSq
				|| params.requiresSneaking() || !params.lineOfSight())
				continue;
			
			MC.rightClickDelay = 4;
			InteractionSimulator.rightClickBlock(params.toHitResult(), hand);
			return;
		}
	}
	
	private InteractionHand getSeedHand()
	{
		if(SEEDS.contains(MC.player.getMainHandItem().getItem()))
			return InteractionHand.MAIN_HAND;
		if(SEEDS.contains(MC.player.getOffhandItem().getItem()))
			return InteractionHand.OFF_HAND;
		return null;
	}
}
