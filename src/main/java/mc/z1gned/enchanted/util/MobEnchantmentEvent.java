package mc.z1gned.enchanted.util;

import mc.z1gned.enchanted.enchantment.ModEnchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.util.math.MathHelper;

public class MobEnchantmentEvent {
    public MobEnchantmentEvent() {
    }

    public static float getDamageAddition(float damage, MobEnchantmentAbility cap) {
        int level = MobEnchantmentUtil.getMobEnchantLevelFromHandler(cap.getMobEnchants(), ModEnchantments.STRONG);
        if (level > 0) {
            damage += 1.0F + (float) Math.max(0, level - 1);
        }

        return damage;
    }

    public static float getDamageReduction(float damage, MobEnchantmentAbility cap) {
        int i = MobEnchantmentUtil.getMobEnchantLevelFromHandler(cap.getMobEnchants(), ModEnchantments.PROTECTION);
        if (i > 0) {
            damage = (float) ((double) damage - (double) MathHelper.floor((double) damage * (double) ((float) i * 0.15F)));
        }

        return damage;
    }

    public static float extraDamage(LivingEntity entity, DamageSource source, float amount) {
        Entity var4 = source.getAttacker();
        if (var4 instanceof MobEnchantInterface ability) {
            Entity var5 = source.getAttacker();
            if (var5 instanceof LivingEntity) {
                if (source.getAttacker() != null && source.getAttacker() instanceof SnowGolemEntity && amount == 0.0F) {
                    amount = getDamageAddition(1.0F, ability.getEnchantAbility());
                } else if (amount > 0.0F) {
                    amount = getDamageAddition(amount, ability.getEnchantAbility());
                }
            }
        }

        if (entity instanceof MobEnchantInterface cap) {
            if (cap.getEnchantAbility().hasEnchant() && MobEnchantmentUtil.findMobEnchantFromHandler(cap.getEnchantAbility().getMobEnchants(), ModEnchantments.PROTECTION)) {
                amount = getDamageReduction(amount, cap.getEnchantAbility());
            }

            if (cap.getEnchantAbility().hasEnchant()) {
                amount = getBonusMobEnchantDamageReduction(amount, cap.getEnchantAbility());
            }
        }

        return amount;
    }

    public static float getBonusMobEnchantDamageReduction(float damage, MobEnchantmentAbility cap) {
        int i = cap.getMobEnchants().size();
        if (i > 0) {
            damage = (float) ((double) damage - (double) MathHelper.floor((double) damage * (double) ((float) i * 0.05F)));
        }

        return damage;
    }
}
