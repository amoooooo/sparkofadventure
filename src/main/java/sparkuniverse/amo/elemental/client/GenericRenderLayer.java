package sparkuniverse.amo.elemental.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelUtils;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.effects.ReactionEffects;

public class GenericRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public static final ResourceLocation ICE = new ResourceLocation("textures/block/ice.png");
    private final EntityModel<T> model;

    public GenericRenderLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
        model = pRenderer.getModel();
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if(pLivingEntity.hasEffect(ReactionEffects.FROZEN.get())){
            var opacity = 0.5f;
            var darkness = 1F;
            EntityModel<T> entityModel = this.model;
            this.getParentModel().copyPropertiesTo(entityModel);
            entityModel.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
            this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(ICE));
            entityModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, darkness, darkness, darkness, 0.75f);
        }
    }
}
