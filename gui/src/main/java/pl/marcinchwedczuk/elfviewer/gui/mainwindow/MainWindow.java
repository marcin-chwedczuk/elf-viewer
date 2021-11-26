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
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32Section;
import pl.marcinchwedczuk.elfviewer.elfreader.elf32.sections.Elf32StringTableSection;
import pl.marcinchwedczuk.elfviewer.elfreader.io.FileSystemFile;
import pl.marcinchwedczuk.elfviewer.elfreader.utils.ByteArrays;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.SectionHeaderRenderer;
import pl.marcinchwedczuk.elfviewer.gui.mainwindow.renderer.StringTableRenderer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static pl.marcinchwedczuk.elfviewer.elfreader.ElfSectionNames.*;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.*;
import static pl.marcinchwedczuk.elfviewer.elfreader.elf32.ElfSectionType.DYNAMIC;

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
            } else if (sh.type().isOneOf(SYMBOL_TABLE, DYNAMIC_SYMBOLS)) {
                Elf32SectionHeader maybeStrTab = currentElfFile.sectionHeaders.get(sh.link());
                if (maybeStrTab.type().isNot(STRING_TABLE)) continue; // Something went wrong...

                TreeItem<DisplayAction> showSymbols = new TreeItem<>(new DisplayAction(
                        "Symbols", () -> displaySymbols(sh, maybeStrTab)));
                showSection.getChildren().add(showSymbols);
            } else if (sh.type().is(ElfSectionType.NOTE)) {
                TreeItem<DisplayAction> showNotes = new TreeItem<>(new DisplayAction(
                        "Notes", () -> displayNotes(sh)));
                showSection.getChildren().add(showNotes);
            } else if (sh.hasName(INTERP)) {
                TreeItem<DisplayAction> showInterpreter = new TreeItem<>(new DisplayAction(
                        "Interpreter", () -> displayInterpreter(sh)));
                showSection.getChildren().add(showInterpreter);
            }

            /* Various heuristics */
            if (sh.flags().hasFlag(SectionAttributes.STRINGS)) {
                TreeItem<DisplayAction> showStrings = new TreeItem<>(new DisplayAction(
                        "(Null Terminated Strings)", () -> displayStrings(sh)));
                showSection.getChildren().add(showStrings);
            }
        }

        // New Sections
        TreeItem<DisplayAction> sections2 = new TreeItem<>(new DisplayAction("Sections - NEW"));
        rootItem.getChildren().add(sections2);

        for (Elf32Section section : currentElfFile.sections()) {
            SectionHeaderRenderer headerRenderer =
                    new SectionHeaderRenderer(section.header());

            TreeItem<DisplayAction> showSection = new TreeItem<>(new DisplayAction(
                    (section.name() == null || section.name().isEmpty()) ? "(empty)" : section.name(),
                    () -> headerRenderer.renderDataOn(tableView)));
            sections2.getChildren().add(showSection);

            // TODO: Visitor pattern???
            if (section instanceof Elf32StringTableSection) {
                Elf32StringTableSection stringSection = (Elf32StringTableSection) section;

                StringTableRenderer renderer = new StringTableRenderer(stringSection.stringTable());
                TreeItem<DisplayAction> showStringTable = new TreeItem<>(new DisplayAction(
                        "String Table", () -> renderer.renderDataOn(tableView)));
                showSection.getChildren().add(showStringTable);
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

    private void displayStringTable(Elf32SectionHeader sh) {
        setupTableGenericStringItem();

        StringTable st = new StringTable(currentElfFile.storage, sh);

        for (StringTableEntry entry : st.getContents()) {
            tableView.getItems().add(new StringTableEntryDto(
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

    private void setupSymbolTable() {
        clearTable();

        // TODO: Refactor this shit...
        Function<Function<Elf32Symbol, Object>, Function<SymbolTableEntry, Object>> s =
                (Function<Elf32Symbol, Object> symbolAccessor) ->
                        (SymbolTableEntry entry) ->
                                symbolAccessor.apply(entry.symbol);

        TableColumn<Object, String> symbolIndex = mkColumn("Symbol Index", (SymbolTableEntry e) ->
                String.format("0x%08x", e.index.intValue()));
        TableColumn<Object, String> nameIndex = mkColumn("Name Index", s.apply(Elf32Symbol::nameIndex));
        TableColumn<Object, String> name = mkColumn("Name", s.apply(Elf32Symbol::name));
        TableColumn<Object, String> value = mkColumn("Value", s.apply(Elf32Symbol::value));
        TableColumn<Object, String> info = mkColumn("Info", s.apply(Elf32Symbol::info));
        TableColumn<Object, String> binding = mkColumn("Info/Binding", s.apply(Elf32Symbol::binding));
        TableColumn<Object, String> symbolType = mkColumn("Info/Symbol Type", s.apply(Elf32Symbol::symbolType));
        TableColumn<Object, String> other = mkColumn("Other", s.apply(Elf32Symbol::other));
        TableColumn<Object, String> visibility = mkColumn("Other/Visibility", s.apply(Elf32Symbol::visibility));
        TableColumn<Object, String> section = mkColumn("Section Index", s.apply(Elf32Symbol::index));

        tableView.getColumns().addAll(
                symbolIndex,
                nameIndex, name, value, info, binding, symbolType, other, visibility, section);
    }
    private void displaySymbols(Elf32SectionHeader symtab,
                                Elf32SectionHeader strtab) {

        setupSymbolTable();

        // Contains symbol names
        StringTable strTab = new StringTable(currentElfFile.storage, strtab);
        SymbolTable symTab = new SymbolTable(currentElfFile, symtab, strTab);

        tableView.getItems().addAll(symTab.symbols());
    }

    private void setupNotesTable() {
        clearTable();

        TableColumn<Object, String> nameLength = mkColumn("Name Length", (GuiNote e) -> e.nameLength);
        TableColumn<Object, String> name = mkColumn("Name", (GuiNote e) -> e.name);
        TableColumn<Object, String> descriptorLength = mkColumn("Descriptor Length", (GuiNote e) -> e.descriptorLength);
        TableColumn<Object, String> descriptor = mkColumn("Descriptor", (GuiNote e) ->
                ByteArrays.toHexString(e.descriptor, ":"));
        TableColumn<Object, String> type = mkColumn("Type",
                (GuiNote e) -> String.format("0x%08x", e.type));
        TableColumn<Object, String> parsedType = mkColumn("(Parsed Type)", (GuiNote e) -> e.nameLength);

        TableColumn<Object, String> comment = mkColumn("(Comment)", (GuiNote e) -> e.comment);

        tableView.getColumns().addAll(
                nameLength, name,
                descriptorLength, descriptor,
                type, parsedType,
                comment);
    }
    private void displayNotes(Elf32SectionHeader sh) {
        setupNotesTable();

        // TODO: Pass sh instead of name
        List<GuiNote> notes = ElfReader.readNotes(currentElfFile, sh.name()).stream()
                .map(GuiNote::new)
                .collect(toList());

        tableView.getItems().addAll(notes);
    }

    private void setupsStringsTable() {
        clearTable();

        TableColumn<Object, String> value = mkColumn("Value", (String s) -> s);

        tableView.getColumns().addAll(value);
    }
    private void displayStrings(Elf32SectionHeader sh) {
        setupsStringsTable();

        Collection<String> strings = ElfReader.readStringsSection(currentElfFile, sh);
        tableView.getItems().addAll(strings);
    }

    private void setupsInterpreterTable() {
        clearTable();

        TableColumn<Object, String> value = mkColumn("Interpreter", (String s) -> s);

        tableView.getColumns().addAll(value);
    }
    private void displayInterpreter(Elf32SectionHeader sh) {
        setupsInterpreterTable();

        Elf32InterpreterProgramHeader iph = new Elf32InterpreterProgramHeader(
                currentElfFile,
                sh);
        tableView.getItems().add(iph.getInterpreterPath());
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
