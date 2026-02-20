package com.molybdenum.alloyed.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.molybdenum.alloyed.common.entity.ai.goal.FoxSitOnBlockGoal;
import com.molybdenum.alloyed.common.util.Platform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal {
	protected FoxMixin(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Inject(method = "registerGoals", at = @At(value = "TAIL"))
	private void foxesSitOnForges(CallbackInfo ci) {
		var fox = (Fox) (Object) this;
		this.goalSelector.addGoal(7, new FoxSitOnBlockGoal(fox, 0.8));
	}
}
