package sparkuniverse.amo.sparkofadventure.reactions.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.common.util.INBTSerializable;
import sparkuniverse.amo.sparkofadventure.util.Pair;

import java.util.List;

public interface ReactionMarkCapabilityHandles extends INBTSerializable<CompoundTag> {
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
