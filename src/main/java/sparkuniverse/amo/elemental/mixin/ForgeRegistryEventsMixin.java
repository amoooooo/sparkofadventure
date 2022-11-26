package sparkuniverse.amo.elemental.mixin;

import com.minecraftserverzone.mobhealthbar.ForgeRegistryEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sparkuniverse.amo.elemental.Elemental;
import sparkuniverse.amo.elemental.compat.MobHealthBar.MobHealthBarCompatLayer;
import sparkuniverse.amo.elemental.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.elemental.util.Pair;
import sparkuniverse.amo.elemental.util.SymbolHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(ForgeRegistryEvents.class)
public class ForgeRegistryEventsMixin {

    private static Entity entity;

    @Inject(method = "renderHpBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getName()Lnet/minecraft/network/chat/Component;", ordinal = 2), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void renderHPBarMixin(RenderNameTagEvent event, CallbackInfo ci, Minecraft minecraft, PoseStack matrixStackIn, float health, float maxhealth, double d0, Font fontrenderer, MultiBufferSource.BufferSource buffer, Matrix4f matrix4f, boolean mobVisible, boolean showOnlyWhenMobIsAggressive, boolean showBar, boolean shouldShowMob, boolean showPlayerNameTag, String[] blacklist, int posXAdd, int posYAdd, double scaleAdd, float f, int i) {
        if (MobHealthBarCompatLayer.isMobHealthBarLoaded) {
            if (event.getEntity() instanceof LivingEntity le) {
                entity = le;
            }
        }
    }

    @Redirect(method = "renderHpBar", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/gui/overlay/ForgeGui;drawString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    private static void drawStringMixin(PoseStack matrixStackIn, Font fontRendererIn, Component text, int x, int y, int color) {
        if (MobHealthBarCompatLayer.isMobHealthBarLoaded) {
            AtomicReference<MutableComponent> component = new AtomicReference<>(entity.getName().copy());
            entity.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(s -> {
                if (!s.getMarks().isEmpty()) {
                    List<String> marks = s.getMarks().stream().map(Pair::getFirst).toList();
                    StringBuilder sb = new StringBuilder();
                    for (String mark : marks) {
                        sb.append(SymbolHelper.getSymbol(mark));
                    }
                    component.set(component.get().append(Component.literal(sb.toString()).withStyle(v -> v.withFont(Elemental.prefix("symbols")).withColor(0xFFFFFF))));
                }
            });
            ForgeGui.drawString(matrixStackIn, fontRendererIn, component.get(), x, y, color);
        } else {
            ForgeGui.drawString(matrixStackIn, fontRendererIn, text, x, y, color);
        }
    }
}
