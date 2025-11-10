package deadlyenderdragon.mixin;

import deadlyenderdragon.utils.DragonUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity {

    @Unique
    final EnderDragonEntity self = (EnderDragonEntity) (Object) this;

    protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }


    @Redirect(method = "createEnderDragonAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;add(Lnet/minecraft/entity/attribute/EntityAttribute;D)Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;"))
    private static DefaultAttributeContainer.Builder changeHealth(DefaultAttributeContainer.Builder instance, EntityAttribute attributeinstance, double attribute) {
        return instance.add(EntityAttributes.GENERIC_MAX_HEALTH, DragonUtils.DRAGON_MAX_HEALTH).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, DragonUtils.DRAGON_ATTACK_DAMAGE);
    }

    @ModifyConstant(method = "tickWithEndCrystals", constant = @Constant(floatValue = 1, ordinal = 0))
    private float changeCrystalHealing(float constant) {
        return DragonUtils.getHealthRegeneration(self);
    }

    @ModifyConstant(method = "launchLivingEntities", constant = @Constant(floatValue = 5, ordinal = 0))
    private float changeDamageBody(float constant) {
        return (float) getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    @ModifyConstant(method = "damageLivingEntities", constant = @Constant(floatValue = 10, ordinal = 0))
    private float changeDamageHead(float constant) {
        return (float) getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    @ModifyConstant(method = "damagePart", constant = @Constant(floatValue = 4, ordinal = 0))
    private float changeDamageReduction(float constant) {
        return 2;
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
    private void changeMovementSpeed(EnderDragonEntity instance, float speed, Vec3d vec3d) {
        updateVelocity(DragonUtils.adjustSpeed(self, speed), vec3d);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;", ordinal = 0))
    private Box changeBoundingBoxRightWing(Box instance, double x, double y, double z) {
        var value = 2;
        return instance.expand(value, value, value);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;", ordinal = 1))
    private Box changeBoundingBoxLeftWing(Box instance, double x, double y, double z) {
        var value = 2;
        return instance.expand(value, value, value);
    }
}
