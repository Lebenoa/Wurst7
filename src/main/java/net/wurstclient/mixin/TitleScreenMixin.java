/*
 * Copyright (c) 2014-2026 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.wurstclient.WurstClient;
import net.wurstclient.altmanager.screens.AltManagerScreen;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen
{
	private AbstractWidget realmsButton = null;
	private Button altsButton;
	
	private TitleScreenMixin(WurstClient wurst, Component title)
	{
		super(title);
	}
	
	/**
	 * Adds the AltManager button to the title screen. This mixin must not
	 * run in demo mode, as the Realms button doesn't exist there.
	 */
	@Inject(method = "createNormalMenuOptions(II)I", at = @At("RETURN"))
	private void onCreateNormalMenuOptions(int y, int spacingY,
		CallbackInfoReturnable<Integer> cir)
	{
		if(!WurstClient.INSTANCE.isEnabled())
			return;
		
		for(AbstractWidget button : Screens.getWidgets(this))
		{
			if(!button.getMessage().getString().equals(I18n.get("menu.online")))
				continue;
			
			realmsButton = button;
			break;
		}
		
		if(realmsButton == null)
			throw new IllegalStateException("Couldn't find realms button!");
		
		// make Realms button smaller
		realmsButton.setWidth(98);
		
		// add AltManager button to the left of the Realms button.
		// ModMenu puts its own button directly to the right of Realms,
		// so the right side is only safe when that slot is empty
		addRenderableWidget(altsButton = Button
			.builder(Component.literal("Alt Manager"),
				b -> minecraft.gui.setScreen(new AltManagerScreen(this,
					WurstClient.INSTANCE.getAltManager())))
			.bounds(realmsButton.getX(), realmsButton.getY(), 98, 20).build());
		positionAltsButton();
	}
	
	private void positionAltsButton()
	{
		int spacing = 2;
		
		// default: directly to the right of the Realms button
		int x = realmsButton.getX() + realmsButton.getWidth() + spacing;
		
		// if another button already occupies that spot (e.g. ModMenu's
		// button when ModMenu is installed), go to the left of Realms
		for(AbstractWidget button : Screens.getWidgets(this))
		{
			if(button == realmsButton || button == altsButton)
				continue;
			
			if(button.getY() == realmsButton.getY()
				&& Math.abs(button.getX() - x) < 98 + spacing)
			{
				x = realmsButton.getX() - 98 - spacing;
				break;
			}
		}
		
		altsButton.setX(x);
		altsButton.setY(realmsButton.getY());
	}
	
	@Inject(method = "tick()V", at = @At("RETURN"))
	private void onTick(CallbackInfo ci)
	{
		if(realmsButton == null || altsButton == null)
			return;
			
		// adjust AltManager button if Realms button has been moved
		// happens when ModMenu is installed
		positionAltsButton();
	}
	
	/**
	 * Stops the multiplayer button being grayed out if the user's Microsoft
	 * account is parental-control'd or banned from online play.
	 */
	@Inject(
		method = "getMultiplayerDisabledReason()Lnet/minecraft/network/chat/Component;",
		at = @At("HEAD"),
		cancellable = true)
	private void onGetMultiplayerDisabledReason(
		CallbackInfoReturnable<Component> cir)
	{
		cir.setReturnValue(null);
	}
}
