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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements MobEnchantInterface {
    @Unique
    private static final TrackedData<MobEnchantmentAbility> MOB_ENCHANTMENT_ABILITY;


    @Shadow public abstract boolean damage(DamageSource source, float amount);


    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo callbackInfo) {
        if ((this.getEnchantAbility().getEnchantOwner().isPresent() || this.getEnchantAbility().isFromOwner()) && this.getEnchantAbility().hasEnchant()) {
            LivingEntity livingEntity;
            if (this.getEnchantAbility().getEnchantOwner().isEmpty()) {
                livingEntity = (LivingEntity) (Object) this;
                this.getEnchantAbility().removeMobEnchantFromOwner(livingEntity);
                this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.5F, 1.6F);
            } else if (this.squaredDistanceTo(this.getEnchantAbility().getEnchantOwner().get()) > 512.0) {
                livingEntity = (LivingEntity) (Object) this;
                this.getEnchantAbility().removeMobEnchantFromOwner(livingEntity);
                this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.5F, 1.6F);
            }
        }

        if ((LivingEntity) (Object) this instanceof PlayerEntity player) {
            if (player.getWorld().isClient) {
                if (player instanceof MobEnchantInterface ability) {
                    System.out.println(ability.getEnchantAbility().hasEnchant());
                }
            }
        }

    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void defineSynchedData(CallbackInfo callbackInfo) {
        this.dataTracker.startTracking(MOB_ENCHANTMENT_ABILITY, new MobEnchantmentAbility());
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void addAdditionalSaveData(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("MobEnchantmentData", this.getEnchantAbility().serializeNBT());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readAdditionalSaveData(NbtCompound nbt, CallbackInfo ci) {
        MobEnchantmentAbility mobEnchantmentAbility = new MobEnchantmentAbility();
        mobEnchantmentAbility.deserializeNBT(nbt.getCompound("MobEnchantmentData"));
        this.getEnchantAbility().deserializeNBT(nbt.getCompound("MobEnchantmentData"));
    }

    @Inject(method = "modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    private void getDamageAfterMagicAbsorb(DamageSource damageSource, float f, CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        cir.setReturnValue(MobEnchantmentEvent.extraDamage(livingEntity, damageSource, f));
    }

    @Override
    public MobEnchantmentAbility getEnchantAbility() {
        return this.dataTracker.get(MOB_ENCHANTMENT_ABILITY);
    }

    @Override
    public void setEnchantAbility(MobEnchantmentAbility ability) {
        this.dataTracker.set(MOB_ENCHANTMENT_ABILITY, ability);
    }

    static {
        MOB_ENCHANTMENT_ABILITY = DataTracker.registerData(LivingEntity.class, ModTrackedData.MOB_ENCHANTMENT_ABILITY);
    }

}

