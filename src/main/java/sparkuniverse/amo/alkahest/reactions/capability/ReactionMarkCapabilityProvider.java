package sparkuniverse.amo.alkahest.reactions.capability;

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

public class ReactionMarkCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    public static final ResourceLocation ID = new ResourceLocation("alkahest:reaction_mark");
    public static final Capability<ReactionMarkCapabilityHandles> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    private final LazyOptional<ReactionMarkCapability> implContainer = LazyOptional.of(ReactionMarkCapability::new);

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

    public static ReactionMarkCapabilityHandles getOrDefault(LivingEntity entity){
        return entity.getCapability(CAPABILITY).orElse(new ReactionMarkCapability());
    }
}
