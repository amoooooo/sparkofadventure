package sparkuniverse.amo.sparkofadventure.reactions.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class NatureCoreEntityRenderer extends EntityRenderer<NatureCoreEntity> {
    private final NatureCoreEntityModel model = new NatureCoreEntityModel();
    protected NatureCoreEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(NatureCoreEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(NatureCoreEntity pEntity) {
        return null;
    }

    public class NatureCoreEntityModel extends Model {
        private final ModelPart core;
        public NatureCoreEntityModel() {
            super(RenderType::entitySolid);
            List<ModelPart.Cube> cube = List.of(
                    new ModelPart.Cube(0,0,4,4,4,12,12,12,0,0,0,false,1,1)
            );
            core = new ModelPart(cube, Map.of());
        }

        @Override
        public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
            core.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        }
    }
}
