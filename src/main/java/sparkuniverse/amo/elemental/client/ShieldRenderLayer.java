package sparkuniverse.amo.elemental.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.effects.ReactionEffects;
import sparkuniverse.amo.elemental.util.Color;
import sparkuniverse.amo.elemental.util.ColorHelper;

public class ShieldRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public static final ResourceLocation ICE = new ResourceLocation("textures/block/ice.png");
    private final EntityModel<T> model;

    public ShieldRenderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        model = pRenderer.getModel();
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pLivingEntity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(s -> {
            if(s.hasShield()){
                Color color = new Color(ColorHelper.getColor(s.getShieldDefType()));
                var opacity = s.getShieldHealth()/s.getShieldMaxHealth();
                EntityModel<T> entityModel = this.model;
                this.getParentModel().copyPropertiesTo(entityModel);
                entityModel.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
                this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation("textures/particle/blank.png")));
                pPoseStack.scale(1.1f, 1.1f, 1.1f);
                pPoseStack.translate(0,0.25f,0);
                entityModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, color.r, color.g, color.b, opacity);
            }
        });
    }
}
