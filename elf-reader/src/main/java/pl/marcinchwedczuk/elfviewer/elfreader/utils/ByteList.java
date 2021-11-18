package pl.marcinchwedczuk.elfviewer.elfreader.utils;

import java.nio.charset.StandardCharsets;

public class ByteList {
    private byte[] storage;
    private int size;

    public ByteList() {
        this(8);
    }

    public ByteList(int initialCapacity) {
        this.storage = new byte[initialCapacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    private void ensureCapacity(int newSize) {
        int capacity = storage.length;
        if (newSize > capacity) {
            int newCapacity = Math.max(newSize, capacity*2);
            byte[] newStorage = new byte[newCapacity];
            System.arraycopy(storage, 0, newStorage, 0, size);
            this.storage = newStorage;
        }
    }

    public void add(byte value) {
        ensureCapacity(size+1);
        this.storage[size] = value;
        this.size += 1;
    }

    public void put(int index, byte value) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(index);

        this.storage[index] = value;
    }

    public byte get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(index);

        return this.storage[index];
    }

    public String toAsciiString() {
        return new String(storage, 0, size, StandardCharsets.US_ASCII);
    }

    public byte[] toByteArray() {
        byte[] result = new byte[size];
        System.arraycopy(storage, 0, result, 0, size);
        return result;
    }
}
