package sparkuniverse.amo.sparkofadventure.reactions.effects;

import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import shadows.apotheosis.Apoth;
import sparkuniverse.amo.sparkofadventure.damagetypes.AttributeRegistry;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.sparkofadventure.reactions.entity.NatureCoreEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LastingEffectMap {
    public static Map<String, Consumer<LivingEntity>> lastingEffectMap = new HashMap<>();
    public static void init(){
        lastingEffectMap.put("sparkofadventure:acid_damage", (entity) -> {
            if(entity.level.random.nextFloat() > 0.959){
                entity.hurt(DamageSource.MAGIC, 0.25f);
                entity.invulnerableTime = 0;
            }
        });
        lastingEffectMap.put("sparkofadventure:air_damage", (entity) -> {
        });
        lastingEffectMap.put("sparkofadventure:earth_damage", (entity) -> {
        });
        lastingEffectMap.put("apotheosis:fire_damage", (entity) -> {
            entity.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(r -> {
                if(r.hasMark(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId())){
                    entity.hurt(DamageSource.IN_FIRE, 0.25f);
                    entity.level.getEntities(null, entity.getBoundingBox().inflate(0.1f)).forEach(e -> {
                        e.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(r2 -> {
                            if(!r2.hasMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId())){
                                r2.addMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId());
                            }
                        });
                    });

                }
            });
        });
        lastingEffectMap.put("apotheosis:cold_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
            if(entity.level.random.nextFloat() > 0.959){
                entity.hurt(DamageSource.FREEZE, 0.25f);
                entity.invulnerableTime = 0;
            }
        });
        lastingEffectMap.put("sparkofadventure:lightning_damage", (entity) -> {
            if(entity.hasEffect(ReactionEffects.ELECTROCHARGED.get())){
                if(entity.level.random.nextFloat() > 0.959){
                    entity.level.getEntities(null, entity.getBoundingBox().inflate(2)).forEach((entity1) -> {
                        if(entity1 instanceof LivingEntity && !(entity1 instanceof Player)){
                            entity1.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent((cap) -> {
                                if(!cap.hasMark("sparkofadventure:lightning_damage")){
                                    cap.addMark("sparkofadventure:lightning_damage");
                                }
                            });
                            entity1.hurt(DamageSource.LIGHTNING_BOLT, 0.15f);
                            entity.invulnerableTime = 0;
                        }
                    });
                }
            }
        });
        lastingEffectMap.put("sparkofadventure:water_damage", (entity) -> {
            if(entity.level.random.nextFloat() > 0.959){
                entity.hurt(DamageSource.DROWN, 0.15f);
                entity.invulnerableTime = 0;
            }
        });
        lastingEffectMap.put("sparkofadventure:poison_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1));
        });
        lastingEffectMap.put("sparkofadventure:necrotic_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20, 1));
        });
        lastingEffectMap.put("sparkofadventure:radiant_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2, 1));
        });
        lastingEffectMap.put("sparkofadventure:psychic_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 2, 1));
            if(entity.getAttribute(Attributes.ATTACK_DAMAGE) != null){
                if(entity.level.random.nextFloat() > 0.959){
                    entity.doHurtTarget(entity);
                }
            }
            entity.invulnerableTime = 0;
        });
        lastingEffectMap.put("sparkofadventure:force_damage", (entity) -> {
            entity.addEffect(new MobEffectInstance(Apoth.Effects.SUNDERING.get(), 1, 1));
        });
        lastingEffectMap.put("sparkofadventure:thunder_damage", (entity) -> {
            RandomSource random = entity.level.random;
            entity.setDeltaMovement(entity.getDeltaMovement().add((random.nextFloat()-0.5)/10f, 0, (random.nextFloat()-0.5)/10f));
        });
        lastingEffectMap.put("sparkofadventure:nature_damage", (entity) -> {
            if(entity instanceof NatureCoreEntity) return;
            if(entity.level.random.nextFloat() > 0.959){
                entity.hurt(DamageSource.CACTUS, 0.15f);
                entity.invulnerableTime = 0;
            }
        });

    }
}
