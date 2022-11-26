package sparkuniverse.amo.elemental.reactions.effects.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import sparkuniverse.amo.elemental.Elemental;

public class TextParticle {
    public Component damage;
    public int color;

    public double x = 0;
    public double y = 0;
    public double z = 0;
    public double xPrev = 0;
    public double yPrev = 0;
    public double zPrev = 0;

    public int age = 0;

    public double ax = 0.00;
    public double ay = -0.01;
    public double az = 0.00;

    public double vx = 0;
    public double vy = 0;
    public double vz = 0;

    public TextParticle(Entity entity, Component damage, int color) {
        Minecraft client = Minecraft.getInstance();
        Vec3 entityLocation = entity.position().add(0, entity.getBbHeight() / 2, 0);
        Vec3 cameraLocation = client.gameRenderer.getMainCamera().getPosition();
        double offsetBy = entity.getBbWidth();
        Vec3 offset = cameraLocation.subtract(entityLocation).normalize().scale(offsetBy);
        Vec3 pos = entityLocation.add(offset);

        age = 0;
        this.damage = damage;
        this.color = color;

        vx = Elemental.RAND.nextGaussian() * 0.04;
        vy = 0.05 + (Elemental.RAND.nextGaussian() * 0.002);
        vz = Elemental.RAND.nextGaussian() * 0.04;

        x = pos.x + (Elemental.RAND.nextGaussian() * 0.25);
        y = pos.y;
        z = pos.z + (Elemental.RAND.nextGaussian() * 0.25);

        xPrev = x;
        yPrev = y;
        zPrev = z;
    }

    public void tick() {
        xPrev = x;
        yPrev = y;
        zPrev = z;
        age++;
        x += vx;
        y += vy;
        z += vz;
        vx += ax;
        vy += ay;
        vz += az;
    }

}
