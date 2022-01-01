package pl.marcinchwedczuk.elfviewer.elfreader.io;

import pl.marcinchwedczuk.elfviewer.elfreader.ElfReaderException;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfOffset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public final class FileView implements AbstractFile {
    private final AbstractFile file;
    private final long viewStartOffsetInFile;
    private final long viewLength;

    public FileView(AbstractFile file,
                    ElfOffset<?> start,
                    long viewLength) {
        if (viewLength < 0)
            throw new IllegalArgumentException("Length cannot be negative.");

        this.file = requireNonNull(file);
        this.viewStartOffsetInFile = start.value().longValue();
        this.viewLength = viewLength;
    }

    public long viewOffsetToFileOffset(long offset) {
        return viewStartOffsetInFile + offset;
    }

    public long viewLength() {
        return viewLength;
    }

    @Override
    public byte read(long offset) {
        checkOffset(offset, 1);
        return file.read(viewStartOffsetInFile + offset);
    }

    @Override
    public byte[] read(long offset, int size) {
        checkOffset(offset, size);
        return file.read(viewStartOffsetInFile + offset, size);
    }

    @Override
    public int readIntoBuffer(long offset, byte[] buffer) {
        if (offset >= viewLength)
            return -1;

        int nbytes = file.readIntoBuffer(viewStartOffsetInFile + offset, buffer);
        return Math.min(nbytes, Math.toIntExact(viewLength - offset));
    }

    private void checkOffset(long offset, int size) {
        if (offset < 0 || offset+size > viewLength)
            throw new IllegalArgumentException(String.format(
                    "Attempt to read data outside file view range, " +
                            "File view length is %d, requested range is [%d, %d).",
                    viewLength, offset, offset + size));
    }

    public void saveToFile(File f) {
        try {
            try (FileOutputStream outputStream = new FileOutputStream(f)) {
                byte[] buffer = new byte[1024 * 4];
                long offset = 0;

                int nbytes;
                while ((nbytes = readIntoBuffer(offset, buffer)) > 0) {
                    outputStream.write(buffer, 0, nbytes);
                    offset += nbytes;
                }

                if (offset != viewLength)
                    throw new ElfReaderException(String.format(
                        "Saving data to file %s failed. Not all bytes could be written.", f.getName()));
            }
        } catch (IOException e) {
            throw new ElfReaderException(e.getMessage(), e);
        }
    }
}
