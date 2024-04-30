package ru.matt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class AntiPlaceHead implements ModInitializer {
	private static final KeyBinding TOGGLE_MOD_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Toggle Key",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_P,
			"AntiPlaceHead"
	));

	private boolean modEnabled = true;

	@Override
	public void onInitialize() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (!modEnabled) {
				return ActionResult.PASS;
			}
			if (player.getStackInHand(hand).getItem() == Blocks.PLAYER_HEAD.asItem()) {
				if (hand == Hand.MAIN_HAND) {
					return ActionResult.PASS;
				} else {
					return ActionResult.FAIL;
				}
			}
			return ActionResult.PASS;
		});
		ClientTickCallback.EVENT.register(client -> checkToggleKey());
	}

	public void checkToggleKey() {
		if (TOGGLE_MOD_KEY.wasPressed()) {
			modEnabled = !modEnabled;
			if (modEnabled) {
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("AntiPlaceHead §2включен"));
			} else {
				MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("AntiPlaceHead §4выключен"));
			}
		}
	}
}