package pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer;

import javafx.scene.control.TableColumn;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.ElfGnuVersionRequirementsSection;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionNeeded;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.versions.ElfVersionNeededAuxiliary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ElfGnuVersionRequirementsSectionRenderer<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>>
        extends BaseRenderer<String[], NATIVE_WORD> {
    private final ElfGnuVersionRequirementsSection<NATIVE_WORD> section;

    public ElfGnuVersionRequirementsSectionRenderer(
            NativeWord<NATIVE_WORD> nativeWord,
            ElfGnuVersionRequirementsSection<NATIVE_WORD> section) {
        super(nativeWord);
        this.section = section;
    }

    @Override
    protected List<TableColumn<String[], String>> defineColumns() {
        return List.of(
                mkColumn("(type)", indexAccessor(0)),
                mkColumn(titleDouble("vn_version", "vna_hash"), indexAccessor(1)),
                mkColumn(titleDouble("vn_cnt", "vna_flags"), indexAccessor(2)),
                mkColumn(titleDouble("vn_file", "vna_other"), indexAccessor(3)),
                mkColumn(titleDouble("(vn_file)", "vna_name"), indexAccessor(4)),
                mkColumn(titleDouble("vn_aux", "(vna_name)"), indexAccessor(5)),
                mkColumn(titleDouble("vn_next", "vna_next"), indexAccessor(6))
        );
    }

    @Override
    protected List<? extends String[]> defineRows() {
        return section.requirements().stream()
                .flatMap(entry -> generateRows(entry))
                .collect(toList());
    }

    private Stream<String[]> generateRows(ElfVersionNeeded<NATIVE_WORD> entry) {
        List<String[]> rows = new ArrayList<>();

        // Entry row
        rows.add(new String[] {
                "ENTRY",
                entry.version().apiName(),
                dec(entry.numberOfAuxiliaryEntries()),
                dec(entry.fileNameIndex().intValue()),
                entry.fileName(),
                dec(entry.offsetAuxiliaryEntries()),
                dec(entry.offsetNextEntry()),
        });

        // Aux entries
        for (ElfVersionNeededAuxiliary<NATIVE_WORD> auxEntry : entry.auxiliaryEntries()) {
            rows.add(new String[] {
                    " -> AUXILIARY",
                    hex(auxEntry.hash()),
                    hex(auxEntry.flags()), // TODO: proper flags
                    auxEntry.other().apiName(),
                    hex(auxEntry.nameIndex().intValue()),
                    auxEntry.name(),
                    dec(auxEntry.offsetNext())
            });
        }

        return rows.stream();
    }
}
