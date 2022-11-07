package sparkuniverse.amo.elemental.reactions.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import sparkuniverse.amo.elemental.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ReactionMarkCapability implements ReactionMarkCapabilityHandles{
    private final List<Pair<String, Integer>> marks = new ArrayList<>();
    @Override
    public String getMark(int index) {
        return marks.get(index).getFirst();
    }

    @Override
    public void setMark(int index, String mark) {
        marks.set(index, Pair.of(mark, 0));
    }

    @Override
    public void addMark(String mark) {
        marks.add(Pair.of(mark, 0));
    }

    @Override
    public void removeMark(int index) {
        marks.remove(index);
    }

    @Override
    public void removeMark(String mark) {
        marks.removeIf(pair -> pair.getFirst().equals(mark));
    }

    @Override
    public void clearMarks() {
        marks.clear();
    }

    @Override
    public int getMarkCount() {
        return marks.size();
    }

    @Override
    public boolean hasMark(String mark) {
        return marks.stream().anyMatch(pair -> pair.getFirst().equals(mark));
    }

    @Override
    public List<Pair<String, Integer>> getMarks() {
        return marks;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        marks.forEach(pair -> {
            CompoundTag tag = new CompoundTag();
            tag.putString("mark", pair.getFirst());
            tag.putInt("count", pair.getSecond());
            list.add(tag);
        });
        nbt.put("marks", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        marks.clear();
        ListTag list = nbt.getList("marks", 10);
        list.forEach(tag -> {
            CompoundTag compoundTag = (CompoundTag) tag;
            marks.add(Pair.of(compoundTag.getString("mark"), compoundTag.getInt("count")));
        });
    }
}
