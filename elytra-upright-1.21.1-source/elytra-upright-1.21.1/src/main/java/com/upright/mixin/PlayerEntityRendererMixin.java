package com.upright.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Removes the "lie flat" rotations the game applies while gliding,
 * so the body stays upright (Bedrock-style).
 */
@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

	@WrapOperation(
		method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V")
	)
	private void elytraUpright$skipGlideRotation(MatrixStack stack, Quaternionf rotation, Operation<Void> original,
			AbstractClientPlayerEntity player, MatrixStack matrices, float f, float g, float h, float i) {
		if (player.isFallFlying()) {
			return; // skip pitch/yaw tilt while gliding
		}
		original.call(stack, rotation);
	}
}
