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
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileSystemFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

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

        for (Elf32SectionHeader sh : currentElfFile.sectionHeaders) {
            TreeItem<DisplayAction> showSection = new TreeItem<>(new DisplayAction(
                    sh.name(), () -> displayInTable(sh)));
            sections.getChildren().add(showSection);

            if (sh.type().is(STRING_TABLE)) {
                TreeItem<DisplayAction> showStringTable = new TreeItem<>(new DisplayAction(
                        "String Table", () -> displayStringTable(sh)));
                showSection.getChildren().add(showStringTable);
            } else if (sh.type().is(DYNAMIC)) {
                TreeItem<DisplayAction> showDynamicTags = new TreeItem<>(new DisplayAction(
                        "Dynamic Tags", () -> displayDynamicTags(sh)));
                showSection.getChildren().add(showDynamicTags);
            } else if (sh.type().is(REL)) {
                TreeItem<DisplayAction> showRelocations = new TreeItem<>(new DisplayAction(
                        "Relocations", () -> displayRelocations(sh)));
                showSection.getChildren().add(showRelocations);
            }
        }

        // Program Headers
        TreeItem<DisplayAction> segments = new TreeItem<>(new DisplayAction("Segments (Program Headers)"));
        rootItem.getChildren().add(segments);

        for (Elf32ProgramHeader ph : currentElfFile.programHeaders) {
            TreeItem<DisplayAction> showSection = new TreeItem<>(new DisplayAction(
                    ph.virtualAddress().toString(), () -> displayInTable(ph)));
            segments.getChildren().add(showSection);
        }
    }


    private void clearTable() {
        tableView.getItems().clear();
        tableView.getColumns().clear();
    }

    private void setupColumn(TableColumn<?,?> column) {
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
                new GenericNumericItem("e_type", header.type()),
                new GenericNumericItem("e_machine", header.machine()),
                new GenericNumericItem("e_version", header.version()),
                new GenericNumericItem("e_entry", header.entry()),
                new GenericNumericItem("e_phoff", header.programHeaderTableOffset()),
                new GenericNumericItem("e_shoff", header.sectionHeaderTableOffset()),
                new GenericNumericItem("e_flags", header.flags()),
                new GenericNumericItem("e_ehsize", header.headerSize()),
                new GenericNumericItem("e_phentsize", header.programHeaderSize()),
                new GenericNumericItem("e_phnum", header.numberOfProgramHeaders()),
                new GenericNumericItem("e_shentsize", header.sectionHeaderSize()),
                new GenericNumericItem("e_shnum", header.numberOfSectionHeaders()),
                new GenericNumericItem("e_shstrndx", header.sectionContainingSectionNames().intValue())
        );
    }

    private void displayInTable(ElfIdentification identification) {
        setupTableGenericStringItem();

        tableView.getItems().addAll(
                new GenericStringItem("Magic String", identification.printableMagicString()),
                new GenericStringItem("Class", identification.elfClass()),
                new GenericStringItem("Data", identification.elfData()),
                new GenericStringItem("Version", identification.elfVersion()),
                new GenericStringItem("OS ABI", identification.osAbi()),
                new GenericStringItem("OS ABI Version", identification.osAbiVersion()),
                new GenericStringItem("Padding bytes", Arrays.toString(identification.paddingBytes()))
        );
    }

    private void displayInTable(Elf32SectionHeader sh) {
        setupTableGenericNumericItem();

        tableView.getItems().addAll(
                new GenericNumericItem("sh_name", sh.nameIndex().intValue()),
                new GenericNumericItem("name", sh.name()),
                new GenericNumericItem("sh_type", sh.type()),
                new GenericNumericItem("sh_flags", sh.flags().toString()),
                new GenericNumericItem("sh_addr", sh.inMemoryAddress()),
                new GenericNumericItem("sh_offset", sh.offsetInFile()),
                new GenericNumericItem("sh_size", sh.sectionSize()),
                new GenericNumericItem("sh_link", sh.link()),
                new GenericNumericItem("sh_info", sh.info()),
                new GenericNumericItem("sh_addralign", sh.addressAlignment()),
                new GenericNumericItem("sh_entsize", sh.containedEntrySize()));
    }

    private void displayInTable(Elf32ProgramHeader ph) {
        setupTableGenericNumericItem();

        tableView.getItems().addAll(
                new GenericNumericItem("p_type", ph.type()),
                new GenericNumericItem("p_offset", ph.fileOffset()),
                new GenericNumericItem("p_vaddr", ph.virtualAddress()),
                new GenericNumericItem("p_paddr", ph.physicalAddress()),
                new GenericNumericItem("p_filesz", ph.fileSize()),
                new GenericNumericItem("p_memsz", ph.memorySize()),
                new GenericNumericItem("p_flags", ph.flags().toString()),
                new GenericNumericItem("p_align", ph.alignment()));
    }

    private void displayStringTable(Elf32SectionHeader sh) {
        setupTableGenericStringItem();

        StringTable st = new StringTable(currentElfFile.storage, sh);

        for (StringTableEntry entry : st.getContents()) {
            tableView.getItems().add(new GenericStringItem(
                    entry.index.toString(),
                    entry.value));
        }
    }

    private void setupDynamicTagsTable() {
        clearTable();

        TableColumn<Object, String> tagColumn = mkColumn("Tag Type", Elf32DynamicTag::type);
        TableColumn<Object, String> hexValueColumn = mkColumn("Value Hex",
                (Elf32DynamicTag t) -> String.format("0x%08x", t.value()));

        TableColumn<Object, String> intValueColumn = mkColumn("Value Int",
                (Elf32DynamicTag t) -> String.format("%010d", t.value()));

        tableView.getColumns().addAll(
                tagColumn, hexValueColumn, intValueColumn
        );
    }

    private void displayDynamicTags(Elf32SectionHeader sh) {
        setupDynamicTagsTable();

        List<Elf32DynamicTag> tags = new Elf32DynamicTags(currentElfFile, sh).getTags();
        tableView.getItems().addAll(tags);
    }

    // TODO: Extract to renderer class RelocationsRenderer.render(tv, sh);
    private void setupRelocationsTable() {
        clearTable();

        TableColumn<Object, String> offsetColumn = mkColumn("Offset", Elf32Relocation::offset);
        TableColumn<Object, String> infoColumn = mkColumn("Info",
                (Elf32Relocation r) -> String.format("0x%08x", r.info()));
        TableColumn<Object, String> symbolColumn = mkColumn("Symbol", Elf32Relocation::symbol);
        TableColumn<Object, String> typeColumn = mkColumn("Type", Elf32Relocation::type);

        // TODO: Intel relocation type - change when viewing other platforms
        TableColumn<Object, String> intelTypeColumn = mkColumn("Intel Type",
                Elf32Relocation::intel386RelocationType);


        tableView.getColumns().addAll(
                offsetColumn, infoColumn, symbolColumn, typeColumn,
                intelTypeColumn
        );
    }

    private void displayRelocations(Elf32SectionHeader sh) {
        RelocationsTable relTable = new RelocationsTable(sh, currentElfFile);
        Collection<Elf32Relocation> relocations = relTable.relocations();

        setupRelocationsTable();
        tableView.getItems().addAll(relocations);
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
