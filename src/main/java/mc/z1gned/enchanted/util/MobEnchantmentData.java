package mc.z1gned.enchanted.util;

import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import net.minecraft.util.collection.Weighted;

public class MobEnchantmentData extends Weighted.Absent {
    public final MobEnchant enchantment;
    public final int enchantmentLevel;

    public MobEnchantmentData(MobEnchant enchantment, int level) {
        super(enchantment.getRarity().getWeight());
        this.enchantment = enchantment;
        this.enchantmentLevel = level;
    }
}
