package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FileRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD>
{
    private final File elfFile;

    public FileRenderer(NativeWord<NATIVE_WORD> nativeWord,
                        File elfFile) {
        super(nativeWord);
        this.elfFile = elfFile;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("(field)", indexAccessor(0)),
                mkColumn("(value)", indexAccessor(1))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        Path path = elfFile.toPath();
        List<String[]> fields = new ArrayList<>();

        try {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            fields.addAll(List.of(
                    mkStrings("name", path.getFileName().toString()),
                    mkStrings("path", path.toAbsolutePath().toString()),
                    mkStrings("size", dec(attr.size())), // TODO: KB MB etc.
                    mkStrings("ctime", attr.creationTime().toString()),
                    mkStrings("mtime", attr.lastModifiedTime().toString()),
                    mkStrings("atime", attr.lastAccessTime().toString())
            ));

            // This may result in an error on e.g. Windows filesystem
            PosixFileAttributes posixAttr = Files.readAttributes(path, PosixFileAttributes.class);
            fields.addAll(List.of(
               mkStrings("owner", posixAttr.owner().toString()),
               mkStrings("group", posixAttr.group().toString()),
               mkStrings("perms", PosixFilePermissions.toString(posixAttr.permissions()))
            ));
        } catch (IOException e) {
            fields.add(mkStrings("(error)", e.getMessage()));
        }

        return fields;
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }
}
