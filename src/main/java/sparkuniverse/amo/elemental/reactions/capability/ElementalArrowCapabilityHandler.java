package sparkuniverse.amo.elemental.reactions.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

public interface ElementalArrowCapabilityHandler extends INBTSerializable<CompoundTag> {
    void setElements(List<String> element);
    void addElement(String element);
    List<String> getElements();
    void clearElement();
    boolean hasElement();
    void setCharge(int charge);
    int getCharge();
    void clearCharge();
    boolean hasCharge();
}
