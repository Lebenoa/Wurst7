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
import net.minecraft.world.phys.Vec3;
import net.wurstclient.Category;
import net.wurstclient.SearchTags;
import net.wurstclient.events.HandleInputListener;
import net.wurstclient.hack.Hack;
import net.wurstclient.settings.CheckboxSetting;
import net.wurstclient.settings.FaceTargetSetting;
import net.wurstclient.settings.FaceTargetSetting.FaceTarget;
import net.wurstclient.settings.SliderSetting;
import net.wurstclient.settings.SliderSetting.ValueDisplay;
import net.wurstclient.settings.InteractSwingSetting;
import net.wurstclient.settings.InteractSwingSetting.InteractSwing;
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
	
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	private final CheckboxSetting checkLOS =
		new CheckboxSetting("Check line of sight",
			"description.wurst.setting.plantaura.check_line_of_sight", false);
	
	private final FaceTargetSetting faceTarget =
		FaceTargetSetting.withoutPacketSpam(this, FaceTarget.SERVER);
	
	private final InteractSwingSetting interactSwing =
		new InteractSwingSetting(this, InteractSwing.SERVER);
	
	public PlantAuraHack()
	{
		super("PlantAura");
		setCategory(Category.BLOCKS);
		addSetting(range);
		addSetting(checkLOS);
		addSetting(faceTarget);
		addSetting(interactSwing);
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
		
		Item heldSeed = getHeldSeed();
		if(heldSeed == null)
			return;
		
		InteractionHand hand = MC.player.getMainHandItem().is(heldSeed)
			? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		
		List<BlockPos> farmland = getFarmlandSpots();
		for(BlockPos pos : farmland)
		{
			BlockPlacingParams params = BlockPlacer.getBlockPlacingParams(pos);
			if(params == null || params.distanceSq() > range.getValueSq()
				|| params.requiresSneaking())
				continue;
			
			if(checkLOS.isChecked() && !params.lineOfSight())
				continue;
			
			MC.rightClickDelay = 4;
			faceTarget.face(params.hitVec());
			InteractionSimulator.rightClickBlock(params.toHitResult(), hand,
				interactSwing.getSelected());
			return;
		}
	}
	
	private Item getHeldSeed()
	{
		if(SEEDS.contains(MC.player.getMainHandItem().getItem()))
			return MC.player.getMainHandItem().getItem();
		if(SEEDS.contains(MC.player.getOffhandItem().getItem()))
			return MC.player.getOffhandItem().getItem();
		return null;
	}
	
	private List<BlockPos> getFarmlandSpots()
	{
		Vec3 eyesVec = MC.player.getEyePosition();
		BlockPos eyesBlock = BlockPos.containing(eyesVec);
		double rangeSq = range.getValueSq();
		int blockRange = range.getValueCeil();
		
		return BlockUtils.getAllInBoxStream(eyesBlock, blockRange)
			.filter(pos -> pos.distToCenterSqr(eyesVec) <= rangeSq)
			.filter(pos -> BlockUtils.getBlock(pos) == Blocks.FARMLAND)
			.filter(pos -> BlockUtils.getState(pos.above()).isAir())
			.map(BlockPos::above)
			.sorted(
				Comparator.comparingDouble(pos -> pos.distToCenterSqr(eyesVec)))
			.toList();
	}
}
