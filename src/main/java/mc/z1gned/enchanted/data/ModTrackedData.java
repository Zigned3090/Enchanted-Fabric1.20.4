package mc.z1gned.enchanted.data;

import mc.z1gned.enchanted.util.MobEnchantmentAbility;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.PacketByteBuf;

public class ModTrackedData {

    public static final TrackedDataHandler<MobEnchantmentAbility> MOB_ENCHANTMENT_ABILITY = new TrackedDataHandler<>() {
        public void write(PacketByteBuf packetByteBuf, MobEnchantmentAbility nbtCompound) {
            packetByteBuf.writeNbt(nbtCompound.serializeNBT());
        }

        public MobEnchantmentAbility read(PacketByteBuf packetByteBuf) {
            MobEnchantmentAbility mobEnchantmentAbility = new MobEnchantmentAbility();
            mobEnchantmentAbility.deserializeNBT(packetByteBuf.readNbt());
            return mobEnchantmentAbility;
        }

        public MobEnchantmentAbility copy(MobEnchantmentAbility mobEnchantmentAbility) {
            return mobEnchantmentAbility;
        }
    };

    public static void registerDataTrackers() {
        TrackedDataHandlerRegistry.register(MOB_ENCHANTMENT_ABILITY);
    }

}
