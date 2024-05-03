package mc.z1gned.enchanted.mixin.client;

import mc.z1gned.enchanted.feature.GlintFeature;
import mc.z1gned.enchanted.util.MobEnchantInterface;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntityRenderer.class)
public class ProjectileEntityRendererMixin<T extends PersistentProjectileEntity> {

    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private void renderer(T persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
        Entity entity = persistentProjectileEntity.getOwner();
        if (entity instanceof MobEnchantInterface ability) {
            if (ability.getEnchantAbility().hasEnchant()) {
                matrixStack.push();
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, persistentProjectileEntity.prevYaw, persistentProjectileEntity.getYaw()) - 90.0F));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, persistentProjectileEntity.prevPitch, persistentProjectileEntity.getPitch())));
                float l = 0.0F;
                float k = (float) persistentProjectileEntity.shake - g;
                float ticks;
                if (k > 0.0F) {
                    ticks = -MathHelper.sin(k * 3.0F) * k;
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(ticks));
                }

                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
                matrixStack.scale(0.05625F, 0.05625F, 0.05625F);
                matrixStack.translate(-4.0F, 0.0F, 0.0F);
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(GlintFeature.enchantSwirl(ability.getEnchantAbility().isAncient() ? GlintFeature.ANCIENT_GLINT : ItemRenderer.ENTITY_ENCHANTMENT_GLINT, MathHelper.cos(l * 0.01F) * 0.5F % 1.0F, l * 0.01F * 0.75F % 1.0F));
                MatrixStack.Entry pose = matrixStack.peek();
                Matrix4f matrix4f = pose.getPositionMatrix();
                Matrix3f matrix3f = pose.getNormalMatrix();
                this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, light);
                this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, light);
                this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, light);
                this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, light);
                this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, light);
                this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, light);
                this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, light);
                this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, light);

                for(int j = 0; j < 4; ++j) {
                    matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                    this.vertex(matrix4f, matrix3f, vertexConsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, light);
                    this.vertex(matrix4f, matrix3f, vertexConsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, light);
                    this.vertex(matrix4f, matrix3f, vertexConsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, light);
                    this.vertex(matrix4f, matrix3f, vertexConsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, light);
                }

                matrixStack.pop();
            }
        }
    }

    @Unique
    public void vertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, int i, int l, int j, float f, float p, int r, int g, int b, int a) {
        vertexConsumer.vertex(matrix4f, (float)i, (float)l, (float)j).color(255, 255, 255, 255).texture(f, p).overlay(OverlayTexture.DEFAULT_UV).light(a).normal(matrix3f, (float)r, (float)b, (float)g).next();
    }

}
