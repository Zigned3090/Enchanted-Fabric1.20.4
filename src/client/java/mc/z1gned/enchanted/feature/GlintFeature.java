package mc.z1gned.enchanted.feature;

import mc.z1gned.enchanted.util.MobEnchantInterface;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static mc.z1gned.enchanted.ModMain.MOD_ID;

@Environment(EnvType.CLIENT)
public class GlintFeature<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    public static final Identifier ANCIENT_GLINT = new Identifier(MOD_ID, "textures/entity/ancient_glint.png");

    public GlintFeature(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity instanceof MobEnchantInterface ability) {
            if (ability.getEnchantAbility().hasEnchant() && !entity.isInvisible()) {
                float intensity = ability.getEnchantAbility().getMobEnchants().size() < 3 ? ((float) ability.getEnchantAbility().getMobEnchants().size() / 3) : 3;
                float f = (float) entity.age + tickDelta;
                EntityModel<T> entitymodel = this.getContextModel();
                entitymodel.animateModel(entity, limbAngle, limbDistance, tickDelta);
                this.getContextModel().copyStateTo(entitymodel);
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(enchantSwirl(ability.getEnchantAbility().isAncient() ? ANCIENT_GLINT : ItemRenderer.ENTITY_ENCHANTMENT_GLINT, MathHelper.cos(f * 0.01F) * 0.5F % 1.0F, f * 0.01F * 0.75F % 1.0F));
                entitymodel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
                entitymodel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, intensity, intensity, intensity, 1.0F);
            }
        }
    }

    public static RenderLayer enchantSwirl(Identifier resourceLocation, float f, float g) {
        return RenderLayer.of("enchant_effect", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder().program(RenderPhase.ENERGY_SWIRL_PROGRAM).texture(new RenderPhase.Texture(resourceLocation, false, false)).texturing(new RenderPhase.OffsetTexturing(f, g)).transparency(RenderPhase.ADDITIVE_TRANSPARENCY).cull(RenderPhase.DISABLE_CULLING).depthTest(RenderPhase.EQUAL_DEPTH_TEST).lightmap(RenderPhase.ENABLE_LIGHTMAP).overlay(RenderPhase.ENABLE_OVERLAY_COLOR).build(false));
    }

}
