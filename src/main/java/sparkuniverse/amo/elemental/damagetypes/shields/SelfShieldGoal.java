package sparkuniverse.amo.elemental.damagetypes.shields;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;
import sparkuniverse.amo.elemental.Elemental;
import sparkuniverse.amo.elemental.damagetypes.AttributeRegistry;
import sparkuniverse.amo.elemental.damagetypes.TypedRangedAttribute;
import sparkuniverse.amo.elemental.net.ClientBoundShieldPacket;
import sparkuniverse.amo.elemental.net.PacketHandler;
import sparkuniverse.amo.elemental.reactions.capability.ShieldCapabilityProvider;

public class SelfShieldGoal extends Goal {
    private final Mob mob;

    public SelfShieldGoal(Mob mob) {
        Elemental.LOGGER.warn("SelfShieldGoal applied to {}", mob);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        float mobHealth = 1-(mob.getHealth() / mob.getMaxHealth());
        if(mob.level.random.nextFloat() >= Math.min((0.05f + mobHealth), 0.15f)) return false;
        if(AttributeRegistry.RESISTANCE_ATTRIBUTES.getEntries().stream().map(RegistryObject::get).anyMatch(attribute -> mob.getAttribute(attribute).getValue() > 0)){
            return true;
        }
        if(mob.getCapability(ShieldCapabilityProvider.CAPABILITY).isPresent()){
            if(mob.getCapability(ShieldCapabilityProvider.CAPABILITY).resolve().get().getShield() == null){
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        mob.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            if(cap.getShield().getDefType().equals("none")){
                TypedRangedAttribute highestAttr = (TypedRangedAttribute) AttributeRegistry.RESISTANCE_ATTRIBUTES.getEntries().stream().map(RegistryObject::get).filter(attribute -> mob.getAttribute(attribute).getValue() > 0).max((attribute1, attribute2) -> Double.compare(mob.getAttribute(attribute1).getValue(), mob.getAttribute(attribute2).getValue())).orElse(null);
                int attrValue = (int) mob.getAttribute((Attribute)highestAttr).getValue();
                Elemental.LOGGER.warn("Applying {} shield to {}", highestAttr.getType().get().getDescriptionId(), mob);
                Shield shield = new Shield(highestAttr.getType().get().getDescriptionId(), highestAttr.getType().get().getDescriptionId(), attrValue/10, attrValue/10);
                cap.setShield(shield);
                mob.playSound(SoundEvents.FIRE_EXTINGUISH, 1.5f, 0.35f);
                PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientBoundShieldPacket(shield.maxHealth, 0, false, mob.getId(), true, highestAttr.getType().get().getDescriptionId()));
            }
        });
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean canContinueToUse() {
        if(mob.getCapability(ShieldCapabilityProvider.CAPABILITY).isPresent()){
            if(mob.getCapability(ShieldCapabilityProvider.CAPABILITY).resolve().get().getShield() != null){
                return false;
            }
        }
        return true;
    }
}
