package mc.z1gned.enchanted.registry;

import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import static mc.z1gned.enchanted.ModMain.MOD_ID;

public class ModRegistries {
    public static final Registry<MobEnchant> MOB_ENCHANT = FabricRegistryBuilder.createDefaulted(ModRegistryKeys.MOB_ENCHANTMENT, new Identifier(MOD_ID, "protection")).buildAndRegister();

    public static void registerRegistries() {}

}
