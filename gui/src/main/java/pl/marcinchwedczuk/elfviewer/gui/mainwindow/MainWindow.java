package pl.marcinchwedczuk.elfviewer.gui.mainwindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import pl.marcinchwedczuk.elfviewer.elfreader.elf.ElfIdentification;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.*;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.*;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileSystemFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteArrays;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.INTERP;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.*;

public class MainWindow implements Initializable {
    public static MainWindow showOn(Stage window) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("MainWindow.fxml"));

            Scene scene = new Scene(loader.load());
            MainWindow controller = (MainWindow) loader.getController();

            window.setTitle("Main Window");
            window.setScene(scene);
            window.setResizable(true);

            window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
                if (KeyCode.ESCAPE == event.getCode()) {
                    controller.guiExit();
                }
            });

            controller.window = window;

            window.show();

            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Window window;

    @FXML
    private TreeView<DisplayAction> treeView;
    private TreeItem<DisplayAction> rootItem;

    @FXML
    private TableView<Object> tableView;

    private FileChooser openFileChooser;

    private File currentElfPath;
    private Elf32File currentElfFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        openFileChooser = newOpenFileChooser();

        rootItem = new TreeItem<>(new DisplayAction("No ELF file loaded"));
        rootItem.setExpanded(true);

        treeView.setRoot(rootItem);

        // Selection mode
        treeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        treeView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldTreeItem, newTreeItem) -> {
                    if (newTreeItem != null) {
                        newTreeItem.getValue().runAction();
                    } else {
                        clearTable();
                    }
                });

        tableView.setPlaceholder(new Label("Select data to display"));
    }

    private void loadElfFile(File f) {
        currentElfFile = ElfReader.readElf32(new FileSystemFile(f));
        currentElfPath = f;

        recreateTreeView();
    }

    private void recreateTreeView() {
        rootItem.getChildren().clear();

        String name = currentElfPath.getName();
        rootItem.setValue(new DisplayAction(name, this::clearTable));

        TreeItem<DisplayAction> elfHeader = new TreeItem<>(
                new DisplayAction("ELF Header", () -> {
                    displayInTable(currentElfFile.header);
                }));
        rootItem.getChildren().add(elfHeader);

        TreeItem<DisplayAction> identificationBytes = new TreeItem<>(
                new DisplayAction("Identification Bytes", () -> {
                    displayInTable(currentElfFile.header.identification());
                }));
        elfHeader.getChildren().add(identificationBytes);

        // Sections
        TreeItem<DisplayAction> sections = new TreeItem<>(new DisplayAction("Sections"));
        rootItem.getChildren().add(sections);

        for (Elf32Section section : currentElfFile.sections()) {
            SectionHeaderRenderer headerRenderer =
                    new SectionHeaderRenderer(section.header());

            TreeItem<DisplayAction> showSection = new TreeItem<>(new DisplayAction(
                    (section.name() == null || section.name().isEmpty()) ? "(empty)" : section.name(),
                    () -> headerRenderer.renderDataOn(tableView)));
            sections.getChildren().add(showSection);

            // TODO: Visitor pattern???
            if (section instanceof Elf32StringTableSection) {
                Elf32StringTableSection stringSection = (Elf32StringTableSection) section;

                StringTableRenderer renderer = new StringTableRenderer(stringSection.stringTable());
                TreeItem<DisplayAction> showStringTable = new TreeItem<>(new DisplayAction(
                        "String Table", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showStringTable);
            } else if (section instanceof Elf32SymbolTableSection) {
                Elf32SymbolTableSection symbolTableSection = (Elf32SymbolTableSection) section;

                // TODO: Move creating symbol table inside action - to handle errors
                SymbolTableRenderer renderer = new SymbolTableRenderer(symbolTableSection);
                TreeItem<DisplayAction> showSymbolTable = new TreeItem<>(new DisplayAction(
                        "Symbol Table", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showSymbolTable);
            } else if (section instanceof Elf32InterpreterSection) {
                Elf32InterpreterSection interpreterSection = (Elf32InterpreterSection) section;

                InterpreterSectionRenderer renderer = new InterpreterSectionRenderer(interpreterSection);
                TreeItem<DisplayAction> showInterpreter = new TreeItem<>(new DisplayAction(
                        "Interpreter", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showInterpreter);
            } else if (section instanceof Elf32DynamicSection) {
                Elf32DynamicSection dynamicSection = (Elf32DynamicSection) section;

                DynamicSectionRenderer renderer = new DynamicSectionRenderer(dynamicSection);
                TreeItem<DisplayAction> showInterpreter = new TreeItem<>(new DisplayAction(
                        "Dynamic Tags", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showInterpreter);
            } else if (section instanceof Elf32RelocationSection) {
                Elf32RelocationSection relocationSection = (Elf32RelocationSection) section;

                RelocationSectionRenderer renderer = new RelocationSectionRenderer(relocationSection);
                TreeItem<DisplayAction> showRelocations = new TreeItem<>(new DisplayAction(
                        "Relocations", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showRelocations);
            } else if (section instanceof Elf32NotesSection) {
                Elf32NotesSection noteSection = (Elf32NotesSection) section;

                NotesSectionRenderer renderer = new NotesSectionRenderer(noteSection);
                TreeItem<DisplayAction> showNotes = new TreeItem<>(new DisplayAction(
                        "Notes", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showNotes);
            }

            if (section instanceof Elf32InvalidSection) {
                Elf32InvalidSection invalidSection = (Elf32InvalidSection) section;

                InvalidSectionRenderer renderer = new InvalidSectionRenderer(invalidSection);
                TreeItem<DisplayAction> showInterpreter = new TreeItem<>(new DisplayAction(
                        "(Parsing Errors)", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showInterpreter);
            }

            /* Various heuristics */
            if (section.containsStrings()) {
                StringContentsSectionRenderer renderer = new StringContentsSectionRenderer(section);
                TreeItem<DisplayAction> showStrings = new TreeItem<>(new DisplayAction(
                        "(Null Terminated Strings)", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showStrings);
            }
        }

        // Program Headers
        TreeItem<DisplayAction> segments = new TreeItem<>(new DisplayAction("Segments (Program Headers)"));
        rootItem.getChildren().add(segments);

        for (Elf32ProgramHeader ph : currentElfFile.programHeaders) {
            String segmentName = ph.type() + " (" +
                    ph.virtualAddress().toString() + " - " + ph.endVirtualAddress() + ")";
            TreeItem<DisplayAction> showSegment = new TreeItem<>(new DisplayAction(
                    segmentName, () -> displayInTable(ph)));

            // Attach contained sections
            for (Elf32SectionHeader sh : currentElfFile.sectionHeaders) {
                if (sh.type().is(NULL)) continue;

                if (ph.containsSection(sh)) {
                    TreeItem<DisplayAction> showSection = new TreeItem<>(new DisplayAction(
                            sh.name(), () -> displayInTable(sh)));
                    showSegment.getChildren().add(showSection);
                }
            }

            segments.getChildren().add(showSegment);
        }
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

    private <T> TableColumn<Object, String> mkColumn(String caption, Function<T, Object> mapper) {
        TableColumn<Object, String> column = new TableColumn<>(caption);
        column.setCellValueFactory(new LambdaValueFactory<>(
                x -> Objects.toString(mapper.apply((T) x))));
        setupColumn(column);
        return column;
    }

    private void setupTableGenericNumericItem() {
        clearTable();

        TableColumn<Object, String> fieldNameColumn = mkColumn("Field Name", "fieldName");
        TableColumn<Object, String> hexValueColumn = mkColumn("Value Hex\nEnum Value", "hexValue");
        TableColumn<Object, String> intValueColumn = mkColumn("Value Int", "intValue");
        TableColumn<Object, String> descriptionColumn = mkColumn("Description", "description");

        tableView.getColumns().addAll(
                fieldNameColumn, hexValueColumn, intValueColumn, descriptionColumn
        );
    }

    private void setupTableGenericStringItem() {
        clearTable();

        TableColumn<Object, String> fieldNameColumn = mkColumn("Field Name", "fieldName");
        TableColumn<Object, String> valueColumn = mkColumn("Value", "value");
        TableColumn<Object, String> descriptionColumn = mkColumn("Description", "description");

        tableView.getColumns().addAll(
                fieldNameColumn, valueColumn, descriptionColumn
        );
    }

    private void displayInTable(Elf32Header header) {
        setupTableGenericNumericItem();

        tableView.getItems().addAll(
                new StructureFieldDto("e_type", header.type()),
                new StructureFieldDto("e_machine", header.machine()),
                new StructureFieldDto("e_version", header.version()),
                new StructureFieldDto("e_entry", header.entry()),
                new StructureFieldDto("e_phoff", header.programHeaderTableOffset()),
                new StructureFieldDto("e_shoff", header.sectionHeaderTableOffset()),
                new StructureFieldDto("e_flags", header.flags()),
                new StructureFieldDto("e_ehsize", header.headerSize()),
                new StructureFieldDto("e_phentsize", header.programHeaderSize()),
                new StructureFieldDto("e_phnum", header.numberOfProgramHeaders()),
                new StructureFieldDto("e_shentsize", header.sectionHeaderSize()),
                new StructureFieldDto("e_shnum", header.numberOfSectionHeaders()),
                new StructureFieldDto("e_shstrndx", header.sectionContainingSectionNames().intValue())
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

    private void displayInTable(Elf32SectionHeader sh) {
        setupTableGenericNumericItem();

        tableView.getItems().addAll(
                new StructureFieldDto("sh_name", sh.nameIndex().intValue()),
                new StructureFieldDto("name", sh.name()),
                new StructureFieldDto("sh_type", sh.type()),
                new StructureFieldDto("sh_flags", sh.flags().toString()),
                new StructureFieldDto("sh_addr", sh.inMemoryAddress()),
                new StructureFieldDto("sh_offset", sh.offsetInFile()),
                new StructureFieldDto("sh_size", sh.sectionSize()),
                new StructureFieldDto("sh_link", sh.link()),
                new StructureFieldDto("sh_info", sh.info()),
                new StructureFieldDto("sh_addralign", sh.addressAlignment()),
                new StructureFieldDto("sh_entsize", sh.containedEntrySize()));
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

    @FXML
    private void guiOpen() {
        File f = openFileChooser.showOpenDialog(window);
        if (f != null) {
            loadElfFile(f);
        }
    }

    @FXML
    private void guiExit() {
        Platform.exit();
    }

    @FXML
    private void guiAbout() {

    }

    public static FileChooser newOpenFileChooser() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select ELF file...");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*")
        );

        return fileChooser;
    }
}
