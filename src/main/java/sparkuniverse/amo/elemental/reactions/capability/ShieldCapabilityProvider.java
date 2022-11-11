package sparkuniverse.amo.elemental.reactions.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShieldCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation ID = new ResourceLocation("elemental:shield");
    public static final Capability<ShieldCapabilityHandler> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    private final LazyOptional<ShieldCapability> implContainer = LazyOptional.of(ShieldCapability::new);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return CAPABILITY == cap ? implContainer.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if(!implContainer.isPresent())
            return new CompoundTag();
        return implContainer.resolve().get().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        implContainer.ifPresent(cap -> cap.deserializeNBT(nbt));
    }

    public static ShieldCapabilityHandler getOrDefault(LivingEntity entity){
        return entity.getCapability(CAPABILITY).orElse(new ShieldCapability());
    }
}
