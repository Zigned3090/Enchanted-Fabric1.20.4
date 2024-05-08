package mc.z1gned.enchanted.enchantment;

import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import mc.z1gned.enchanted.enchantment.mob.ProtectionMobEnchant;
import mc.z1gned.enchanted.enchantment.mob.StrongMobEnchant;
import mc.z1gned.enchanted.registry.ModRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static mc.z1gned.enchanted.ModMain.MOD_ID;
//和注册物品相似，正常注册模组的附魔
public class ModEnchantments {

    public static final MobEnchant PROTECTION = Registry.register(ModRegistries.MOB_ENCHANT, new Identifier(MOD_ID, "protection"), new ProtectionMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 5)));
    public static final MobEnchant STRONG = Registry.register(ModRegistries.MOB_ENCHANT, new Identifier(MOD_ID, "strong"), new StrongMobEnchant(new MobEnchant.Properties(MobEnchant.Rarity.COMMON, 5)));

    //空的方法，是个占位符,主要的注册方法是Registry.register(),在创建static对象的时候已经被调用了
    public static void registerEnchantments() {
    }

}
