package mc.z1gned.enchanted.mixin;

import mc.z1gned.enchanted.data.ModTrackedData;
import mc.z1gned.enchanted.util.MobEnchantInterface;
import mc.z1gned.enchanted.util.MobEnchantmentAbility;
import mc.z1gned.enchanted.util.MobEnchantmentEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements MobEnchantInterface {
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<MobEnchantmentAbility> MOB_ENCHANTMENT_ABILITY_DATA = DataTracker.registerData(LivingEntity.class, ModTrackedData.MOB_ENCHANTMENT_ABILITY);
    @Unique
    private static final String MOB_ENCHANTMENT_ABILITY_KEY = "MobEnchantmentData";

    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
        throw new IllegalStateException("This class must not be instantiated");
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readNbt(NbtCompound nbt, CallbackInfo info) {
        setEnchantAbility(MobEnchantmentAbility.deserializeNBT(nbt.getCompound(MOB_ENCHANTMENT_ABILITY_KEY)));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.put(MOB_ENCHANTMENT_ABILITY_KEY, getEnchantAbility().serializeNBT());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo callbackInfo) {
        if ((this.getEnchantAbility().getEnchantOwner().isPresent() || this.getEnchantAbility().isFromOwner()) && this.getEnchantAbility().hasEnchant()) {
            LivingEntity livingEntity;
            if (this.getEnchantAbility().getEnchantOwner().isEmpty()) {
                livingEntity = (LivingEntity) (Object) this;
                this.getEnchantAbility().removeMobEnchantFromOwner(livingEntity);
                this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.5F, 1.6F);
            }
            else if (this.squaredDistanceTo(this.getEnchantAbility().getEnchantOwner().get()) > 512.0) {
                livingEntity = (LivingEntity) (Object) this;
                this.getEnchantAbility().removeMobEnchantFromOwner(livingEntity);
                this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.5F, 1.6F);
            }
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void defineSyncData(CallbackInfo callbackInfo) {
        this.dataTracker.startTracking(MOB_ENCHANTMENT_ABILITY_DATA, new MobEnchantmentAbility());
    }

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    private void getDamageAfterMagicAbsorb(DamageSource damageSource, float f, CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        cir.setReturnValue(MobEnchantmentEvent.extraDamage(livingEntity, damageSource, f));
    }

    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public MobEnchantmentAbility getEnchantAbility() {
        return this.dataTracker.get(MOB_ENCHANTMENT_ABILITY_DATA);
    }

    @Override
    @SuppressWarnings("AddedMixinMembersNamePattern")
    public void setEnchantAbility(MobEnchantmentAbility ability) {
        this.dataTracker.set(MOB_ENCHANTMENT_ABILITY_DATA, ability);
    }
}

