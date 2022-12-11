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

import java.util.concurrent.atomic.AtomicBoolean;

public class SelfShieldGoal extends Goal {
    private final Mob mob;
    private int cooldown = 0;
    public SelfShieldGoal(Mob mob) {
        Elemental.LOGGER.warn("SelfShieldGoal applied to {}", mob);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if(cooldown != 0)
            return false;
        AtomicBoolean cap = new AtomicBoolean(true);
        mob.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(s -> {
            if (s.hasShield()) {
                cap.set(false);
            }
        });
        if(!cap.get()) return false;
        if(!isLowEnough()) return false;
        float mobHealth = (mob.getHealth()/mob.getMaxHealth());
        float chance = mob.level.random.nextFloat() * (1-mobHealth);
//        Elemental.LOGGER.error("Mob health: {}, chance: {}, canShield: {}", mobHealth, chance, isLowEnough());
        if(chance <= 0.85f) {
            Elemental.LOGGER.error("FAIL: Mob health: {}, chance: {}, canShield: {}", mobHealth, chance, isLowEnough());
            return false;
        }
        Elemental.LOGGER.error("SUCCESS: Mob health: {}, chance: {}, canShield: {}", mobHealth, chance, isLowEnough());
        if(!AttributeRegistry.RESISTANCE_ATTRIBUTES.getEntries().stream().map(s -> s.get().equals(getHighestAttribute(mob))).toList().isEmpty()){
            cooldown = 200;
            return true;
        }
        if(mob.getCapability(ShieldCapabilityProvider.CAPABILITY).isPresent()){
            if(mob.getCapability(ShieldCapabilityProvider.CAPABILITY).resolve().get().getShield() == null){
                cooldown = 200;
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        cooldown--;
        mob.getCapability(ShieldCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            if(cap.getShield().getDefType().equals("none")){
                TypedRangedAttribute highestAttr = getHighestAttribute(mob);
                int attrValue = (int) mob.getAttribute((Attribute)highestAttr).getValue();
                Elemental.LOGGER.warn("Applying {} shield to {}", highestAttr.getType().get().getDescriptionId(), mob);
                Shield shield = new Shield(highestAttr.getType().get().getDescriptionId(), highestAttr.getType().get().getDescriptionId(), (int) (mob.getMaxHealth()/5f), (int) (mob.getMaxHealth()/5f));
                cap.setShield(shield);
                mob.playSound(SoundEvents.FIRE_EXTINGUISH, 1.5f, 0.35f);
                PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new ClientBoundShieldPacket(shield.maxHealth, 0, false, mob.getId(), true, highestAttr.getType().get().getDescriptionId()));
            }
        });
    }

    public static TypedRangedAttribute getHighestAttribute(Mob mob){
        TypedRangedAttribute highestAttr = (TypedRangedAttribute) AttributeRegistry.RESISTANCE_ATTRIBUTES.getEntries().stream().map(RegistryObject::get).toList().get(0);
        for(TypedRangedAttribute attr : AttributeRegistry.RESISTANCE_ATTRIBUTES.getEntries().stream().map(s -> (TypedRangedAttribute) s.get()).toList()){
            if(mob.getAttribute((Attribute)attr).getValue() < mob.getAttribute((Attribute)highestAttr).getValue()){
                highestAttr = attr;
            }
        }
        return highestAttr;
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

    public boolean isLowEnough(){
        return mob.getHealth() <= mob.getMaxHealth()/2f;
    }
}
