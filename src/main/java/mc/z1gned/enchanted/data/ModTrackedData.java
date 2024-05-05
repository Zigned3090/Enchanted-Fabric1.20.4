package mc.z1gned.enchanted.data;

import mc.z1gned.enchanted.util.MobEnchantmentAbility;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;

public class ModTrackedData {

    public static final TrackedDataHandler<MobEnchantmentAbility> MOB_ENCHANTMENT_ABILITY = new TrackedDataHandler<>() {
        public void write(PacketByteBuf buf, MobEnchantmentAbility ability) {
            buf.writeNbt(ability.serializeNBT());
        }

        public MobEnchantmentAbility read(PacketByteBuf buf) {
            return MobEnchantmentAbility.deserializeNBT(buf.readNbt());
        }

        public MobEnchantmentAbility copy(MobEnchantmentAbility ability) {
            return ability.copy();
        }
    };

    public static void registerDataTrackers() {
        TrackedDataHandlerRegistry.register(MOB_ENCHANTMENT_ABILITY);
    }
}
