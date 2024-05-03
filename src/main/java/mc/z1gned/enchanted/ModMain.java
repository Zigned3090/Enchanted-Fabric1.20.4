package mc.z1gned.enchanted;

import mc.z1gned.enchanted.data.ModTrackedData;
import mc.z1gned.enchanted.enchantment.ModEnchantments;
import mc.z1gned.enchanted.group.ModItemGroup;
import mc.z1gned.enchanted.item.ModItems;
import mc.z1gned.enchanted.registry.ModRegistries;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModMain implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("enchanted");
    public static final String MOD_ID = "enchanted";

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        ModItems.registerItem();
        ModItemGroup.registerItemGroup();
        ModEnchantments.registerEnchantments();
        ModTrackedData.registerDataTrackers();
        ModRegistries.registerRegistries();
    }
}