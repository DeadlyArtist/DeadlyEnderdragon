package deadlyenderdragon.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import deadlyenderdragon.utils.DragonUtils;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DragonFireballEntity.class)
public abstract class DragonFireballEntityMixin extends ExplosiveProjectileEntity {

    @Unique
    final DragonFireballEntity self = (DragonFireballEntity) (Object) this;

    protected DragonFireballEntityMixin(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    public double getSizeMultiplier() {
        return 1;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;DDD)V", at = @At("TAIL"))
    public void injectSpeedMultiplier(World world, LivingEntity owner, double directionX, double directionY, double directionZ, CallbackInfo ci) {
        speed *= DragonUtils.FIREBALL_SPEED_MULTIPLIER;
    }

    @Override
    public void tick() {
        super.tick();
        if (getRemovalReason() != null) return;

        var value = (DragonUtils.getPhase(self) - 1) * getSizeMultiplier();
        List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(value, value, value));
        if (list.isEmpty()) return;
        onCollision(new EntityHitResult(list.get(0)));
    }


    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void injectOnCollision(HitResult hitResult, CallbackInfo ci) {
        if (hitResult.getType() != HitResult.Type.ENTITY) return;
        var entity = ((EntityHitResult) hitResult).getEntity();
        if (entity instanceof DragonFireballEntity fireball || entity instanceof EnderDragonPart || entity instanceof EnderDragonEntity)
            ci.cancel();
    }

    @Redirect(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
    private Box changeBoxRadius(Box instance, double x, double y, double z) {
        var value = 5;
        return instance.expand(value, value, value);
    }

    @ModifyConstant(method = "onCollision", constant = @Constant(floatValue = 3, ordinal = 0))
    private float changeRadius(float constant, @Local AreaEffectCloudEntity area) {
        return (float) (4 * getSizeMultiplier());
    }

    @ModifyConstant(method = "onCollision", constant = @Constant(floatValue = 7, ordinal = 0))
    private float changeRadiusGrowth(float constant, @Local AreaEffectCloudEntity area) {
        return area.getRadius() * 2;
    }

    @ModifyConstant(method = "onCollision", constant = @Constant(intValue = 1, ordinal = 1))
    private int changeAmplifier(int constant) {
        return DragonUtils.getFireballAmplifier(self);
    }

    @ModifyConstant(method = "onCollision", constant = @Constant(doubleValue = 16, ordinal = 0))
    private double changeSquaredDistance(double constant) {
        return Math.pow(4 * getSizeMultiplier(), 2);
    }

    @Inject(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/AreaEffectCloudEntity;setPosition(DDD)V"))
    private void onHit(HitResult rayTraceResult, CallbackInfo ci) {
        self.getWorld().createExplosion(self.getOwner(), rayTraceResult.getPos().x, rayTraceResult.getPos().y, rayTraceResult.getPos().z, (float) (DragonUtils.getFireballDamage(self)), false, World.ExplosionSourceType.NONE);
    }
}