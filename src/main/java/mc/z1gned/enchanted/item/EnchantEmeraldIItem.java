package mc.z1gned.enchanted.item;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import mc.z1gned.enchanted.registry.ModRegistries;
import mc.z1gned.enchanted.util.MobEnchantInterface;
import mc.z1gned.enchanted.util.MobEnchantmentUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

//附魔物品
public class EnchantEmeraldIItem extends Item {
    //构造函数
    public EnchantEmeraldIItem(Settings settings) {
        super(settings);
    }
    //
    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (MobEnchantmentUtil.hasMobEnchant(stack)) {
            boolean[] flag = new boolean[]{false};
            if (entity instanceof MobEnchantInterface) {
                flag[0] = MobEnchantmentUtil.addItemMobEnchantToEntity(stack, entity, ((MobEnchantInterface)entity).getEnchantAbility());
            }

            if (flag[0]) {
                if (!user.isCreative()) {
                    stack.decrement(1);
                }
                user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL;
            }
        } else {
            return super.useOnEntity(stack, user, entity, hand);
        }
    }

    public TypedActionResult<ItemStack> use(World level, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (MobEnchantmentUtil.hasMobEnchant(stack)) {
            boolean[] flag = new boolean[] {false};
            if (user instanceof MobEnchantInterface) {
                flag[0] = MobEnchantmentUtil.addItemMobEnchantToEntity(stack, user, ((MobEnchantInterface) user).getEnchantAbility());
            }

            if (flag[0]) {
                if (!user.isCreative()) {
                    stack.decrement(1);
                }
                user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
                return TypedActionResult.success(stack);
            } else {
                return TypedActionResult.fail(stack);
            }
        } else {
            return super.use(level, user, hand);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World level, List<Text> tooltip, TooltipContext p_41424_) {
        super.appendTooltip(stack, level, tooltip, p_41424_);
        if (MobEnchantmentUtil.hasMobEnchant(stack)) {
            NbtList nbtList = MobEnchantmentUtil.getEnchantmentListForNBT(stack.getNbt());

            for(int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                MobEnchant mobEnchant = MobEnchantmentUtil.getEnchantFromNBT(nbtCompound);
                int enchantmentLevel = MobEnchantmentUtil.getEnchantLevelFromNBT(nbtCompound);
                if (mobEnchant != null) {
                    Formatting[] textFormatting = new Formatting[]{Formatting.AQUA};
                    Identifier identifier = ModRegistries.MOB_ENCHANT.getId(mobEnchant);
                    String var10001 = identifier.getNamespace();
                    tooltip.add(Text.translatable("mob_enchant." + var10001 + "." + identifier.getPath()).formatted(textFormatting).append(" ").append(Text.translatable("enchantment.level." + enchantmentLevel).formatted(textFormatting)));
                }
            }

            List<com.mojang.datafixers.util.Pair<EntityAttribute, EntityAttributeModifier>> list1 = Lists.newArrayList();

            for(int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                MobEnchant mobEnchant = MobEnchantmentUtil.getEnchantFromNBT(nbtCompound);
                int mobEnchantLevel = MobEnchantmentUtil.getEnchantLevelFromNBT(nbtCompound);
                if (mobEnchant != null) {
                    Map<EntityAttribute, EntityAttributeModifier> map = mobEnchant.getAttributeModifierMap();
                    if (!map.isEmpty()) {
                        Iterator var13 = map.entrySet().iterator();

                        while(var13.hasNext()) {
                            Map.Entry<EntityAttribute, EntityAttributeModifier> entry = (Map.Entry)var13.next();
                            EntityAttributeModifier attributeModifier = entry.getValue();
                            EntityAttributeModifier attributeModifier1 = new EntityAttributeModifier(attributeModifier.toString(), mobEnchant.getAttributeModifierAmount(mobEnchantLevel, attributeModifier), attributeModifier.getOperation());
                            list1.add(new com.mojang.datafixers.util.Pair(entry.getKey(), attributeModifier1));
                        }
                    }
                }
            }

            if (!list1.isEmpty()) {
                Iterator var19 = list1.iterator();

                while(var19.hasNext()) {
                    com.mojang.datafixers.util.Pair<EntityAttribute, EntityAttributeModifier> pair = (Pair)var19.next();
                    EntityAttributeModifier attributeModifier2 = pair.getSecond();
                    double d0 = attributeModifier2.getValue();
                    double d1;
                    if (attributeModifier2.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && attributeModifier2.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
                        d1 = attributeModifier2.getValue();
                    } else {
                        d1 = attributeModifier2.getValue() * 100.0;
                    }

                    if (d0 > 0.0) {
                        tooltip.add(Text.translatable("attribute.modifier.plus." + attributeModifier2.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d1), Text.translatable(pair.getFirst().getTranslationKey())).formatted(Formatting.BLUE));
                    } else if (d0 < 0.0) {
                        d1 *= -1.0;
                        tooltip.add(Text.translatable("attribute.modifier.take." + attributeModifier2.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(d1), Text.translatable(pair.getFirst().getTranslationKey())).formatted(Formatting.RED));
                    }
                }
            }
        }

    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    public static void generateMobEnchantmentBookTypesOnlyMaxLevel(ItemGroup.Entries output, ItemGroup.StackVisibility tabVisibility) {
        Iterator var2 = ModRegistries.MOB_ENCHANT.iterator();

        while(var2.hasNext()) {
            MobEnchant mobEnchant = (MobEnchant)var2.next();
            if (!mobEnchant.isDisabled()) {
                ItemStack stack = new ItemStack(ModItems.ENCHANT_EMERALD);
                MobEnchantmentUtil.addMobEnchantToItemStack(stack, mobEnchant, mobEnchant.getMaxLevel());
                output.add(stack, tabVisibility);
            }
        }

    }

}
