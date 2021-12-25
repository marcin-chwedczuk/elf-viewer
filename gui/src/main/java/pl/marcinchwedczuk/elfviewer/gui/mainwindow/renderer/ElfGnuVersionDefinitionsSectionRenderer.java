package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuVersionDefinitionsSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionDefinition;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionDefinitionAuxiliary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ElfGnuVersionDefinitionsSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD> {
    private final ElfGnuVersionDefinitionsSection<NATIVE_WORD> section;

    public ElfGnuVersionDefinitionsSectionRenderer(
            NativeWord<NATIVE_WORD> nativeWord,
            ElfGnuVersionDefinitionsSection<NATIVE_WORD> section) {
        super(nativeWord);
        this.section = section;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("(type)", indexAccessor(0)),
                mkColumn(titleDouble("vd_version", "vda_name"), indexAccessor(1)),
                mkColumn(titleDouble("vd_flags", "(vda_name)"), indexAccessor(2)),
                mkColumn(titleDouble("vd_ndx", "vda_next"), indexAccessor(3)),
                mkColumn(titleDouble("vd_cnt", ""), indexAccessor(4)),
                mkColumn(titleDouble("vd_hash", ""), indexAccessor(5)),
                mkColumn(titleDouble("vd_aux", ""), indexAccessor(6)),
                mkColumn(titleDouble("vd_next", ""), indexAccessor(7))
        );
    }

    @Override
    protected List<String[]> defineRows() {
        return section.definitions().stream()
                .flatMap(entry -> generateRows(entry))
                .collect(toList());
    }

    private Stream<String[]> generateRows(ElfVersionDefinition<NATIVE_WORD> entry) {
        List<String[]> rows = new ArrayList<>();

        // Entry row
        rows.add(new String[] {
                "ENTRY",
                entry.version().apiName(),
                hex(entry.flags()),
                entry.versionIndex().apiName(),
                dec(entry.numberOfAuxiliaryEntries()),
                hex(entry.nameHash()),
                dec(entry.offsetAuxiliary()),
                dec(entry.offsetNext())
        });

        // Aux entries
        for (ElfVersionDefinitionAuxiliary<NATIVE_WORD> auxEntry : entry.auxiliaryEntries()) {
            rows.add(new String[] {
                    " -> AUXILIARY",
                    hex(auxEntry.nameIndex().intValue()),
                    auxEntry.name(),
                    dec(auxEntry.offsetNext())
            });
        }

        return rows.stream();
    }

    @Override
    protected Predicate<String[]> createFilter(String searchPhrase) {
        return mkStringsFilter(searchPhrase);
    }
}
