package deadlyenderdragon.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

public class DragonUtils {
    public static int DRAGON_MAX_HEALTH = 300;
    public static int DEFAULT_DRAGON_ATTACK_DAMAGE = 10;
    public static int DRAGON_ATTACK_DAMAGE = 10;
    public static int PERCENT_REGENERATED_BY_CRYSTAL = 1;
    public static int HIGHEST_PHASE = 3;

    public static int MAXIMUM_FIREBALL_ANGLE = 70;
    public static float FIREBALL_SPEED_MULTIPLIER = 1.5f;

    public static double SECOND_PHASE_PERCENT = 0.66;
    public static double THIRD_PHASE_PERCENT = 0.33;

    public static int getPhase(EnderDragonEntity dragon) {
        if (dragon.getHealth() / dragon.getMaxHealth() < THIRD_PHASE_PERCENT) return 3;
        if (dragon.getHealth() / dragon.getMaxHealth() < SECOND_PHASE_PERCENT) return 2;
        return 1;
    }

    public static int getHealthRegeneration(EnderDragonEntity dragon) {
        return (int) (dragon.getMaxHealth() / 100 * PERCENT_REGENERATED_BY_CRYSTAL);
    }

    public static int getPhase(DragonFireballEntity entity) {
        var owner = entity.getOwner();
        if (!(owner instanceof EnderDragonEntity dragon)) return 1;
        return getPhase(dragon);
    }
    public static float adjustSpeed(EnderDragonEntity dragon, float speed) {
        return (float) (speed * (1 + 0.2 * (DragonUtils.getPhase(dragon) - 1)));
    }

    public static int getBreathAmplifier(EnderDragonEntity dragon) {
        return Math.max(1, (int) (dragon.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / DEFAULT_DRAGON_ATTACK_DAMAGE));
    }

    public static int getFireballAmplifier(DragonFireballEntity fireball) {
        var owner = fireball.getOwner();
        if (owner instanceof EnderDragonEntity dragon) {
            return Math.max(1, (int) (dragon.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / DEFAULT_DRAGON_ATTACK_DAMAGE));
        }
        return 1;
    }

    public static int getFireballDamage(DragonFireballEntity fireball) {
        var owner = fireball.getOwner();
        if (owner instanceof EnderDragonEntity dragon) {
            return Math.max(1, (int) (dragon.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) / 10));
        }
        return 5;
    }

    public static int getTicksBetweenShots(EnderDragonEntity dragon) {
        return 70 - DragonUtils.getPhase(dragon) * 10;
    }

    public static int getNumberOfStrafeShots(EnderDragonEntity dragon) {
        return 10 + 3 * DragonUtils.getPhase(dragon);
    }


    public static DragonFireballEntity tryShootFireball(EnderDragonEntity dragon, LivingEntity target) {
        if (target == null || !dragon.canSee(target)) return null;

        Vec3d vec3d = new Vec3d(target.getX() - dragon.getX(), 0.0, target.getZ() - dragon.getZ()).normalize();
        Vec3d vec3d2 = new Vec3d(
                (double) MathHelper.sin(dragon.getYaw() * (float) (Math.PI / 180.0)),
                0.0,
                (double) (-MathHelper.cos(dragon.getYaw() * (float) (Math.PI / 180.0)))
        )
                .normalize();
        float j = (float) vec3d2.dotProduct(vec3d);
        float k = (float) (Math.acos((double) j) * 180.0F / (float) Math.PI);
        k += 0.5F;
        if (k < 0.0F || k >= MAXIMUM_FIREBALL_ANGLE) return null;

        double h = 1.0;
        Vec3d vec3d3 = dragon.getRotationVec(1.0F);
        double l = dragon.head.getX() - vec3d3.x;
        double m = dragon.head.getBodyY(0.5) + 0.5;
        double n = dragon.head.getZ() - vec3d3.z;
        double o = target.getX() - l;
        double p = target.getBodyY(0.5) - m;
        double q = target.getZ() - n;
        if (!dragon.isSilent()) {
            dragon.getWorld().syncWorldEvent(null, WorldEvents.ENDER_DRAGON_SHOOTS, dragon.getBlockPos(), 0);
        }

        DragonFireballEntity dragonFireballEntity = new DragonFireballEntity(dragon.getWorld(), dragon, o, p, q);
        dragonFireballEntity.refreshPositionAndAngles(l, m, n, 0.0F, 0.0F);
        dragon.getWorld().spawnEntity(dragonFireballEntity);
        return dragonFireballEntity;
    }
}
