package mc.z1gned.enchanted.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static mc.z1gned.enchanted.ModMain.MOD_ID;

public class ModItems {

    public static final Item ENCHANT_EMERALD = new EnchantEmeraldIItem(new FabricItemSettings());
    public static final Item FILTHY_EMERALD = new FilthyEmeraldItem(new FabricItemSettings());

    public static void registerItem() {
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "enchant_emerald"), ENCHANT_EMERALD);
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "filthy_emerald"), FILTHY_EMERALD);
    }

}
