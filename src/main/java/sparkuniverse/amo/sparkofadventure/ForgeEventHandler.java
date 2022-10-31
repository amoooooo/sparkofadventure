package sparkuniverse.amo.sparkofadventure;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sparkuniverse.amo.sparkofadventure.damagetypes.AttributeRegistry;
import sparkuniverse.amo.sparkofadventure.damagetypes.DamageTypeJSONListener;
import sparkuniverse.amo.sparkofadventure.damagetypes.TypedRangedAttribute;
import sparkuniverse.amo.sparkofadventure.reactions.Reaction;
import sparkuniverse.amo.sparkofadventure.reactions.ReactionRegistry;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityHandles;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityProvider;
import sparkuniverse.amo.sparkofadventure.reactions.effects.particle.CubeParticleData;
import sparkuniverse.amo.sparkofadventure.util.ColorHelper;
import sparkuniverse.amo.sparkofadventure.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = SparkOfAdventure.MODID)
public class ForgeEventHandler {

    private static boolean avoidSO;

    @SubscribeEvent
    public static void attackEvent(final LivingAttackEvent event) {
        if (event.getEntity().level.isClientSide || avoidSO) return;

        avoidSO = true;

        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker) {
            final LivingEntity hurtEntity = event.getEntity();
            final EntityType<?> entityType = event.getEntity().getType();

            if (DamageTypeJSONListener.attributeResistances.containsKey(entityType)) {
                final Map<Attribute, Double> resistanceMap = DamageTypeJSONListener.attributeResistances.get(entityType);
                for (Map.Entry<Attribute, Double> mapEntry : resistanceMap.entrySet()) {
                    final Attribute attribute = mapEntry.getKey();
                    double mobResistance = hurtEntity.getAttributeValue(attribute);
                    Attribute playerAtt = ((TypedRangedAttribute)attribute).getType().get();
                    double attributeDamage = attacker.getAttributes().hasAttribute(playerAtt) ? attacker.getAttributeValue(playerAtt) : 0.0;
                    if (attributeDamage > 0.001 && hurtEntity.getAttributes().hasAttribute(attribute)) {
                        hurtEntity.hurt(src(attacker), (float) (event.getAmount() * mobResistance));
                        if(mobResistance > 1){
                            hurtEntity.level.playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.5f, 0.75f);
                        } else if (mobResistance < 1){
                            hurtEntity.level.playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.5f, 1.25f);
                        }
                        hurtEntity.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
                            if(!cap.hasMark(playerAtt.getDescriptionId())){
                                cap.addMark(playerAtt.getDescriptionId());
                            }
                            Reaction reaction = ReactionRegistry.getReaction(playerAtt.getDescriptionId(), cap.getMarks().stream().map(Pair::getFirst).collect(Collectors.toList()));
                            if(reaction != null){
                                reaction.applyReaction(hurtEntity, attacker, attributeDamage);
                                cap.removeMark(playerAtt.getDescriptionId());
                                cap.removeMark(reaction.getOther(playerAtt.getDescriptionId()));
                            }
                        });
                        event.setCanceled(true);
                    }
                }
            }
        }
        avoidSO = false;
    }

    /**
     * Credit to this method and the above boolean goes to Apotheosis and ShadowsOfFire
     * @param entity
     * @return A DamageSource pertaining to whatever living entity caused the attack.
     */

    private static DamageSource src(LivingEntity entity) {
        return entity instanceof Player p ? DamageSource.playerAttack(p) : DamageSource.mobAttack(entity);
    }

    @SubscribeEvent
    public static void onJsonListener(AddReloadListenerEvent event){
        DamageTypeJSONListener.register(event);
    }


    @SubscribeEvent
    public static void mobSpawn(final LivingSpawnEvent event){
        if (event.getEntity().level.isClientSide) return;

        if (DamageTypeJSONListener.attributeResistances.containsKey(event.getEntity().getType())) {
            final LivingEntity entity = event.getEntity();
            final Map<Attribute, Double> attributeResistanceMap = DamageTypeJSONListener.attributeResistances.get(entity.getType());

            for (Map.Entry<Attribute, Double> entry : attributeResistanceMap.entrySet()) {
                entity.getAttribute(entry.getKey()).setBaseValue(entry.getValue());
            }
        }
    }

    @SubscribeEvent
    public static void entityTick(LivingEvent.LivingTickEvent event){
        if (event.getEntity().level.isClientSide) return;
        if(event.getEntity() instanceof Player) return;
        event.getEntity().getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            if(!cap.getMarks().isEmpty()){
                List<String> toRemove = new ArrayList<>();
                cap.getMarks().forEach(s -> {
                    if(s.getSecond() < 100) {
                        s.setSecond(s.getSecond() + 1);
                    } else {
                        toRemove.add(s.getFirst());
                    }
                    Color color = new Color(ColorHelper.typeColorMap.get(s.getFirst()));
                    CubeParticleData particleData = new CubeParticleData(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.025f, 1 ,false);
                    event.getEntity().level.getServer().overworld().sendParticles(particleData, event.getEntity().getX() + (event.getEntity().level.random.nextFloat() - 0.5), event.getEntity().getY()+1 + (event.getEntity().level.random.nextFloat() - 0.5), event.getEntity().getZ() + (event.getEntity().level.random.nextFloat() - 0.5), 3, 0, 0, 0, 0.1);
                });
                toRemove.forEach(cap::removeMark);
            }});
    }


    @SubscribeEvent
    public static void attachCaps(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof LivingEntity){
            event.addCapability(ReactionMarkCapabilityProvider.ID, new ReactionMarkCapabilityProvider());
        }
    }

}
