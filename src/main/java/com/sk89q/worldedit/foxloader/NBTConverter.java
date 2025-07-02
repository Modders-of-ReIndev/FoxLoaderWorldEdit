package com.sk89q.worldedit.foxloader;

import com.sk89q.jnbt.*;

import java.util.*;

public class NBTConverter {

    private NBTConverter() {
    }

    public static com.mojang.nbt.Tag toNative(Tag tag) {
        if (tag instanceof IntArrayTag) {
            return toNative((IntArrayTag) tag);

        } else if (tag instanceof ListTag) {
            return toNative((ListTag) tag);

        } else if (tag instanceof LongTag) {
            return toNative((LongTag) tag);

        } else if (tag instanceof StringTag) {
            return toNative((StringTag) tag);

        } else if (tag instanceof IntTag) {
            return toNative((IntTag) tag);

        } else if (tag instanceof ByteTag) {
            return toNative((ByteTag) tag);

        } else if (tag instanceof ByteArrayTag) {
            return toNative((ByteArrayTag) tag);

        } else if (tag instanceof CompoundTag) {
            return toNative((CompoundTag) tag);

        } else if (tag instanceof FloatTag) {
            return toNative((FloatTag) tag);

        } else if (tag instanceof ShortTag) {
            return toNative((ShortTag) tag);

        } else if (tag instanceof DoubleTag) {
            return toNative((DoubleTag) tag);
        } else {
            throw new IllegalArgumentException("Can't convert tag of type " + tag.getClass().getCanonicalName());
        }
    }

    public static com.mojang.nbt.IntArrayTag toNative(IntArrayTag tag) {
        int[] value = tag.getValue();
        return new com.mojang.nbt.IntArrayTag(Arrays.copyOf(value, value.length));
    }

    public static com.mojang.nbt.ListTag<com.mojang.nbt.Tag> toNative(ListTag tag) {
        com.mojang.nbt.ListTag<com.mojang.nbt.Tag> list = new com.mojang.nbt.ListTag<>();
        for (Tag child : tag.getValue()) {
            if (child instanceof EndTag) {
                continue;
            }
            list.setTag(toNative(child));
        }
        return list;
    }

    public static com.mojang.nbt.LongTag toNative(LongTag tag) {
        return new com.mojang.nbt.LongTag(tag.getValue());
    }

    public static com.mojang.nbt.StringTag toNative(StringTag tag) {
        return new com.mojang.nbt.StringTag(tag.getValue());
    }

    public static com.mojang.nbt.IntTag toNative(IntTag tag) {
        return new com.mojang.nbt.IntTag(tag.getValue());
    }

    public static com.mojang.nbt.ByteTag toNative(ByteTag tag) {
        return new com.mojang.nbt.ByteTag(tag.getValue());
    }

    public static com.mojang.nbt.ByteArrayTag toNative(ByteArrayTag tag) {
        byte[] value = tag.getValue();
        return new com.mojang.nbt.ByteArrayTag(Arrays.copyOf(value, value.length));
    }

    public static com.mojang.nbt.CompoundTag toNative(CompoundTag tag) {
        com.mojang.nbt.CompoundTag compound = new com.mojang.nbt.CompoundTag();
        for (Map.Entry<String, Tag> child : tag.getValue().entrySet()) {
            compound.setTag(child.getKey(), toNative(child.getValue()));
        }
        return compound;
    }

    public static com.mojang.nbt.FloatTag toNative(FloatTag tag) {
        return new com.mojang.nbt.FloatTag(tag.getValue());
    }

    public static com.mojang.nbt.ShortTag toNative(ShortTag tag) {
        return new com.mojang.nbt.ShortTag(tag.getValue());
    }

    public static com.mojang.nbt.DoubleTag toNative(DoubleTag tag) {
        return new com.mojang.nbt.DoubleTag(tag.getValue());
    }

