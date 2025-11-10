package deadlyenderdragon.mixin;

import deadlyenderdragon.utils.DragonUtils;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractSittingPhase;
import net.minecraft.entity.boss.dragon.phase.SittingFlamingPhase;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SittingFlamingPhase.class)
public abstract class SittingFlamingPhaseMixin extends AbstractSittingPhase {


    @Unique
    final SittingFlamingPhase self = (SittingFlamingPhase) (Object) this;

    public SittingFlamingPhaseMixin(EnderDragonEntity enderDragonEntity) {
        super(enderDragonEntity);
    }

    @ModifyConstant(method = "serverTick", constant = @Constant(intValue = 4, ordinal = 0))
    private int changeRepetitionAmount(int constant) {
        return 2;
    }

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/AreaEffectCloudEntity;addEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)V"))
    private void redirectAddEffect(AreaEffectCloudEntity instance, StatusEffectInstance effect) {
        instance.addEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, DragonUtils.getBreathAmplifier(dragon)));
    }
}
