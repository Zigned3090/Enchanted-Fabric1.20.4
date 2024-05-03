package mc.z1gned.enchanted.util;

import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import mc.z1gned.enchanted.registry.ModRegistries;
import net.minecraft.nbt.NbtCompound;

public class MobEnchantmentHandler {
    private MobEnchant mobEnchant;
    private int enchantLevel;

    public MobEnchantmentHandler(MobEnchant mobEnchant, int enchantLevel) {
        this.mobEnchant = mobEnchant;
        this.enchantLevel = enchantLevel;
    }

    public MobEnchant getMobEnchant() {
        return this.mobEnchant;
    }

    public int getEnchantLevel() {
        return this.enchantLevel;
    }

    public NbtCompound writeNBT() {
        NbtCompound nbt = new NbtCompound();
        if (this.mobEnchant != null) {
            nbt.putString(MobEnchantmentUtil.MOB_ENCHANTMENTS, ModRegistries.MOB_ENCHANT.getId(this.mobEnchant).toString());
            nbt.putInt(MobEnchantmentUtil.LVL, this.enchantLevel);
        }

        return nbt;
    }

    public void readNBT(NbtCompound nbt) {
        this.mobEnchant = MobEnchantmentUtil.getEnchantFromNBT(nbt);
        this.enchantLevel = MobEnchantmentUtil.getEnchantLevelFromNBT(nbt);
    }
}
