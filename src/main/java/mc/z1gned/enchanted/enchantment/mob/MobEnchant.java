package mc.z1gned.enchanted.enchantment.mob;

import com.google.common.collect.Maps;
import mc.z1gned.enchanted.registry.ModRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Util;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MobEnchant {
    private final Map<EntityAttribute, EntityAttributeModifier> attributeModifierMap = Maps.newHashMap();
    protected final Rarity enchantType;
    private final int level;
    private int minLevel = 1;

    public MobEnchant(Properties properties) {
        this.enchantType = properties.enchantType;
        this.level = properties.level;
    }

    public Rarity getRarity() {
        return this.enchantType;
    }

    public MobEnchant setMinLevel(int level) {
        this.minLevel = level;
        return this;
    }

    public int getMinLevel() {
        return this.minLevel;
    }

    public int getMaxLevel() {
        return this.level;
    }

    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + enchantmentLevel * 10;
    }

    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 5;
    }

    public void tick(LivingEntity entity, int level) {
    }

    public final boolean isCompatibleWith(MobEnchant enchantmentIn) {
        return this.canApplyTogether(enchantmentIn) && enchantmentIn.canApplyTogether(this);
    }

    public boolean isTresureEnchant() {
        return false;
    }

    public boolean isOnlyChest() {
        return false;
    }

    public boolean isCompatibleMob(LivingEntity livingEntity) {
        return true;
    }

    protected boolean canApplyTogether(MobEnchant ench) {
        return this != ench;
    }

    public MobEnchant addAttributesModifier(EntityAttribute attributeIn, String uuid, double amount, EntityAttributeModifier.Operation operation) {
        EntityAttributeModifier attributemodifier = new EntityAttributeModifier(UUID.fromString(uuid), Util.createTranslationKey("mob_enchant", ModRegistries.MOB_ENCHANT.getId(this)), amount, operation);
        this.attributeModifierMap.put(attributeIn, attributemodifier);
        return this;
    }

    public Map<EntityAttribute, EntityAttributeModifier> getAttributeModifierMap() {
        return this.attributeModifierMap;
    }

    public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AttributeContainer attributeMapIn) {
        Iterator var3 = this.attributeModifierMap.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<EntityAttribute, EntityAttributeModifier> entry = (Map.Entry)var3.next();
            EntityAttributeInstance modifiableattributeinstance = attributeMapIn.getCustomInstance(entry.getKey());
            if (modifiableattributeinstance != null) {
                modifiableattributeinstance.removeModifier(entry.getValue().getId());
            }
        }

    }

    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AttributeContainer attributeMapIn, int amplifier) {

        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierEntry : this.attributeModifierMap.entrySet()) {
            Map.Entry<EntityAttribute, EntityAttributeModifier> entry = entityAttributeEntityAttributeModifierEntry;
            EntityAttributeInstance modifiableAttributeInstance = attributeMapIn.getCustomInstance(entry.getKey());
            if (modifiableAttributeInstance != null) {
                EntityAttributeModifier attributeModifier = entry.getValue();
                modifiableAttributeInstance.removeModifier(attributeModifier.getId());
                    modifiableAttributeInstance.addPersistentModifier(new EntityAttributeModifier(attributeModifier.getId(), ModRegistries.MOB_ENCHANT.getId(this).toString() + " " + amplifier, this.getAttributeModifierAmount(amplifier, attributeModifier), attributeModifier.getOperation()));
            }
        }

    }

    public double getAttributeModifierAmount(int amplifier, EntityAttributeModifier modifier) {
        return modifier.getValue() * (double)amplifier;
    }

    public boolean isDisabled() {
        return false;
    }

    public static class Properties {
        private final Rarity enchantType;
        private final int level;

        public Properties(Rarity enchantType, int level) {
            this.enchantType = enchantType;
            this.level = level;
        }
    }

    public static enum Rarity {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        private Rarity(int rarityWeight) {
            this.weight = rarityWeight;
        }

        public int getWeight() {
            return this.weight;
        }
    }
}
