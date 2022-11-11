package sparkuniverse.amo.elemental.reactions.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import sparkuniverse.amo.elemental.damagetypes.Shield;

import java.util.List;

public interface ShieldCapabilityHandler extends INBTSerializable<CompoundTag> {
    int getShieldHealth();
    int getShieldMaxHealth();
    void setShield(Shield shield);
    void damageShield(float amount, String defType);
    void healShield(int amount, String defType);
    void clearShield();
    void removeShield(Shield shield);
    void removeShield(String defType);
    boolean hasShield(Shield shield);
    boolean hasShield(String defType);
    int getShieldCount();
    Shield getShield();
    boolean hasShield();
    String getShieldDefType();
    String getShieldBreakType();

}
