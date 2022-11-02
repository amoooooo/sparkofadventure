package sparkuniverse.amo.alkahest.reactions.effects.particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleStates {
    public static List<TextParticle> PARTICLES = new ArrayList<>();

    public static void tick() {
        PARTICLES.forEach(TextParticle::tick);
        PARTICLES.removeIf(p -> p.age > 50);
    }
}
