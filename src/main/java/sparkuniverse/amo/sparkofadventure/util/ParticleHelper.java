package sparkuniverse.amo.sparkofadventure.util;

import net.minecraft.world.level.Level;
import sparkuniverse.amo.sparkofadventure.reactions.effects.particle.CubeParticleData;

import java.awt.*;

public class ParticleHelper {
    public static void particleBurst(double x, double y, double z, int amount, double speed, double spread, int color, Level level) {
        for (int i = 0; i < amount; i++) {
            double xSpeed = (Math.random() - 0.5) * speed;
            double ySpeed = (Math.random() - 0.5) * speed;
            double zSpeed = (Math.random() - 0.5) * speed;
            double xSpread = (Math.random() - 0.5) * spread;
            double ySpread = (Math.random() - 0.5) * spread;
            double zSpread = (Math.random() - 0.5) * spread;
            ParticleHelper.spawnParticle(x + xSpread, y + ySpread, z + zSpread, xSpeed, ySpeed, zSpeed, color, level);
        }
    }
    public static void particleCircle(double x, double y, double z, int amount, double speed, double spread, int color, Level level) {
        for (int i = 0; i < amount; i++) {
            double angle = Math.random() * 2 * Math.PI;
            double xSpeed = Math.cos(angle) * speed;
            double ySpeed = (Math.random() - 0.5) * speed;
            double zSpeed = Math.sin(angle) * speed;
            double xSpread = (Math.random() - 0.5) * spread;
            double ySpread = (Math.random() - 0.5) * spread;
            double zSpread = (Math.random() - 0.5) * spread;
            ParticleHelper.spawnParticle(x + xSpread, y + ySpread, z + zSpread, xSpeed, ySpeed, zSpeed, color, level);
        }
    }

    public static void spawnParticle(double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int color, Level level) {
        Color c = new Color(color);
        CubeParticleData particle = new CubeParticleData(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 0.05f, 1, false);
        if(!level.isClientSide)
            level.getServer().getLevel(level.dimension()).sendParticles(particle, x, y, z, 1, 0,0, 0, ySpeed);
    }
}
