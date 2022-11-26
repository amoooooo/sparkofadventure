package sparkuniverse.amo.elemental.reactions.effects.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ExtraRenderer {
    public static void drawDamageNumber(PoseStack matrix, Component dmg, double x, double y,
                                        float width, int color) {
        Minecraft minecraft = Minecraft.getInstance();
        int sw = minecraft.font.width(dmg);
        //minecraft.font.draw(matrix, dmg, (int) (x + (width / 2) - sw), (int) y + 5, color);
        minecraft.font.drawShadow(matrix, dmg, (int) (x + (width / 2) - sw), (int) y + 5, color);
    }
}
