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

//祛魔物品
public class FilthyEmeraldItem extends Item {
    //构造函数
    public FilthyEmeraldItem(Settings settings) {
        super(settings);
    }

    //右键触发，玩家对自己使用
    public TypedActionResult<ItemStack> use(World level, PlayerEntity user, Hand handIn) {
        //获得主手物品栈
        ItemStack stack = user.getStackInHand(handIn);
        //如果玩家有附魔接口，就移除附魔
        if (user instanceof MobEnchantInterface) {
            MobEnchantmentUtil.removeMobEnchantToEntity(user, ((MobEnchantInterface) user).getEnchantAbility());
        }
        //播放音效
        user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
        //不是创造模式就消耗物品
        if (!user.isCreative()) {
            stack.decrement(1);
        }
        //成功，返回物品栈信息（为啥🤔）
        return TypedActionResult.success(stack);
    }
    //对生物使用，逻辑同上
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
