package sparkuniverse.amo.alkahest.reactions.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import sparkuniverse.amo.alkahest.damagetypes.AttributeRegistry;
import sparkuniverse.amo.alkahest.util.ColorHelper;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class EarthCoreEntityRenderer extends EntityRenderer<EarthCoreEntity> {
    Color color = new Color(ColorHelper.typeColorMap.get(AttributeRegistry.EARTH_DAMAGE.get().getDescriptionId()));
    private final EarthCoreEntityRenderer.EarthCoreEntityModel model = new EarthCoreEntityModel();
    protected EarthCoreEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(EarthCoreEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees((pEntity.level.getGameTime() + pPartialTick)*5));
        pPoseStack.scale(0.5F, 0.5F, 0.5F);
        pPoseStack.translate(-0.5, -0.5 + (Math.sin((pPartialTick + pEntity.level.getGameTime())/5)/3f), -0.5);
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(lightning(getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 0.5f);
        pPoseStack.translate(0.05, 0.05, 0.05);
        pPoseStack.scale(0.9f, 0.9f, 0.9f);
        Color color2 = new Color(color.getRGB()).brighter().brighter();
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(lightning(getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, color2.getRed()/255f, color2.getGreen()/255f, color2.getBlue()/255f, 0.5f);
        pPoseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EarthCoreEntity pEntity) {
        return new ResourceLocation("alkahest:textures/particle/blank.png");
    }

    public static class EarthCoreEntityModel extends Model {
        private final ModelPart core;
        public EarthCoreEntityModel() {
            super(EarthCoreEntityRenderer::lightning);
            java.util.List<ModelPart.Cube> cube = List.of(
                    new ModelPart.Cube(0,0,4,4,4,8,8,8,0,0,0,false,1,1)
            );
            core = new ModelPart(cube, Map.of());
        }

        @Override
        public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
            core.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        }
    }
    public static RenderType lightning(ResourceLocation pLocation) {
        return RenderType.lightning();
    }
}