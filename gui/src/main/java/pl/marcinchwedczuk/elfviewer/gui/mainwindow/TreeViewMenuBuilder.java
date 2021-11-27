package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32File;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32Header;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32ProgramHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.Elf32SectionHeader;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.visitor.Elf32Visitor;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.*;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;

public class TreeViewMenuBuilder {
    private final File elfPath;
    private final Elf32File elfFile;
    private final TableView<Object> tableView;

    private final Stack<TreeItem<DisplayAction>> parents = new Stack<>();

    public TreeViewMenuBuilder(File elfPath,
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

    private void setupColumn(TableColumn<?, ?> column) {
        column.setSortable(false);
        column.setEditable(false);
        column.setResizable(true);
    }

    private TableColumn<Object, String> mkColumn(String caption, String propertyName) {
        TableColumn<Object, String> column = new TableColumn<>(caption);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        setupColumn(column);
        return column;
    }

    private void setupTableGenericNumericItem() {
        clearTable();

        TableColumn<Object, String> fieldNameColumn = mkColumn("Field Name", "fieldName");
        TableColumn<Object, String> hexValueColumn = mkColumn("Value Hex\nEnum Value", "rawValue");
        TableColumn<Object, String> intValueColumn = mkColumn("Value Int", "parsedValue");
        TableColumn<Object, String> descriptionColumn = mkColumn("Comment", "comment");

        tableView.getColumns().addAll(
                fieldNameColumn, hexValueColumn, intValueColumn, descriptionColumn
        );
    }

    private void setupTableGenericStringItem() {
        clearTable();

        TableColumn<Object, String> fieldNameColumn = mkColumn("Field Name", "fieldName");
        TableColumn<Object, String> valueColumn = mkColumn("Value", "value");
        TableColumn<Object, String> descriptionColumn = mkColumn("Comment", "comment");

        tableView.getColumns().addAll(
                fieldNameColumn, valueColumn, descriptionColumn
        );
    }

    private void displayInTable(ElfIdentification identification) {
        setupTableGenericStringItem();

        tableView.getItems().addAll(
                new StringTableEntryDto("Magic String", identification.printableMagicString()),
                new StringTableEntryDto("Class", identification.elfClass()),
                new StringTableEntryDto("Data", identification.elfData()),
                new StringTableEntryDto("Version", identification.elfVersion()),
                new StringTableEntryDto("OS ABI", identification.osAbi()),
                new StringTableEntryDto("OS ABI Version", identification.osAbiVersion()),
                new StringTableEntryDto("Padding bytes", Arrays.toString(identification.paddingBytes()))
        );
    }

    private void displayInTable(Elf32ProgramHeader ph) {
        setupTableGenericNumericItem();

        tableView.getItems().addAll(
                new StructureFieldDto("p_type", ph.type()),
                new StructureFieldDto("p_offset", ph.fileOffset()),
                new StructureFieldDto("p_vaddr", ph.virtualAddress()),
                new StructureFieldDto("p_paddr", ph.physicalAddress()),
                new StructureFieldDto("p_filesz", ph.fileSize()),
                new StructureFieldDto("p_memsz", ph.memorySize()),
                new StructureFieldDto("p_flags", ph.flags().toString()),
                new StructureFieldDto("p_align", ph.alignment()));
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
                    tv -> new SectionHeaderRenderer(section.header()).renderDataOn(tv))));

            /* Various heuristics */
            if (section.containsStrings()) {
                addChild(new TreeItem<>(new DisplayAction(
                        "(Null Terminated Strings)",
                        tv -> new StringContentsSectionRenderer(section).renderDataOn(tv))));
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
                    tv -> new DynamicSectionRenderer(section).renderDataOn(tv))));
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
                    tv -> new InterpreterSectionRenderer(section).renderDataOn(tv))));
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
                    tv -> new NotesSectionRenderer(section).renderDataOn(tableView))));
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
                    tv -> new RelocationSectionRenderer(section).renderDataOn(tv))));
        }

        @Override
        public void exit(Elf32RelocationSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32StringTableSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "String Table",
                    tv -> new StringTableRenderer(section.stringTable()).renderDataOn(tv))));
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
                    tv -> new SymbolTableRenderer(section).renderDataOn(tv))));
        }

        @Override
        public void exit(Elf32SymbolTableSection section) {
            genericSectionExit();
        }

        @Override
        public void enter(Elf32InvalidSection section) {
            genericSectionEnter(section);
            addChild(new TreeItem<>(new DisplayAction(
                    "(Parsing Errors)",
                    tv -> new InvalidSectionRenderer(section).renderDataOn(tv))));
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
        public void enter(Elf32ProgramHeader programHeader) {
            String segmentName = programHeader.type() + " (" +
                    programHeader.virtualAddress().toString() + " - " +
                    programHeader.endVirtualAddress() + ")";

            enterNode(new TreeItem<>(new DisplayAction(
                    segmentName, tv -> displayInTable(programHeader))));

            for (Elf32Section section : elfFile.sections()) {
                if (programHeader.containsSection(section.header())) {
                    // Recreate section submenu
                    section.accept(this);
                }
            }
        }

        @Override
        public void exit(Elf32ProgramHeader programHeader) {
            exitNode();
        }

        @Override
        public void exitSegments() {
            exitNode();
        }
    }
}