    public static Tag fromNative(com.mojang.nbt.Tag other) {
        if (other instanceof com.mojang.nbt.IntArrayTag) {
            return fromNative((com.mojang.nbt.IntArrayTag) other);

        } else if (other instanceof com.mojang.nbt.ListTag) {
            return fromNative((com.mojang.nbt.ListTag<?>) other);

        } else if (other instanceof com.mojang.nbt.EndTag) {
            return fromNative((com.mojang.nbt.EndTag) other);

        } else if (other instanceof com.mojang.nbt.LongTag) {
            return fromNative((com.mojang.nbt.LongTag) other);

        } else if (other instanceof com.mojang.nbt.StringTag) {
            return fromNative((com.mojang.nbt.StringTag) other);

        } else if (other instanceof com.mojang.nbt.IntTag) {
            return fromNative((com.mojang.nbt.IntTag) other);

        } else if (other instanceof com.mojang.nbt.ByteTag) {
            return fromNative((com.mojang.nbt.ByteTag) other);

        } else if (other instanceof com.mojang.nbt.ByteArrayTag) {
            return fromNative((com.mojang.nbt.ByteArrayTag) other);

        } else if (other instanceof com.mojang.nbt.CompoundTag) {
            return fromNative((com.mojang.nbt.CompoundTag) other);

        } else if (other instanceof com.mojang.nbt.FloatTag) {
            return fromNative((com.mojang.nbt.FloatTag) other);

        } else if (other instanceof com.mojang.nbt.ShortTag) {
            return fromNative((com.mojang.nbt.ShortTag) other);

        } else if (other instanceof com.mojang.nbt.DoubleTag) {
            return fromNative((com.mojang.nbt.DoubleTag) other);
        } else {
            throw new IllegalArgumentException("Can't convert other of type " + other.getClass().getCanonicalName());
        }
    }

    public static IntArrayTag fromNative(com.mojang.nbt.IntArrayTag other) {
        int[] value = other.getIntArray();
        return new IntArrayTag(Arrays.copyOf(value, value.length));
    }

    public static ListTag fromNative(com.mojang.nbt.ListTag<?> other) {
        List<Tag> list = new ArrayList<>();
        Class<? extends Tag> listClass = StringTag.class;
        for (com.mojang.nbt.Tag tag : other) {
            Tag child = fromNative(tag);
            list.add(child);
            listClass = child.getClass();
        }
        return new ListTag(listClass, list);
    }

    public static EndTag fromNative(com.mojang.nbt.EndTag other) {
        return new EndTag();
    }

    public static LongTag fromNative(com.mojang.nbt.LongTag other) {
        return new LongTag(other.getLong());
    }

    public static StringTag fromNative(com.mojang.nbt.StringTag other) {
        return new StringTag(other.data);
    }

    public static IntTag fromNative(com.mojang.nbt.IntTag other) {
        return new IntTag(other.getInt());
    }

    public static ByteTag fromNative(com.mojang.nbt.ByteTag other) {
        return new ByteTag(other.getByte());
    }

    public static ByteArrayTag fromNative(com.mojang.nbt.ByteArrayTag other) {
        byte[] value = other.getByteArray();
        return new ByteArrayTag(Arrays.copyOf(value, value.length));
    }

    public static CompoundTag fromNative(com.mojang.nbt.CompoundTag other) {
        Map<String, com.mojang.nbt.Tag> tags = other.copyMap();
        Map<String, Tag> map = new HashMap<>();
        for (Map.Entry<String, com.mojang.nbt.Tag> entry : tags.entrySet()) {
            map.put(entry.getKey(), fromNative(entry.getValue()));
        }
        return new CompoundTag(map);
    }

    public static FloatTag fromNative(com.mojang.nbt.FloatTag other) {
        return new FloatTag(other.getFloat());
    }

    public static ShortTag fromNative(com.mojang.nbt.ShortTag other) {
        return new ShortTag(other.getShort());
    }

    public static DoubleTag fromNative(com.mojang.nbt.DoubleTag other) {
        return new DoubleTag(other.getDouble());
    }
}
