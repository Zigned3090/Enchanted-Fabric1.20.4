package mc.z1gned.enchanted.registry;

import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class ModRegistryKeys {

    public static final RegistryKey<Registry<MobEnchant>> MOB_ENCHANTMENT = ModRegistryKeys.of("mob_enchantment");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(new Identifier(id));
    }

}
