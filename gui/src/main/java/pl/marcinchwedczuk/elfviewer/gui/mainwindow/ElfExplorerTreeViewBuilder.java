package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.arch.NativeWord;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfFile;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.ElfHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.segments.ElfSegment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.shared.visitor.ElfVisitor;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.*;

import java.io.File;
import java.util.Stack;

public class ElfExplorerTreeViewBuilder<
        NATIVE_WORD extends Number & Comparable<NATIVE_WORD>
        >
{
    private final File elfPath;
    private final ElfFile<NATIVE_WORD> elfFile;
    private final TableView<Object> tableView;

    private final Stack<TreeItem<RenderDataAction<NATIVE_WORD>>> parents = new Stack<>();

    private final StringProperty searchPhrase;

    public ElfExplorerTreeViewBuilder(File elfPath,
                                      ElfFile<NATIVE_WORD> elfFile,
                                      TableView<Object> tableView,
                                      StringProperty searchPhrase) {
        this.elfPath = elfPath;
        this.elfFile = elfFile;
        this.tableView = tableView;
        this.searchPhrase = searchPhrase;
    }

    private NativeWord<NATIVE_WORD> nativeWord() {
        return elfFile.nativeWordMetadata();
    }

    public TreeItem<RenderDataAction<NATIVE_WORD>> build() {
        parents.clear();

        String fileName = elfPath.getName();
        TreeItem<RenderDataAction<NATIVE_WORD>> rootItem = new TreeItem<>(new RenderDataAction<>(
                fileName,
                new FileRenderer<>(nativeWord(), elfPath)));
        parents.add(rootItem);

        buildTreeMenu(elfFile);

        if (parents.size() != 1)
            throw new AssertionError();

        return parents.elementAt(0);
    }

    void buildTreeMenu(ElfFile<NATIVE_WORD> elfFile) {
        elfFile.accept(new BuildMenuVisitor(elfFile.nativeWordMetadata()));
    }

    private void addChild(TreeItem<RenderDataAction<NATIVE_WORD>> child) {
        this.parents.peek().getChildren().add(child);
    }

    private void enterNode(TreeItem<RenderDataAction<NATIVE_WORD>> child) {
        this.parents.peek().getChildren().add(child);
        this.parents.push(child);
    }

    private void exitNode() {
        this.parents.pop();
    }

    class BuildMenuVisitor implements ElfVisitor<NATIVE_WORD> {
        private final NativeWord<NATIVE_WORD> nativeWord;

        BuildMenuVisitor(NativeWord<NATIVE_WORD> nativeWord) {
            this.nativeWord = nativeWord;
        }

        @Override
        public void enter(ElfIdentification identification) {
            enterNode(new TreeItem<>(new RenderDataAction<NATIVE_WORD>(
                    "Identification Bytes",
                    new ElfIdentificationRenderer<>(nativeWord, identification))));
        }

        @Override
        public void exit(ElfIdentification identification) {
            exitNode();
        }

        @Override
        public void enter(ElfHeader<NATIVE_WORD> header) {
            enterNode(new TreeItem<>(new RenderDataAction<>(
                    "ELF Header",
                    new ElfHeaderRenderer<>(nativeWord, header))));
        }

        @Override
        public void exit(ElfHeader<NATIVE_WORD> header) {
            exitNode();
        }

        @Override
        public void enterSections() {
            enterNode(new TreeItem<>(new RenderDataAction<>(
                    "Sections",
                    new NothingRenderer<>(nativeWord))));
        }

        private void genericSectionEnter(ElfSection<NATIVE_WORD> section) {
            String displayName = (section.name() == null || section.name().isEmpty())
                    ? "(empty)"
                    : section.name();

            enterNode(new TreeItem<>(new RenderDataAction<>(
                    displayName,
                    new ElfSectionHeaderRenderer<>(nativeWord, section.header()))));

            /* Various heuristics */
            if (section.containsStrings()) {
                addChild(new TreeItem<>(new RenderDataAction<>(
                        "(Null Terminated Strings)",
                        new ElfSectionStringsViewRenderer<>(nativeWord, section))));
            }

            if (section.header().size().longValue() > 0) {
                addChild(new TreeItem<>(new RenderDataAction<>(
                        ContentsHexRenderer.ENTRY_NAME,
                        new ContentsHexRenderer<>(nativeWord, section.contents()))));
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
            addChild(new TreeItem<>(new RenderDataAction<>(
                    "Dynamic Tags",
                    new ElfDynamicSectionRenderer<NATIVE_WORD>(nativeWord, section))));
        }

        @Override
        public void exit(ElfDynamicSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfInterpreterSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction<>(
                    "Interpreter",
                    new ElfInterpreterSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfInterpreterSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfNotesSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction<>(
                    "Notes",
                    new ElfNotesSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfNotesSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfRelocationSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction<>(
                    "Relocations",
                    new ElfRelocationSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfRelocationSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfRelocationAddendSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction<>(
                    "Relocations (Addend)",
                    new ElfRelocationAddendSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfRelocationAddendSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfStringTableSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction(
                    "String Table",
                    new ElfStringTableSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfStringTableSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfSymbolTableSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction(
                    "Symbol Table",
                    new ElfSymbolTableSectionRenderer<>(nativeWord, section))));
        }
        @Override
        public void exit(ElfSymbolTableSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuHashSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction(
                    "Gnu Hash Table",
                    new ElfGnuHashSectionRenderer(elfFile.nativeWordMetadata(), section))));
        }

        @Override
        public void exit(ElfGnuHashSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfInvalidSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction(
                    "(Parsing Errors)",
                    new ElfInvalidSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfInvalidSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuVersionSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction(
                    "Symbol Versions",
                    new ElfGnuVersionSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfGnuVersionSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuVersionRequirementsSection<NATIVE_WORD> section) {
            genericSectionEnter(section);

            addChild(new TreeItem<>(new RenderDataAction(
                    "Symbol Versions (Required)",
                    new ElfGnuVersionRequirementsSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfGnuVersionRequirementsSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuVersionDefinitionsSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction(
                    "Symbol Versions (Defined)",
                    new ElfGnuVersionDefinitionsSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfGnuVersionDefinitionsSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfHashSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction(
                    "Hash Table",
                    new ElfHashSectionRenderer<>(nativeWord, section))));
        }

        @Override
        public void exit(ElfHashSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void enter(ElfGnuWarningSection<NATIVE_WORD> section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new RenderDataAction(
                    "Warning",
                    new ElfGnuWarningSectionRenderer<>(nativeWord, section))));

        }

        @Override
        public void exit(ElfGnuWarningSection<NATIVE_WORD> section) {
            genericSectionExit();
        }

        @Override
        public void exitSections() {
            exitNode();
        }

        @Override
        public void enterSegments() {
            enterNode(new TreeItem<>(new RenderDataAction<>(
                    "Segments",
                    new NothingRenderer<>(nativeWord))));
        }

        @Override
        public void enter(ElfSegment<NATIVE_WORD> segment) {
            ElfProgramHeader<NATIVE_WORD> programHeader = segment.programHeader();
            String segmentName = programHeader.type() + " (" +
                    programHeader.virtualAddress().toString() + " - " +
                    programHeader.endVirtualAddress() + ")";

            enterNode(new TreeItem<>(new RenderDataAction(
                    segmentName,
                    new ElfSegmentRenderer<>(nativeWord, segment))));

            if (segment.programHeader().fileSize().longValue() > 0) {
                addChild(new TreeItem<>(new RenderDataAction(
                        "(Contents)",
                        new ContentsHexRenderer<>(nativeWord, segment.contents()))));
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
