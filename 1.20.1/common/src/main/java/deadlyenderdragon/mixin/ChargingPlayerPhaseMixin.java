package deadlyenderdragon.mixin;

import deadlyenderdragon.mixinInterfaces.IChargingPlayerPhaseMixin;
import deadlyenderdragon.utils.DragonUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.ChargingPlayerPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChargingPlayerPhase.class)
public abstract class ChargingPlayerPhaseMixin extends AbstractPhase implements IChargingPlayerPhaseMixin {

    @Unique
    final ChargingPlayerPhase self = (ChargingPlayerPhase) (Object) this;

    @Unique
    LivingEntity target;

    @Unique
    int numberOfTimesShot = 0;

    @Unique
    int ticksSinceLastShot = 0;

    public ChargingPlayerPhaseMixin(EnderDragonEntity dragon) {
        super(dragon);
        numberOfTimesShot = 0;
    }

    @Inject(method = "beginPhase", at = @At("HEAD"))
    private void injectBeginPhase(CallbackInfo ci) {
        numberOfTimesShot = 0;
    }

    @Inject(method = "serverTick", at = @At("HEAD"))
    private void injectServerTarget(CallbackInfo ci) {
        if (self.getPathTarget() == null) return;
        ticksSinceLastShot++;

        if (ticksSinceLastShot <= DragonUtils.getTicksBetweenShots(dragon)) return;
        var fireball = DragonUtils.tryShootFireball(dragon, target);
        if (fireball != null) {
            //PComponents.DRAGON_FIREBALL.get(fireball).sizeMultiplier = 2;
            numberOfTimesShot++;
            ticksSinceLastShot = 0;
        }
    }

    @Override
    public void setTarget(LivingEntity entity) {
        target = entity;
    }
}
