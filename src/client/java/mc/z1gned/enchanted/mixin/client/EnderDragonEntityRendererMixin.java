package mc.z1gned.enchanted.mixin.client;

import mc.z1gned.enchanted.feature.GlintFeature;
import mc.z1gned.enchanted.util.MobEnchantInterface;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntityRenderer.class)
public class EnderDragonEntityRendererMixin {

    @Shadow @Final private EnderDragonEntityRenderer.DragonEntityModel model;

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private void renderer(EnderDragonEntity enderDragonEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (enderDragonEntity instanceof MobEnchantInterface ability) {
            if (ability.getEnchantAbility().hasEnchant()) {
                matrixStack.push();
                float k = (float) enderDragonEntity.getSegmentProperties(7, g)[0];
                float l = (float) (enderDragonEntity.getSegmentProperties(5, g)[1] - enderDragonEntity.getSegmentProperties(10, g)[1]);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-k));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(l * 10.0F));
                matrixStack.translate(0.0F, 0.0F, 1.0F);
                matrixStack.scale(-1.0F, -1.0F, 1.0F);
                matrixStack.translate(0.0F, -1.501F, 0.0F);
                boolean flag = enderDragonEntity.hurtTime > 0;
                this.model.animateModel(enderDragonEntity, 0.0F, 0.0F, g);
                if (enderDragonEntity.ticksSinceDeath <= 0) {
                    float f2 = (float)enderDragonEntity.age + g;
                    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(GlintFeature.enchantSwirl(ability.getEnchantAbility().isAncient() ? GlintFeature.ANCIENT_GLINT : ItemRenderer.ENTITY_ENCHANTMENT_GLINT, MathHelper.cos(f2 * 0.01F) * 0.5F % 1.0F, f2 * 0.01F * 0.75F % 1.0F));
                    this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.getUv(0.0F, flag), 1.0F, 1.0F, 1.0F, 1.0F);
                }

                matrixStack.pop();
            }
        }
    }

}
