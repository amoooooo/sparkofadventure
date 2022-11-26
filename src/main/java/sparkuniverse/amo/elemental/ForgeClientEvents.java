package sparkuniverse.amo.elemental;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import sparkuniverse.amo.elemental.compat.MobHealthBar.MobHealthBarCompatLayer;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.elemental.reactions.effects.particle.ParticleRenderer;
import sparkuniverse.amo.elemental.util.Pair;
import sparkuniverse.amo.elemental.util.SymbolHelper;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Elemental.MODID, value = Dist.CLIENT)
public class ForgeClientEvents {

    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            ParticleRenderer.renderParticles(event.getPoseStack(), Minecraft.getInstance().gameRenderer.getMainCamera(), event.getPartialTick());
        }
    }

    @SubscribeEvent
    public static void onNametagRenderEvent(RenderNameTagEvent event){
        if(event.getEntity() instanceof LivingEntity le){
            le.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(s -> {
                if(!s.getMarks().isEmpty()){
                    if(ModList.get().isLoaded("mobhealthbar")){
                        MobHealthBarCompatLayer.isMobHealthBarLoaded = true;

                    } else {
                        PoseStack ps = event.getPoseStack();
                        ps.pushPose();
                        //ps.scale(1.5f, 1.5f, 1.5f);
                        ps.translate(0, 0.5, 0);
                        List<String> marks = s.getMarks().stream().map(Pair::getFirst).toList();
                        StringBuilder sb = new StringBuilder();
                        for(String mark : marks){
                            sb.append(SymbolHelper.getSymbol(mark));
                        }
                        Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(le).renderNameTag(le, Component.literal(sb.toString()).withStyle(v -> v.withFont(Elemental.prefix("symbols"))), ps, event.getMultiBufferSource(), event.getPackedLight());
                        ps.popPose();
                    }
                }
            });
        }
    }
}
