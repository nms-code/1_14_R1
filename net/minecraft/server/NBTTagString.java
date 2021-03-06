package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class NBTTagString implements NBTBase {

    private String data;

    public NBTTagString() {
        this("");
    }

    public NBTTagString(String s) {
        Objects.requireNonNull(s, "Null string not allowed");
        this.data = s;
    }

    @Override
    public void write(DataOutput dataoutput) throws IOException {
        dataoutput.writeUTF(this.data);
    }

    @Override
    public void load(DataInput datainput, int i, NBTReadLimiter nbtreadlimiter) throws IOException {
        nbtreadlimiter.a(288L);
        this.data = datainput.readUTF();
        nbtreadlimiter.a((long) (16 * this.data.length()));
    }

    @Override
    public byte getTypeId() {
        return 8;
    }

    @Override
    public String toString() {
        return a(this.data);
    }

    @Override
    public NBTTagString clone() {
        return new NBTTagString(this.data);
    }

    public boolean equals(Object object) {
        return this == object ? true : object instanceof NBTTagString && Objects.equals(this.data, ((NBTTagString) object).data);
    }

    public int hashCode() {
        return this.data.hashCode();
    }

    @Override
    public String asString() {
        return this.data;
    }

    @Override
    public IChatBaseComponent a(String s, int i) {
        String s1 = a(this.data);
        String s2 = s1.substring(0, 1);
        IChatBaseComponent ichatbasecomponent = (new ChatComponentText(s1.substring(1, s1.length() - 1))).a(NBTTagString.c);

        return (new ChatComponentText(s2)).addSibling(ichatbasecomponent).a(s2);
    }

    public static String a(String s) {
        StringBuilder stringbuilder = new StringBuilder(" ");
        int i = 0;

        for (int j = 0; j < s.length(); ++j) {
            char c0 = s.charAt(j);

            if (c0 == '\\') {
                stringbuilder.append('\\');
            } else if (c0 == '"' || c0 == '\'') {
                if (i == 0) {
                    i = c0 == '"' ? 39 : 34;
                }

                if (i == c0) {
                    stringbuilder.append('\\');
                }
            }

            stringbuilder.append(c0);
        }

        if (i == 0) {
            i = 34;
        }

        stringbuilder.setCharAt(0, (char) i);
        stringbuilder.append((char) i);
        return stringbuilder.toString();
    }
}
