package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.segments.Elf32Segment;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.*;

import java.io.File;
import java.util.Stack;

public class ElfExplorerTreeViewBuilder {
    private final File elfPath;
    private final Elf32File elfFile;
    private final TableView<Object> tableView;

    private final Stack<TreeItem<DisplayAction>> parents = new Stack<>();

    public ElfExplorerTreeViewBuilder(File elfPath,
                                      Elf32File elfFile,
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

        elfFile.accept(new BuildMenuVisitor());

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

    class BuildMenuVisitor implements Elf32Visitor {
        @Override
        public void enter(ElfIdentification identification) {
            enterNode(new TreeItem<>(new DisplayAction(
                    "Identification Bytes",
                    tv -> new ElfIdentificationRenderer(identification).renderDataOn(tv))));
        }

        @Override
        public void exit(ElfIdentification identification) {
            exitNode();
        }

        @Override
        public void enter(Elf32Header header) {
            enterNode(new TreeItem<>(new DisplayAction(
                    "ELF Header",
                    tv -> new Elf32HeaderRenderer(header).renderDataOn(tv)
            )));
        }

        @Override
        public void exit(Elf32Header header) {
            exitNode();
        }

        @Override
        public void enterSections() {
            enterNode(new TreeItem<>(new DisplayAction("Sections")));
        }

        private void genericSectionEnter(Elf32Section section) {
            String displayName = (section.name() == null || section.name().isEmpty())
                    ? "(empty)"
                    : section.name();

            enterNode(new TreeItem<>(new DisplayAction(
                    displayName,
                    tv -> new Elf32SectionHeaderRenderer(section.header()).renderDataOn(tv))));

            /* Various heuristics */
            if (section.containsStrings()) {
                addChild(new TreeItem<>(new DisplayAction(
                        "(Null Terminated Strings)",
                        tv -> new Elf32SectionStringsViewRenderer(section).renderDataOn(tv))));
            }

            if (section.header().size() > 0) {
                addChild(new TreeItem<>(new DisplayAction(
                        "(Contents)",
                        tv -> new FileViewRenderer(section.contents()).renderDataOn(tv))));
            }
        }

        private void genericSectionExit() {
            exitNode();
        }

        @Override
        public void enter(Elf32Section section) {
            genericSectionEnter(section);
        }

        @Override
        public void exit(Elf32Section section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32DynamicSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Dynamic Tags",
                    tv -> new Elf32DynamicSectionRenderer(section).renderDataOn(tv))));
        }

        @Override
        public void exit(Elf32DynamicSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32InterpreterSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Interpreter",
                    tv -> new Elf32InterpreterSectionRenderer(section).renderDataOn(tv))));
        }

        @Override
        public void exit(Elf32InterpreterSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32NotesSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Notes",
                    tv -> new Elf32NotesSectionRenderer(section).renderDataOn(tableView))));
        }

        @Override
        public void exit(Elf32NotesSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32RelocationSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Relocations",
                    tv -> new Elf32RelocationSectionRenderer(section).renderDataOn(tv))));
        }

        @Override
        public void exit(Elf32RelocationSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32RelocationAddendSection section) {

        }

        @Override
        public void exit(Elf32RelocationAddendSection section) {

        }

        @Override
        public void enter(Elf32StringTableSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "String Table",
                    tv -> new Elf32StringTableSectionRenderer(section).renderDataOn(tv))));
        }

        @Override
        public void exit(Elf32StringTableSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32SymbolTableSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Symbol Table",
                    tv -> new Elf32SymbolTableSectionRenderer(section).renderDataOn(tv))));
        }
        @Override
        public void exit(Elf32SymbolTableSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32GnuHashSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "Gnu Hash Table",
                    tv -> new Elf32GnuHashSectionRenderer(section).renderDataOn(tv))));
        }

        @Override
        public void exit(Elf32GnuHashSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32InvalidSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "(Parsing Errors)",
                    tv -> new Elf32InvalidSectionRenderer(section).renderDataOn(tv))));
        }

        @Override
        public void exit(Elf32InvalidSection section) {
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
        public void enter(Elf32Segment segment) {
            Elf32ProgramHeader programHeader = segment.programHeader();
            String segmentName = programHeader.type() + " (" +
                    programHeader.virtualAddress().toString() + " - " +
                    programHeader.endVirtualAddress() + ")";

            enterNode(new TreeItem<>(new DisplayAction(
                    segmentName,
                    tv -> new Elf32SegmentRenderer(segment).renderDataOn(tv))));

            if (segment.programHeader().fileSize() > 0) {
                addChild(new TreeItem<>(new DisplayAction(
                        "(Contents)",
                        tv -> new FileViewRenderer(segment.contents()).renderDataOn(tv))));
            }

            for (Elf32Section section : segment.containedSections()) {
                // Recreate section submenu
                section.accept(this);
            }
        }

        @Override
        public void exit(Elf32Segment programHeader) {
            exitNode();
        }

        @Override
        public void exitSegments() {
            exitNode();
        }
    }
}
