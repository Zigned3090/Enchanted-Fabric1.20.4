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
        //注册Item
        ModItems.registerItem();
        //注册ItemGroup
        ModItemGroup.registerItemGroup();
        //注册生物的Enchantments，要看ModRegistries.registerRegistries()这一行
        ModEnchantments.registerEnchantments();
        //注册数据追踪器，这个需要好好研究研究🧐
        ModTrackedData.registerDataTrackers();
        //注册注册器，这个也需要研究研究🧐
        ModRegistries.registerRegistries();
    }
}