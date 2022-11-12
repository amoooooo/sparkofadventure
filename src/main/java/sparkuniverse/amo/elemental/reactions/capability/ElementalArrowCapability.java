package sparkuniverse.amo.elemental.reactions.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

import java.util.ArrayList;
import java.util.List;

public class ElementalArrowCapability implements ElementalArrowCapabilityHandler{
    private List<String> elements = new ArrayList<>();
    private int charge;
    @Override
    public void setElements(List<String> element) {
        this.elements = element;
    }

    @Override
    public void addElement(String element) {
        this.elements.add(element);
    }

    @Override
    public List<String> getElements() {
        return elements;
    }

    @Override
    public void clearElement() {
        elements.clear();
    }

    @Override
    public boolean hasElement() {
        return elements.size() > 0;
    }

    @Override
    public void setCharge(int charge) {
        this.charge = charge;
    }

    @Override
    public int getCharge() {
        return charge;
    }

    @Override
    public void clearCharge() {
        charge = 0;
    }

    @Override
    public boolean hasCharge() {
        return charge > 0;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("charge", charge);
        ListTag list = new ListTag();
        for (String element : elements) {
            list.add(StringTag.valueOf(element));
        }
        nbt.put("elements", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        charge = nbt.getInt("charge");
        ListTag list = nbt.getList("elements", 8);
        for (int i = 0; i < list.size(); i++) {
            elements.add(list.getString(i));
        }
    }
}
