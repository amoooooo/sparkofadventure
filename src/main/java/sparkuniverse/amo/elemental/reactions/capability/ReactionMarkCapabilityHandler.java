package sparkuniverse.amo.elemental.reactions.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import sparkuniverse.amo.elemental.util.Pair;

import java.util.List;

public interface ReactionMarkCapabilityHandler extends INBTSerializable<CompoundTag> {
    String getMark(int index);
    void setMark(int index, String mark);
    void addMark(String mark);
    void removeMark(int index);
    void removeMark(String mark);
    void clearMarks();
    int getMarkCount();
    boolean hasMark(String mark);
    List<Pair<String, Integer>> getMarks();
}
