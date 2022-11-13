package sparkuniverse.amo.elemental.reactions.effects.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;

public class ParticleRenderer {
    public static void renderParticles(PoseStack matrix, Camera camera, float ptick) {
        for (TextParticle p : ParticleStates.PARTICLES) {
            renderParticle(matrix, p, camera, ptick);
        }
    }

    private static void renderParticle(PoseStack matrix, TextParticle particle, Camera camera, float ptick) {
        double distanceSquared = camera.getPosition().distanceToSqr(particle.x, particle.y, particle.z);
        if (distanceSquared > 128) {
            return;
        }

        float scaleToGui = 0.025f;

        double x = Mth.lerp(ptick, particle.xPrev, particle.x);
        double y = Mth.lerp(ptick, particle.yPrev, particle.y);
        double z = Mth.lerp(ptick, particle.zPrev, particle.z);

        Vec3 camPos = camera.getPosition();
        double camX = camPos.x;
        double camY = camPos.y;
        double camZ = camPos.z;

        matrix.pushPose();
        matrix.translate(x - camX, y - camY, z - camZ);
        matrix.mulPose(Vector3f.YP.rotationDegrees(-camera.getYRot()));
        matrix.mulPose(Vector3f.XP.rotationDegrees(camera.getXRot()));
        matrix.scale(-scaleToGui, -scaleToGui, scaleToGui);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE,
                GL11.GL_ZERO);

        ExtraRenderer.drawDamageNumber(matrix, particle.damage, 0, 0, 10, particle.color);

        RenderSystem.disableBlend();

        matrix.popPose();
    }
}
