package mc.z1gned.enchanted.util;

import com.google.common.collect.Lists;
import mc.z1gned.enchanted.enchantment.mob.MobEnchant;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MobEnchantmentAbility {
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("6678a403-e2cc-31e6-195e-4757200e0935");
    private static final EntityAttributeModifier HEALTH_MODIFIER;
    private List<MobEnchantmentHandler> mobEnchants = Lists.newArrayList();
    private Optional<LivingEntity> enchantOwner = Optional.empty();
    private boolean fromOwner;
    private EnchantType enchantType;

    public MobEnchantmentAbility() {
        this.enchantType = EnchantType.NORMAL;
    }

    public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel) {
        this.mobEnchants.add(new MobEnchantmentHandler(mobEnchant, enchantLevel));
        this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
        entity.calculateDimensions();
        this.sync(entity);
    }

    public void addMobEnchant(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, boolean ancient) {
        this.addMobEnchant(entity, mobEnchant, enchantLevel);
        this.setEnchantType(entity, ancient ? EnchantType.ANCIENT : EnchantType.NORMAL);
    }

    public void setEnchantType(LivingEntity entity, EnchantType enchantType) {
        this.enchantType = enchantType;
        this.sync(entity);
    }

    public void addMobEnchantFromOwner(LivingEntity entity, MobEnchant mobEnchant, int enchantLevel, LivingEntity owner) {
        this.mobEnchants.add(new MobEnchantmentHandler(mobEnchant, enchantLevel));
        this.onNewEnchantEffect(entity, mobEnchant, enchantLevel);
        entity.calculateDimensions();
        this.sync(entity);
    }

    public void addOwner(LivingEntity entity, @Nullable LivingEntity owner) {
        this.fromOwner = true;
        this.enchantOwner = Optional.ofNullable(owner);
        this.sync(entity);
    }

    public void removeOwner(LivingEntity entity) {
        this.fromOwner = false;
        this.enchantOwner = Optional.empty();
    }

    public void removeAllMobEnchant(LivingEntity entity) {
        for(int i = 0; i < this.mobEnchants.size(); ++i) {
            this.onRemoveEnchantEffect(entity, this.mobEnchants.get(i).getMobEnchant());
        }

        this.mobEnchants.removeAll(this.mobEnchants);
        entity.calculateDimensions();
        this.sync(entity);
    }

    public void removeMobEnchantFromOwner(LivingEntity entity) {
        for(int i = 0; i < this.mobEnchants.size(); ++i) {
            this.onRemoveEnchantEffect(entity, this.mobEnchants.get(i).getMobEnchant());
        }

        this.mobEnchants.removeAll(this.mobEnchants);
        this.removeOwner(entity);
        entity.calculateDimensions();
        this.sync(entity);
    }

    public void onNewEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
        if (!entity.getWorld().isClient) {
            enchant.applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel);
        }

    }

    protected void onChangedEnchantEffect(LivingEntity entity, MobEnchant enchant, int enchantLevel) {
        if (!entity.getWorld().isClient) {
            enchant.applyAttributesModifiersToEntity(entity, entity.getAttributes(), enchantLevel);
        }

    }

    protected void onRemoveEnchantEffect(LivingEntity entity, MobEnchant enchant) {
        if (!entity.getWorld().isClient()) {
            enchant.removeAttributesModifiersFromEntity(entity, entity.getAttributes());
            EntityAttributeInstance modifiableAttributeInstance = entity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (modifiableAttributeInstance != null && modifiableAttributeInstance.hasModifier(HEALTH_MODIFIER)) {
                entity.setHealth(entity.getHealth() / 1.5F);
                modifiableAttributeInstance.removeModifier(HEALTH_MODIFIER.getId());
            }
        }

    }

    public MobEnchantmentAbility copy() {
        MobEnchantmentAbility ability = new MobEnchantmentAbility();
        ability.mobEnchants = this.mobEnchants;
        ability.enchantOwner = this.enchantOwner;
        ability.enchantType = this.enchantType;
        return ability;
    }

    public final void sync(LivingEntity entity) {
        ((MobEnchantInterface) entity).setEnchantAbility(copy());
    }

    public List<MobEnchantmentHandler> getMobEnchants() {
        return this.mobEnchants;
    }

    public boolean hasEnchant() {
        return !this.mobEnchants.isEmpty();
    }

    public Optional<LivingEntity> getEnchantOwner() {
        return this.enchantOwner;
    }

    public boolean hasOwner() {
        return this.enchantOwner.isPresent() && this.enchantOwner.get().isAlive();
    }

    public boolean isFromOwner() {
        return this.fromOwner;
    }

    public EnchantType getEnchantType() {
        return this.enchantType;
    }

    public boolean isAncient() {
        return this.enchantType == EnchantType.ANCIENT;
    }

    public NbtCompound serializeNBT() {
        NbtCompound nbt = new NbtCompound();
        NbtList listNbt = new NbtList();

        for (MobEnchantmentHandler mobEnchant : this.mobEnchants) {
            listNbt.add(mobEnchant.writeNBT());
        }

        nbt.put(MobEnchantmentUtil.STORED_MOB_ENCHANTS, listNbt);
        nbt.putBoolean("FromOwner", this.fromOwner);
        return nbt;
    }

    public static MobEnchantmentAbility deserializeNBT(NbtCompound nbt) {
        MobEnchantmentAbility ability = new MobEnchantmentAbility();
        NbtList list = MobEnchantmentUtil.getEnchantmentListForNBT(nbt);
        ability.mobEnchants.clear();

        for(int i = 0; i < list.size(); ++i) {
            NbtCompound nbtCompound = list.getCompound(i);
            MobEnchant mobEnchant = MobEnchantmentUtil.getEnchantFromNBT(nbtCompound);
            if (mobEnchant != null)
                ability.mobEnchants.add(new MobEnchantmentHandler(mobEnchant, MobEnchantmentUtil.getEnchantLevelFromNBT(nbtCompound)));
        }

        ability.fromOwner = nbt.getBoolean("FromOwner");
        return ability;
    }

    static {
        HEALTH_MODIFIER = new EntityAttributeModifier(HEALTH_MODIFIER_UUID, "Health boost", 0.5, EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    public static enum EnchantType {
        NORMAL,
        ANCIENT;

        private EnchantType() {
        }

        public static EnchantType get(String nameIn) {
            EnchantType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                EnchantType enchantType = var1[var3];
                if (enchantType.name().equals(nameIn)) {
                    return enchantType;
                }
            }

            return NORMAL;
        }
    }

}
