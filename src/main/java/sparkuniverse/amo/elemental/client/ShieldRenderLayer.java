package sparkuniverse.amo.elemental.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import sparkuniverse.amo.elemental.Elemental;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.effects.ReactionEffects;
import sparkuniverse.amo.elemental.reactions.entity.NatureCoreEntityRenderer;
import sparkuniverse.amo.elemental.util.Color;
import sparkuniverse.amo.elemental.util.ColorHelper;

import java.util.List;
import java.util.Map;

public class ShieldRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public static final ResourceLocation ICE = Elemental.prefix("textures/particle/blank.png");
    private final EntityModel<T> model;
    public ShieldRenderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        model = pRenderer.getModel();
    }

    @Override
    public void render(PoseStack ps, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pLivingEntity.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(s -> {
            if(s.hasShield()){
                Color color = new Color(ColorHelper.getColor(s.getShieldDefType()), 1);
                double opacity = Math.min(0.75, s.getShieldHealth()/s.getShieldMaxHealth());
                EntityModel<T> entityModel = this.model;
                this.getParentModel().copyPropertiesTo(entityModel);
                entityModel.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
                this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentCull(ICE));
                ps.scale(1.0001f, 1.0001f, 1.0001f);
                ps.translate(0, -0.01825*pLivingEntity.getEyeHeight(), 0);
                entityModel.renderToBuffer(ps, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, color.r/255f, color.g/255f, color.b/255f, (float) opacity);
                ps.translate(0, 0.000025, 0);
                ps.scale(1/1.075f, 1/1.075f, 1/1.075f);
                ps.translate(-pLivingEntity.getBbWidth()*pLivingEntity.getBbHeight()/2f, -pLivingEntity.getBbHeight()+0.5f, -pLivingEntity.getBbWidth()*pLivingEntity.getBbHeight()/2f);
                ps.scale(pLivingEntity.getBbWidth()*pLivingEntity.getBbHeight(), pLivingEntity.getBbHeight()*2, pLivingEntity.getBbWidth()*pLivingEntity.getBbHeight());
            }
        });
    }
}
