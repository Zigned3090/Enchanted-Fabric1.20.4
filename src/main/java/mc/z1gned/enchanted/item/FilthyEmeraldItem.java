package mc.z1gned.enchanted.item;

import mc.z1gned.enchanted.util.MobEnchantInterface;
import mc.z1gned.enchanted.util.MobEnchantmentUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FilthyEmeraldItem extends Item {
    public FilthyEmeraldItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World level, PlayerEntity user, Hand handIn) {
        ItemStack stack = user.getStackInHand(handIn);
        if (user instanceof MobEnchantInterface) {
            MobEnchantmentUtil.removeMobEnchantToEntity(user, ((MobEnchantInterface) user).getEnchantAbility());
        }

        user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
        if (!user.isCreative()) {
            stack.decrement(1);
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (entity instanceof MobEnchantInterface) {
            MobEnchantmentUtil.removeMobEnchantToEntity(entity, ((MobEnchantInterface) entity).getEnchantAbility());
        }

        itemStack.decrement(1);
        user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
        return super.useOnEntity(stack, user, entity, hand);
    }

}
