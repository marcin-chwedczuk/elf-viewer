package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHashTable;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.*;

import java.io.File;
import java.util.Stack;

public class ElfExplorerTreeViewBuilder {
    private final File elfPath;
    private final ElfFile<?> elfFile;
    private final TableView<Object> tableView;

    private final Stack<TreeItem<DisplayAction>> parents = new Stack<>();

    public ElfExplorerTreeViewBuilder(File elfPath,
                                      ElfFile<?> elfFile,
                                      TableView<Object> tableView) {
        this.elfPath = elfPath;
        this.elfFile = elfFile;
        this.tableView = tableView;
    }

    public TreeItem<DisplayAction> build() {
        parents.clear();

        String fileName = elfPath.getName();
        TreeItem<DisplayAction> rootItem = new TreeItem<>(new DisplayAction(
                fileName,
                tv -> clearTable()));
        parents.add(rootItem);

        elfFile.accept(new BuildMenuVisitor(elfFile.nativeWordMetadata()));

        if (parents.size() != 1)
            throw new AssertionError();

        return parents.elementAt(0);
    }

    private void addChild(TreeItem<DisplayAction> child) {
        this.parents.peek().getChildren().add(child);
    }

    private void enterNode(TreeItem<DisplayAction> child) {
        this.parents.peek().getChildren().add(child);
        this.parents.push(child);
    }

    private void exitNode() {
        this.parents.pop();
    }

    private void clearTable() {
        tableView.getItems().clear();
        tableView.getColumns().clear();
    }

    class BuildMenuVisitor<NATIVE_WORD extends Number & Comparable<NATIVE_WORD>> implements ElfVisitor<NATIVE_WORD> {
        private final NativeWord<NATIVE_WORD> nativeWord;

        BuildMenuVisitor(NativeWord<NATIVE_WORD> nativeWord) {
            this.nativeWord = nativeWord;
        }

        @Override
        public void enter(ElfIdentification identification) {
            enterNode(new TreeItem<>(new DisplayAction(
                    "Identification Bytes",
                    tv -> new ElfIdentificationRenderer<>(nativeWord, identification).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfIdentification identification) {
            exitNode();
        }

        @Override
        public void enter(ElfHeader<NATIVE_WORD> header) {
            enterNode(new TreeItem<>(new DisplayAction(
                    "ELF Header",
                    tv -> new ElfHeaderRenderer<>(nativeWord, header).renderDataOn(tv)
            )));
        }

        @Override
        public void exit(ElfHeader<NATIVE_WORD> header) {
            exitNode();
        }

        @Override
        public void enterSections() {
            enterNode(new TreeItem<>(new DisplayAction("Sections")));
        }

        private void genericSectionEnter(ElfSection<NATIVE_WORD> section) {
            String displayName = (section.name() == null || section.name().isEmpty())
                    ? "(empty)"
                    : section.name();

            enterNode(new TreeItem<>(new DisplayAction(
                    displayName,
                    tv -> new ElfSectionHeaderRenderer<>(nativeWord, section.header()).renderDataOn(tv))));

            /* Various heuristics */
            if (section.containsStrings()) {
                addChild(new TreeItem<>(new DisplayAction(
                        "(Null Terminated Strings)",
                        tv -> new ElfSectionStringsViewRenderer<>(nativeWord, section).renderDataOn(tv))));
            }

            if (section.header().size().longValue() > 0) {
                addChild(new TreeItem<>(new DisplayAction(
                        "(Contents)",
                        tv -> new FileViewRenderer<>(nativeWord, section.contents()).renderDataOn(tv))));
            }
        }

        private void genericSectionExit() {
            exitNode();
        }

        @Override
        public void enter(ElfSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
        }

        @Override
        public void exit(ElfSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfDynamicSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Dynamic Tags",
                    tv -> new ElfDynamicSectionRenderer<NATIVE_WORD>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfDynamicSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfInterpreterSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Interpreter",
                    tv -> new ElfInterpreterSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfInterpreterSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfNotesSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Notes",
                    tv -> new ElfNotesSectionRenderer<>(nativeWord, section).renderDataOn(tableView))));
        }

        @Override
        public void exit(ElfNotesSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfRelocationSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Relocations",
                    tv -> new ElfRelocationSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfRelocationSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfRelocationAddendSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Relocations (Addend)",
                    tv -> new ElfRelocationAddendSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfRelocationAddendSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfStringTableSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "String Table",
                    tv -> new ElfStringTableSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfStringTableSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfSymbolTableSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Symbol Table",
                    tv -> new ElfSymbolTableSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }
        @Override
        public void exit(ElfSymbolTableSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuHashSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Gnu Hash Table",
                    tv -> new ElfGnuHashSectionRenderer(elfFile.nativeWordMetadata(), section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfGnuHashSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfInvalidSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "(Parsing Errors)",
                    tv -> new ElfInvalidSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfInvalidSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuVersionSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Symbol Versions",
                    tv -> new ElfGnuVersionSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfGnuVersionSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuVersionRequirementsSection<NATIVE_WORD> section) {
            genericSectionEnter(section);

            addChild(new TreeItem<>(new DisplayAction(
                    "Symbol Versions (Required)",
                    tv -> new ElfGnuVersionRequirementsSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfGnuVersionRequirementsSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuVersionDefinitionsSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Symbol Versions (Defined)",
                    tv -> new ElfGnuVersionDefinitionsSectionRenderer<>(nativeWord, section).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfGnuVersionDefinitionsSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfHashSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Hash Table",
                    tv -> new ElfHashSectionRenderer<>(nativeWord, section).renderDataOn(tv))));

        }

        @Override
        public void exit(ElfHashSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void exitSections() {
            exitNode();
        }

        @Override
        public void enterSegments() {
            enterNode(new TreeItem<>(new DisplayAction("Segments")));
        }

        @Override
        public void enter(ElfSegment<NATIVE_WORD> segment) {
            ElfProgramHeader<NATIVE_WORD> programHeader = segment.programHeader();
            String segmentName = programHeader.type() + " (" +
                    programHeader.virtualAddress().toString() + " - " +
                    programHeader.endVirtualAddress() + ")";

            enterNode(new TreeItem<>(new DisplayAction(
                    segmentName,
                    tv -> new ElfSegmentRenderer<>(nativeWord, segment).renderDataOn(tv))));

            if (segment.programHeader().fileSize().longValue() > 0) {
                addChild(new TreeItem<>(new DisplayAction(
                        "(Contents)",
                        tv -> new FileViewRenderer<>(nativeWord, segment.contents()).renderDataOn(tv))));
            }

            for (ElfSection<NATIVE_WORD> section : segment.containedSections()) {
                // Recreate section submenu
                section.accept(this);
            }
        }

        @Override
        public void exit(ElfSegment<NATIVE_WORD> programHeader) {
            exitNode();
        }

        @Override
        public void exitSegments() {
            exitNode();
        }
    }
}
