package mc.z1gned.enchanted.mixin.client;

import mc.z1gned.enchanted.feature.GlintFeature;
import mc.z1gned.enchanted.util.MobEnchantInterface;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.WitherSkullEntityRenderer;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherSkullEntityRenderer.class)
public class WitherSkullEntityRendererMixin {
    @Shadow @Final private SkullEntityModel model;

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/entity/projectile/WitherSkullEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private void renderer(WitherSkullEntity witherSkullEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        Entity owner = witherSkullEntity.getOwner();
        if (owner instanceof MobEnchantInterface ability) {
            if (ability.getEnchantAbility().hasEnchant()) {
                matrixStack.push();
                matrixStack.scale(-1.0F, -1.0F, 1.0F);
                float l = MathHelper.lerpAngleDegrees(witherSkullEntity.prevYaw, witherSkullEntity.getYaw(), g);
                float f1 = MathHelper.lerp(g, witherSkullEntity.prevPitch, witherSkullEntity.getPitch());
                float f2 = (float)witherSkullEntity.age + g;
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(GlintFeature.enchantSwirl(ability.getEnchantAbility().isAncient() ? GlintFeature.ANCIENT_GLINT : ItemRenderer.ENTITY_ENCHANTMENT_GLINT, MathHelper.cos(f2 * 0.01F) * 0.5F % 1.0F, f2 * 0.01F * 0.75F % 1.0F));
                this.model.setHeadRotation(0.0F, l, f1);
                this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStack.pop();
            }
        }
    }

}
