package mc.z1gned.enchanted.group;

import mc.z1gned.enchanted.item.EnchantEmeraldIItem;
import mc.z1gned.enchanted.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.stream.Stream;

import static mc.z1gned.enchanted.ModMain.MOD_ID;

//正常的ItemGroup类
public class ModItemGroup {
    private static final List<ItemStack> GENERAL_ITEM = Stream.of(ModItems.ENCHANT_EMERALD).map(ItemStack::new).toList();

    public static ItemGroup ITEM_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "item"),
            FabricItemGroup.builder().displayName(Text.translatable("enchanted.group.item")).icon(() -> new ItemStack(ModItems.FILTHY_EMERALD)).entries((displayContext, entries) -> {
                entries.add(ModItems.FILTHY_EMERALD);
                entries.addAll(GENERAL_ITEM);
                EnchantEmeraldIItem.generateMobEnchantmentBookTypesOnlyMaxLevel(entries, ItemGroup.StackVisibility.PARENT_TAB_ONLY);
            }).build());
//空的方法，是个占位符
    public static void registerItemGroup() {}

}
