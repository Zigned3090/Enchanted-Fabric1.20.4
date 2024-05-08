package mc.z1gned.enchanted.registry;

import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModRegistryKeys {

    //这是RegistryKey的实例，RegistryKey和Registry我都不晓得是干啥的😭
    public static final RegistryKey<Registry<MobEnchant>> MOB_ENCHANTMENT = ModRegistryKeys.of("mob_enchantment");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(new Identifier(id));//Identifier是各种资源的唯一标识符，包括物品、方块、实体、声音等等
    }

}
