package com.upright.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla forces the head to look up 45 degrees while gliding (because the body is horizontal).
 * With an upright body, let the head follow the real look pitch instead.
 */
@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin {
	@Shadow @Final public ModelPart head;
	@Shadow @Final public ModelPart hat;

	@Inject(
		method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
		at = @At("TAIL")
	)
	private void elytraUpright$headPitch(LivingEntity entity, float limbAngle, float limbDistance,
			float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
		if (entity.getFallFlyingTicks() > 4) {
			this.head.pitch = headPitch * ((float) Math.PI / 180F);
			this.hat.copyTransform(this.head);
		}
	}
}
