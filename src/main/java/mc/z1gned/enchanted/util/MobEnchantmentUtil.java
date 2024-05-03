package mc.z1gned.enchanted.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import mc.z1gned.enchanted.item.ModItems;
import mc.z1gned.enchanted.registry.ModRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class MobEnchantmentUtil {
    public static final String MOB_ENCHANTMENTS = "id";
    public static final String LVL = "lvl";
    public static final String STORED_MOB_ENCHANTS = "StoredMobEnchantments";

    public MobEnchantmentUtil() {
    }

    public static @Nullable MobEnchant getEnchantFromNBT(@Nullable NbtCompound tag) {
        return tag != null && ModRegistries.MOB_ENCHANT.containsId(Identifier.tryParse(tag.getString(MOB_ENCHANTMENTS))) ? ModRegistries.MOB_ENCHANT.get(Identifier.tryParse(tag.getString(MOB_ENCHANTMENTS))) : null;
    }

    public static int getEnchantLevelFromNBT(@Nullable NbtCompound tag) {
        return tag != null ? tag.getInt(LVL) : 0;
    }

    public static @Nullable MobEnchant getEnchantFromString(@Nullable String id) {
        return id != null && ModRegistries.MOB_ENCHANT.containsId(Identifier.tryParse(id)) ? ModRegistries.MOB_ENCHANT.get(Identifier.tryParse(id)) : null;
    }

    public static @Nullable MobEnchant getEnchantFromResourceLocation(@Nullable Identifier id) {
        return id != null && ModRegistries.MOB_ENCHANT.containsId(id) ? ModRegistries.MOB_ENCHANT.get(id) : null;
    }

    public static boolean hasMobEnchant(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.contains(STORED_MOB_ENCHANTS);
    }

    public static NbtList getEnchantmentListForNBT(NbtCompound nbtCompound) {
        return nbtCompound != null ? nbtCompound.getList(STORED_MOB_ENCHANTS, 10) : new NbtList();
    }

    public static Map<MobEnchant, Integer> getEnchantments(ItemStack stack) {
        NbtList nbtList = getEnchantmentListForNBT(stack.getNbt());
        return makeMobEnchantListFromListNBT(nbtList);
    }

    public static void setEnchantments(Map<MobEnchant, Integer> enchMap, ItemStack stack) {
        NbtList listnbt = new NbtList();
        Iterator var3 = enchMap.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<MobEnchant, Integer> entry = (Map.Entry)var3.next();
            MobEnchant enchantment = entry.getKey();
            if (enchantment != null) {
                int i = entry.getValue();
                NbtCompound compoundnbt = new NbtCompound();
                compoundnbt.putString(MOB_ENCHANTMENTS, String.valueOf(ModRegistries.MOB_ENCHANT.getId(enchantment)));
                compoundnbt.putShort(LVL, (short)i);
                listnbt.add(compoundnbt);
                if (stack.getItem() == ModItems.ENCHANT_EMERALD) {
                    addMobEnchantToItemStack(stack, enchantment, i);
                }
            }
        }

        if (listnbt.isEmpty()) {
            stack.removeSubNbt(STORED_MOB_ENCHANTS);
        }

    }

    private static Map<MobEnchant, Integer> makeMobEnchantListFromListNBT(NbtList nbtList) {
        Map<MobEnchant, Integer> map = Maps.newLinkedHashMap();

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            MobEnchant mobEnchant = getEnchantFromString(nbtCompound.getString(MOB_ENCHANTMENTS));
            map.put(mobEnchant, nbtCompound.getInt(LVL));
        }

        return map;
    }

    public static void addMobEnchantToItemStack(ItemStack itemIn, MobEnchant mobenchant, int level) {
        NbtList nbtList = getEnchantmentListForNBT(itemIn.getNbt());
        boolean flag = true;
        Identifier identifier = ModRegistries.MOB_ENCHANT.getId(mobenchant);

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Identifier identifier1 = Identifier.tryParse(nbtCompound.getString(MOB_ENCHANTMENTS));
            if (identifier1 != null && identifier1.equals(identifier)) {
                if (nbtCompound.getInt(LVL) < level) {
                    nbtCompound.putInt(LVL, level);
                }

                flag = false;
                break;
            }
        }

        if (flag) {
            NbtCompound nbtCompound1 = new NbtCompound();
            nbtCompound1.putString(MOB_ENCHANTMENTS, String.valueOf(identifier));
            nbtCompound1.putInt(LVL, level);
            nbtList.add(nbtCompound1);
        }

        itemIn.getOrCreateNbt().put(STORED_MOB_ENCHANTS, nbtList);
    }

    public static boolean addItemMobEnchantToEntity(ItemStack itemIn, LivingEntity entity, MobEnchantmentAbility ability) {
        NbtList nbtList = getEnchantmentListForNBT(itemIn.getNbt());
        boolean flag = false;

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            if (checkAllowMobEnchantFromMob(getEnchantFromNBT(nbtCompound), entity, ability)) {
                ability.addMobEnchant(entity, getEnchantFromNBT(nbtCompound), getEnchantLevelFromNBT(nbtCompound));
                flag = true;
            }
        }

        return flag;
    }

    public static void removeMobEnchantToEntity(LivingEntity entity, MobEnchantmentAbility ability) {
        ability.removeAllMobEnchant(entity);
    }

    public static int getExperienceFromMob(MobEnchantmentAbility cap) {
        int l = 0;

        MobEnchant enchantment;
        int integer;
        for(Iterator var2 = cap.getMobEnchants().iterator(); var2.hasNext(); l += enchantment.getMinEnchantability(integer)) {
            MobEnchantmentHandler list = (MobEnchantmentHandler)var2.next();
            enchantment = list.getMobEnchant();
            integer = list.getEnchantLevel();
        }

        return l;
    }

    public static boolean addRandomEnchantmentToEntity(LivingEntity livingEntity, MobEnchantmentAbility ability, Random random, int level, boolean allowRare) {
        List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowRare);
        boolean flag = false;
        Iterator var7 = list.iterator();

        while(var7.hasNext()) {
            MobEnchantmentData enchantmentData = (MobEnchantmentData)var7.next();
            if (checkAllowMobEnchantFromMob(enchantmentData.enchantment, livingEntity, ability)) {
                ability.addMobEnchant(livingEntity, enchantmentData.enchantment, enchantmentData.enchantmentLevel);
                flag = true;
            }
        }

        return flag;
    }

    public static boolean addUnstableRandomEnchantmentToEntity(LivingEntity livingEntity, LivingEntity ownerEntity, MobEnchantmentAbility ability, Random random, int level, boolean allowRare) {
        List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowRare);
        boolean flag = false;
        Iterator var8 = list.iterator();

        while(var8.hasNext()) {
            MobEnchantmentData enchantmentData = (MobEnchantmentData)var8.next();
            if (checkAllowMobEnchantFromMob(enchantmentData.enchantment, livingEntity, ability)) {
                ability.addMobEnchantFromOwner(livingEntity, enchantmentData.enchantment, enchantmentData.enchantmentLevel, ownerEntity);
                flag = true;
            }
        }

        if (flag) {
            ability.addOwner(livingEntity, ownerEntity);
        }

        return flag;
    }

    public static ItemStack addRandomEnchantmentToItemStack(Random random, ItemStack stack, int level, boolean allowRare) {
        List<MobEnchantmentData> list = buildEnchantmentList(random, level, allowRare);
        Iterator var5 = list.iterator();

        while(var5.hasNext()) {
            MobEnchantmentData enchantmentData = (MobEnchantmentData)var5.next();
            if (!enchantmentData.enchantment.isDisabled()) {
                addMobEnchantToItemStack(stack, enchantmentData.enchantment, enchantmentData.enchantmentLevel);
            }
        }

        return stack;
    }

    public static boolean findMobEnchantmentHandler(List<MobEnchantmentHandler> list, MobEnchant findMobEnchant) {
        Iterator var2 = list.iterator();

        MobEnchantmentHandler mobEnchant;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            mobEnchant = (MobEnchantmentHandler)var2.next();
        } while(!mobEnchant.getMobEnchant().equals(findMobEnchant));

        return true;
    }

    public static boolean findMobEnchant(List<MobEnchant> list, MobEnchant findMobEnchant) {
        return list.contains(findMobEnchant);
    }

    public static boolean findMobEnchantFromHandler(List<MobEnchantmentHandler> list, MobEnchant findMobEnchant) {
        Iterator var2 = list.iterator();

        MobEnchantmentHandler mobEnchant;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            mobEnchant = (MobEnchantmentHandler)var2.next();
        } while(mobEnchant == null || findMobEnchant.isDisabled() || !mobEnchant.getMobEnchant().equals(findMobEnchant));

        return true;
    }

    public static boolean checkAllowMobEnchantFromMob(@Nullable MobEnchant mobEnchant, LivingEntity livingEntity, MobEnchantmentAbility ability) {
        if (mobEnchant != null && !mobEnchant.isCompatibleMob(livingEntity)) {
            return false;
        } else if (mobEnchant.isDisabled()) {
            return false;
        } else {
            Iterator var3 = ability.getMobEnchants().iterator();

            MobEnchantmentHandler enchantHandler;
            do {
                if (!var3.hasNext()) {
                    return mobEnchant != null;
                }

                enchantHandler = (MobEnchantmentHandler)var3.next();
            } while(mobEnchant == null || enchantHandler.getMobEnchant() == null || enchantHandler.getMobEnchant().isCompatibleWith(mobEnchant));

            return false;
        }
    }

    public static int getMobEnchantLevelFromHandler(List<MobEnchantmentHandler> list, MobEnchant findMobEnchant) {
        Iterator var2 = list.iterator();

        MobEnchantmentHandler mobEnchant;
        do {
            if (!var2.hasNext()) {
                return 0;
            }

            mobEnchant = (MobEnchantmentHandler)var2.next();
        } while(mobEnchant == null || !mobEnchant.getMobEnchant().equals(findMobEnchant));

        return mobEnchant.getEnchantLevel();
    }

    public static List<MobEnchantmentData> buildEnchantmentList(Random random, int level, boolean allowTresure) {
        ArrayList<MobEnchantmentData> list = Lists.newArrayList();
        int i = 1;
        if (i <= 0) {
            return list;
        }
        level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
        float f = (random.nextFloat() + random.nextFloat() - 1.0f) * 0.15f;
        List<MobEnchantmentData> list2 = makeMobEnchantmentDatas(level = MathHelper.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE), allowTresure);
        if (!list2.isEmpty()) {
            Weighting.getRandom(random, list2).ifPresent(list::add);
            while (random.nextInt(50) <= level) {
                if (!list.isEmpty()) {
                    removeIncompatible(list2, Util.getLast(list));
                }
                if (list2.isEmpty()) break;
                Weighting.getRandom(random, list2).ifPresent(list::add);
                level /= 2;
            }
        }

        return list;
    }

    public static List<MobEnchantmentData> makeMobEnchantmentDatas(int p_185291_0_, boolean allowTresure) {
        List<MobEnchantmentData> list = Lists.newArrayList();
        Iterator var3 = ModRegistries.MOB_ENCHANT.stream().toList().iterator();

        while(true) {
            while(true) {
                MobEnchant enchantment;
                do {
                    do {
                        if (!var3.hasNext()) {
                            return list;
                        }

                        enchantment = (MobEnchant)var3.next();
                    } while(enchantment.isTresureEnchant() && !allowTresure);
                } while(enchantment.isOnlyChest());

                for(int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                    if (p_185291_0_ >= enchantment.getMinEnchantability(i) && p_185291_0_ <= enchantment.getMaxEnchantability(i)) {
                        list.add(new MobEnchantmentData(enchantment, i));
                        break;
                    }
                }
            }
        }
    }

    private static void removeIncompatible(List<MobEnchantmentData> dataList, MobEnchantmentData data) {
        Iterator<MobEnchantmentData> iterator = dataList.iterator();

        while(true) {
            do {
                if (!iterator.hasNext()) {
                    return;
                }
            } while(data.enchantment.isCompatibleWith(iterator.next().enchantment) && !data.enchantment.isDisabled());

            iterator.remove();
        }
    }
}
