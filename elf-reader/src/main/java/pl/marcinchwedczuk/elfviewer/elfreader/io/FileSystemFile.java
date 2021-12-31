package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileSystemFile implements AbstractFile, AutoCloseable {
    private final RandomAccessFile file;

    public FileSystemFile(File file) {
        try {
            this.file = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            throw new ElfReaderException(e.getMessage(), e);
        }
    }

    @Override
    public byte read(long offset) {
        try {
            file.seek(offset);

            return file.readByte();
        } catch (IOException e) {
            throw new ElfReaderException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] read(long offset, int size) {
        try {
            file.seek(offset);

            byte[] buf = new byte[size];
            file.readFully(buf);
            return buf;
        } catch (IOException e) {
            throw new ElfReaderException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws Exception {
        file.close();
    }
}
