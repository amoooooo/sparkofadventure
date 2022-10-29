package sparkuniverse.amo.sparkofadventure;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

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

                    double attributeDamage = attacker.getAttributes().hasAttribute(attribute) ? attacker.getAttributeValue(attribute) : 0.0;

                    if (attributeDamage > 0.001 && hurtEntity.getAttributes().hasAttribute(attribute)) {
                        hurtEntity.hurt(src(attacker), (float) (event.getAmount() * mapEntry.getValue()));
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
     * @return A DamageSource pertaigning to whatever living entity caused the attack.
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

}
