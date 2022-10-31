package sparkuniverse.amo.sparkofadventure.reactions.entity;

import io.netty.util.AttributeMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import shadows.apotheosis.Apoth;
import sparkuniverse.amo.sparkofadventure.damagetypes.AttributeRegistry;
import sparkuniverse.amo.sparkofadventure.reactions.capability.ReactionMarkCapabilityProvider;

public class NatureCoreEntity extends Mob {

    public NatureCoreEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.tickCount > 100) {
            this.remove(RemovalReason.DISCARDED);
        }
        this.getCapability(ReactionMarkCapabilityProvider.CAPABILITY).ifPresent(cap -> {
            if(!cap.hasMark(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId())){
                cap.addMark(AttributeRegistry.NATURE_DAMAGE.get().getDescriptionId());
            }
            if(cap.hasMark(Apoth.Attributes.FIRE_DAMAGE.get().getDescriptionId())){
                this.remove(RemovalReason.DISCARDED);
            }
        });
    }

    @Override
    protected void defineSynchedData() {

    }


    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return null;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
