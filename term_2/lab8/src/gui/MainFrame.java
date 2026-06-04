package gui;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import client.RequestSender;
import models.MusicBand;
import models.MusicGenre;
import network.AuthData;
import network.CollectionElement;
import network.CommandRequest;
import network.CommandResponse;
import network.CommandType;
import network.arguments.GenreArgument;
import network.arguments.InsertArgument;
import network.arguments.KeyArgument;
import network.arguments.MusicBandArgument;
import network.arguments.NumberOfParticipantsArgument;
import network.arguments.UpdateArgument;

public class MainFrame extends JFrame {
    private final RequestSender requestSender;
    private final AuthData authData;
    private Locale currentLocale = GuiResources.EN_IN;
    private LinkedHashMap<Long, MusicBand> collection = new LinkedHashMap<>();
    private Map<Long, String> ownerByKey = new LinkedHashMap<>();
    private JLabel statusLabel;
    private JLabel userLabel;
    private JLabel languageLabel;
    private JLabel tableTitleLabel;
    private JLabel visualizationTitleLabel;
    private JLabel filterLabel;
    private JLabel filterColumnLabel;
    private JLabel sortLabel;
    private JLabel orderLabel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton commandsButton;
    private JComboBox<String> languageBox;
    private MusicBandTableModel tableModel;
    private JTable table;
    private JTextField filterField;
    private JComboBox<String> filterColumnBox;
    private JComboBox<String> sortColumnBox;
    private JComboBox<String> sortOrderBox;
    private VisualizationPanel visualizationPanel;
    private Timer refreshTimer;
    private boolean loadingCollection;

    public MainFrame(RequestSender requestSender, AuthData authData) {
        this(requestSender, authData, GuiResources.EN_IN);
    }

    public MainFrame(RequestSender requestSender, AuthData authData, Locale initialLocale) {
        this.requestSender = requestSender;
        this.authData = authData;
        this.currentLocale = initialLocale;

        setTitle(t("app.title"));
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                stopAutoRefresh();
            }
        });
        setLocationRelativeTo(null);

        initUi();
        loadCollection();
        startAutoRefresh();
    }

    private void initUi() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        userLabel = new JLabel();
        topPanel.add(userLabel, BorderLayout.WEST);

        JPanel controlsPanel = new JPanel();

        addButton = new JButton("insert");
        editButton = new JButton("update");
        deleteButton = new JButton("remove_key");
        commandsButton = new JButton();

        addButton.addActionListener(e -> addBand());
        editButton.addActionListener(e -> editSelectedBand());
        deleteButton.addActionListener(e -> deleteSelectedBand());
        commandsButton.addActionListener(e -> showCommandsMenu(commandsButton));

        controlsPanel.add(addButton);
        controlsPanel.add(editButton);
        controlsPanel.add(deleteButton);
        controlsPanel.add(commandsButton);

        topPanel.add(controlsPanel, BorderLayout.CENTER);

        JPanel languagePanel = new JPanel();
        languageLabel = new JLabel();
        languageBox = new JComboBox<>(new String[] {"RU", "NO", "DA", "EN_IN"});
        languageBox.setSelectedItem(GuiResources.labelForLocale(currentLocale));
        languageBox.addActionListener(e -> {
            Object selected = languageBox.getSelectedItem();
            if (selected != null) {
                currentLocale = GuiResources.localeForLabel(String.valueOf(selected));
                applyLocale();
            }
        });
        languagePanel.add(languageLabel);
        languagePanel.add(languageBox);
        topPanel.add(languagePanel, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(createFilterPanel(), BorderLayout.SOUTH);
        root.add(northPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tableTitleLabel = new JLabel("", SwingConstants.CENTER);
        tablePanel.add(tableTitleLabel, BorderLayout.NORTH);

        tableModel = new MusicBandTableModel();
        tableModel.setLocale(currentLocale);
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(false);

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel visualizationWrapper = new JPanel(new BorderLayout());
        visualizationTitleLabel = new JLabel("", SwingConstants.CENTER);
        visualizationWrapper.add(visualizationTitleLabel, BorderLayout.NORTH);

        visualizationPanel = new VisualizationPanel();
        visualizationPanel.setLocale(currentLocale);
        visualizationWrapper.add(visualizationPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, visualizationWrapper);
        splitPane.setResizeWeight(0.5);

        root.add(splitPane, BorderLayout.CENTER);

        statusLabel = new JLabel();
        root.add(statusLabel, BorderLayout.SOUTH);

        setContentPane(root);
        applyLocale();
    }

    private void loadCollection() {
        if (loadingCollection) {
            return;
        }

        loadingCollection = true;
        statusLabel.setText(t("status") + ": " + t("loading"));

        new SwingWorker<CommandResponse, Void>() {
            @Override
            protected CommandResponse doInBackground() {
                CommandRequest request = new CommandRequest(CommandType.SHOW, null, authData);
                return requestSender.send(request);
            }

            @Override
            protected void done() {
                try {
                    CommandResponse response = get();
                    statusLabel.setText("Status: " + response.getMessage());

                    if (response.isSuccess() && response.getData() != null) {
                        Serializable data = response.getData();

                        if (data instanceof LinkedHashMap<?, ?> map) {
                            LinkedHashMap<Long, MusicBand> loaded = new LinkedHashMap<>();
                            Map<Long, String> loadedOwners = new LinkedHashMap<>();

                            for (var entry : map.entrySet()) {
                                if (entry.getKey() instanceof Long key) {
                                    if (entry.getValue() instanceof CollectionElement element) {
                                        loaded.put(key, element.getMusicBand());
                                        loadedOwners.put(key, element.getOwnerUsername());
                                    } else if (entry.getValue() instanceof MusicBand band) {
                                        loaded.put(key, band);
                                        loadedOwners.put(key, "unknown");
                                    }
                                }
                            }

                            collection = loaded;
                            ownerByKey = loadedOwners;
                            statusLabel.setText(t("status") + ": "
                                    + MessageFormat.format(t("loaded"), collection.size()));
                            refreshTable();
                            visualizationPanel.setCollection(collection, ownerByKey);
                        }
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Load error: " + e.getMessage());
                } finally {
                    loadingCollection = false;
                }
            }
        }.execute();
    }

    private void refreshTable() {
        String filterText = filterField == null ? "" : filterField.getText().trim().toLowerCase();
        int filterColumn = filterColumnBox == null ? 2 : filterColumnBox.getSelectedIndex();
        int sortColumn = sortColumnBox == null ? 2 : sortColumnBox.getSelectedIndex();
        boolean descending = sortOrderBox != null && sortOrderBox.getSelectedIndex() == 1;

        ArrayList<Map.Entry<Long, MusicBand>> rows = collection.entrySet().stream()
                .filter(entry -> filterText.isEmpty()
                        || String.valueOf(getColumnValue(entry, filterColumn)).toLowerCase().contains(filterText))
                .sorted((first, second) -> compareColumnValues(
                        getColumnValue(first, sortColumn),
                        getColumnValue(second, sortColumn)
                ))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

        if (descending) {
            java.util.Collections.reverse(rows);
        }

        tableModel.setRows(rows);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();

        filterLabel = new JLabel();
        filterPanel.add(filterLabel);

        filterField = new JTextField(14);
        filterPanel.add(filterField);

        filterColumnLabel = new JLabel();
        filterPanel.add(filterColumnLabel);
        filterColumnBox = new JComboBox<>(localizedColumnNames());
        filterPanel.add(filterColumnBox);

        sortLabel = new JLabel();
        filterPanel.add(sortLabel);
        sortColumnBox = new JComboBox<>(localizedColumnNames());
        sortColumnBox.setSelectedIndex(2);
        filterPanel.add(sortColumnBox);

        orderLabel = new JLabel();
        filterPanel.add(orderLabel);
        sortOrderBox = new JComboBox<>();
        filterPanel.add(sortOrderBox);

        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshTable();
            }
        });

        filterColumnBox.addActionListener(e -> refreshTable());
        sortColumnBox.addActionListener(e -> refreshTable());
        sortOrderBox.addActionListener(e -> refreshTable());

        return filterPanel;
    }

    private Object getColumnValue(Map.Entry<Long, MusicBand> entry, int columnIndex) {
        MusicBand band = entry.getValue();

        return switch (columnIndex) {
            case 0 -> entry.getKey();
            case 1 -> band.getId();
            case 2 -> band.getName();
            case 3 -> band.getCoordinates().getX();
            case 4 -> band.getCoordinates().getY();
            case 5 -> band.getCreationDate();
            case 6 -> band.getNumberOfParticipants();
            case 7 -> band.getGenre();
            case 8 -> band.getFrontMan().getName();
            case 9 -> band.getFrontMan().getBirthday();
            case 10 -> band.getFrontMan().getHeight();
            case 11 -> band.getFrontMan().getPassportID();
            case 12 -> band.getFrontMan().getEyeColor();
            default -> "";
        };
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private int compareColumnValues(Object first, Object second) {
        if (first == null && second == null) {
            return 0;
        }
        if (first == null) {
            return 1;
        }
        if (second == null) {
            return -1;
        }
        if (first instanceof String firstString && second instanceof String secondString) {
            return firstString.compareToIgnoreCase(secondString);
        }
        if (first instanceof Comparable firstComparable && first.getClass().isInstance(second)) {
            return firstComparable.compareTo(second);
        }
        return String.valueOf(first).compareToIgnoreCase(String.valueOf(second));
    }

    private String t(String key) {
        return GuiResources.get(currentLocale, key);
    }

    private String[] localizedColumnNames() {
        String[] names = new String[MusicBandTableModel.COLUMN_KEYS.length];
        for (int i = 0; i < MusicBandTableModel.COLUMN_KEYS.length; i++) {
            names[i] = t(MusicBandTableModel.COLUMN_KEYS[i]);
        }
        return names;
    }

    private void applyLocale() {
        setTitle(t("app.title"));

        if (userLabel != null) {
            userLabel.setText(t("user") + ": " + authData.getUsername());
        }
        if (languageLabel != null) {
            languageLabel.setText(t("language") + ":");
        }
        if (commandsButton != null) {
            commandsButton.setText(t("commands"));
        }
        if (tableTitleLabel != null) {
            tableTitleLabel.setText(t("table"));
        }
        if (visualizationTitleLabel != null) {
            visualizationTitleLabel.setText(t("visualization"));
        }
        if (filterLabel != null) {
            filterLabel.setText(t("filter") + ":");
        }
        if (filterColumnLabel != null) {
            filterColumnLabel.setText(t("column") + ":");
        }
        if (sortLabel != null) {
            sortLabel.setText(t("sort") + ":");
        }
        if (orderLabel != null) {
            orderLabel.setText(t("order") + ":");
        }
        if (statusLabel != null && (statusLabel.getText() == null || statusLabel.getText().isBlank())) {
            statusLabel.setText(t("status") + ": ");
        }

        updateColumnBoxes();

        if (sortOrderBox != null) {
            int selectedOrder = Math.max(0, sortOrderBox.getSelectedIndex());
            sortOrderBox.setModel(new DefaultComboBoxModel<>(new String[] {t("asc"), t("desc")}));
            sortOrderBox.setSelectedIndex(Math.min(selectedOrder, 1));
        }

        if (tableModel != null) {
            tableModel.setLocale(currentLocale);
            refreshTable();
        }
        if (visualizationPanel != null) {
            visualizationPanel.setLocale(currentLocale);
        }
    }

    private void updateColumnBoxes() {
        String[] names = localizedColumnNames();

        if (filterColumnBox != null) {
            int selected = Math.max(0, filterColumnBox.getSelectedIndex());
            filterColumnBox.setModel(new DefaultComboBoxModel<>(names));
            filterColumnBox.setSelectedIndex(Math.min(selected, names.length - 1));
        }

        if (sortColumnBox != null) {
            int selected = sortColumnBox.getSelectedIndex() < 0 ? 2 : sortColumnBox.getSelectedIndex();
            sortColumnBox.setModel(new DefaultComboBoxModel<>(names));
            sortColumnBox.setSelectedIndex(Math.min(selected, names.length - 1));
        }
    }

    private void addBand() {
        String keyText = JOptionPane.showInputDialog(
                this,
                "Enter collection key:",
                "Add",
                JOptionPane.QUESTION_MESSAGE
        );

        if (keyText == null) {
            return;
        }

        Long key;
        try {
            key = Long.valueOf(keyText.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Key must be an integer number.",
                    "Input error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        MusicBandDialog dialog = new MusicBandDialog(this, "insert", null, currentLocale);
        dialog.setVisible(true);

        MusicBand band = dialog.getResult();
        if (band == null) {
            return;
        }

        sendInsert(key, band);
    }

    private void sendInsert(Long key, MusicBand band) {
        statusLabel.setText("Status: Adding object...");

        new SwingWorker<CommandResponse, Void>() {
            @Override
            protected CommandResponse doInBackground() {
                InsertArgument argument = new InsertArgument(key, band);
                CommandRequest request = new CommandRequest(CommandType.INSERT, argument, authData);
                return requestSender.send(request);
            }

            @Override
            protected void done() {
                try {
                    CommandResponse response = get();
                    statusLabel.setText("Status: " + response.getMessage());

                    if (response.isSuccess()) {
                        loadCollection();
                    } else {
                        showErrorMessage("insert", response.getMessage());
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Add error: " + e.getMessage());
                    showErrorMessage("insert", e.getMessage());
                }
            }
        }.execute();
    }

    private Map.Entry<Long, MusicBand> getSelectedEntry() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            return tableModel.getEntryAt(modelRow);
        }

        if (visualizationPanel != null && visualizationPanel.getSelectedEntry() != null) {
            return visualizationPanel.getSelectedEntry();
        }

        JOptionPane.showMessageDialog(
                this,
                "Select an object first.",
                "Selection error",
                JOptionPane.WARNING_MESSAGE
        );
        return null;
    }

    private void editSelectedBand() {
        Map.Entry<Long, MusicBand> selectedEntry = getSelectedEntry();
        if (selectedEntry == null) {
            return;
        }

        MusicBand oldBand = selectedEntry.getValue();

        MusicBandDialog dialog = new MusicBandDialog(this, "update", oldBand, currentLocale);
        dialog.setVisible(true);

        MusicBand newBand = dialog.getResult();
        if (newBand == null) {
            return;
        }

        sendUpdate(oldBand.getId(), newBand);
    }

    private void sendUpdate(Long id, MusicBand band) {
        statusLabel.setText("Status: Updating object...");

        new SwingWorker<CommandResponse, Void>() {
            @Override
            protected CommandResponse doInBackground() {
                UpdateArgument argument = new UpdateArgument(id, band);
                CommandRequest request = new CommandRequest(CommandType.UPDATE, argument, authData);
                return requestSender.send(request);
            }

            @Override
            protected void done() {
                try {
                    CommandResponse response = get();
                    statusLabel.setText("Status: " + response.getMessage());

                    if (response.isSuccess()) {
                        loadCollection();
                    } else {
                        showErrorMessage("update", response.getMessage());
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Update error: " + e.getMessage());
                    showErrorMessage("update", e.getMessage());
                }
            }
        }.execute();
    }

    private void deleteSelectedBand() {
        Map.Entry<Long, MusicBand> selectedEntry = getSelectedEntry();
        if (selectedEntry == null) {
            return;
        }

        Long key = selectedEntry.getKey();

        int answer = JOptionPane.showConfirmDialog(
                this,
                "remove_key " + key + "?",
                "remove_key",
                JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        sendDelete(key);
    }

    private void sendDelete(Long key) {
        statusLabel.setText("Status: Deleting object...");

        new SwingWorker<CommandResponse, Void>() {
            @Override
            protected CommandResponse doInBackground() {
                KeyArgument argument = new KeyArgument(key);
                CommandRequest request = new CommandRequest(CommandType.REMOVE_KEY, argument, authData);
                return requestSender.send(request);
            }

            @Override
            protected void done() {
                try {
                    CommandResponse response = get();
                    statusLabel.setText("Status: " + response.getMessage());

                    if (response.isSuccess()) {
                        loadCollection();
                    } else {
                        showErrorMessage("remove_key", response.getMessage());
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Delete error: " + e.getMessage());
                    showErrorMessage("remove_key", e.getMessage());
                }
            }
        }.execute();
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(3000, e -> loadCollection());
        refreshTimer.start();
    }

    private void showCommandsMenu(JButton ownerButton) {
        JPopupMenu menu = new JPopupMenu();

        addMenuItem(menu, "help", () -> sendCommand(CommandType.HELP, null, false, false));
        addMenuItem(menu, "info", () -> sendCommand(CommandType.INFO, null, false, false));
        addMenuItem(menu, "clear", this::confirmClear);
        addMenuItem(menu, "show_even", () -> sendCommand(CommandType.SHOW_EVEN, null, false, false));
        addMenuItem(menu, "print_field_descending_front_man",
                () -> sendCommand(CommandType.PRINT_FIELD_DESCENDING_FRONT_MAN, null, false, false));
        addMenuItem(menu, "count_greater_than_number_of_participants", this::countGreaterThanParticipants);
        addMenuItem(menu, "filter_greater_than_genre", this::filterGreaterThanGenre);
        addMenuItem(menu, "remove_greater_key", this::removeGreaterKey);
        addMenuItem(menu, "remove_lower", this::removeLower);
        addMenuItem(menu, "replace_if_greater", this::replaceIfGreater);
        addMenuItem(menu, "exit", () -> sendCommand(CommandType.EXIT, null, false, true));

        menu.show(ownerButton, 0, ownerButton.getHeight());
    }

    private void addMenuItem(JPopupMenu menu, String title, Runnable action) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(e -> action.run());
        menu.add(item);
    }

    private void confirmClear() {
        int answer = JOptionPane.showConfirmDialog(
                this,
                "clear your objects?",
                "clear",
                JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {
            sendCommand(CommandType.CLEAR, null, true, false);
        }
    }

    private void countGreaterThanParticipants() {
        String value = JOptionPane.showInputDialog(
                this,
                "numberOfParticipants:",
                "count_greater_than_number_of_participants",
                JOptionPane.QUESTION_MESSAGE
        );

        if (value == null) {
            return;
        }

        try {
            Integer participants = Integer.valueOf(value.trim());
            sendCommand(
                    CommandType.COUNT_GREATER_THAN_NUMBER_OF_PARTICIPANTS,
                    new NumberOfParticipantsArgument(participants),
                    false,
                    false
            );
        } catch (NumberFormatException e) {
            showInputError("Value must be an integer number.");
        }
    }

    private void filterGreaterThanGenre() {
        MusicGenre genre = (MusicGenre) JOptionPane.showInputDialog(
                this,
                "genre:",
                "filter_greater_than_genre",
                JOptionPane.QUESTION_MESSAGE,
                null,
                MusicGenre.values(),
                MusicGenre.values()[0]
        );

        if (genre != null) {
            sendCommand(CommandType.FILTER_GREATER_THAN_GENRE, new GenreArgument(genre), false, false);
        }
    }

    private void removeGreaterKey() {
        String value = JOptionPane.showInputDialog(
                this,
                "key:",
                "remove_greater_key",
                JOptionPane.QUESTION_MESSAGE
        );

        if (value == null) {
            return;
        }

        try {
            Long key = Long.valueOf(value.trim());
            sendCommand(CommandType.REMOVE_GREATER_KEY, new KeyArgument(key), true, false);
        } catch (NumberFormatException e) {
            showInputError("Key must be an integer number.");
        }
    }

    private void removeLower() {
        MusicBandDialog dialog = new MusicBandDialog(this, "remove_lower", null, currentLocale);
        dialog.setVisible(true);

        MusicBand band = dialog.getResult();
        if (band != null) {
            sendCommand(CommandType.REMOVE_LOWER, new MusicBandArgument(band), true, false);
        }
    }

    private void replaceIfGreater() {
        String keyText = JOptionPane.showInputDialog(
                this,
                "key:",
                "replace_if_greater",
                JOptionPane.QUESTION_MESSAGE
        );

        if (keyText == null) {
            return;
        }

        Long key;
        try {
            key = Long.valueOf(keyText.trim());
        } catch (NumberFormatException e) {
            showInputError("Key must be an integer number.");
            return;
        }

        MusicBandDialog dialog = new MusicBandDialog(this, "replace_if_greater", null, currentLocale);
        dialog.setVisible(true);

        MusicBand band = dialog.getResult();
        if (band != null) {
            sendCommand(CommandType.REPLACE_IF_GREATER, new InsertArgument(key, band), true, false);
        }
    }

    private void sendCommand(CommandType type, Serializable argument, boolean reloadOnSuccess, boolean closeOnSuccess) {
        statusLabel.setText("Status: " + type.name().toLowerCase() + "...");

        new SwingWorker<CommandResponse, Void>() {
            @Override
            protected CommandResponse doInBackground() {
                CommandRequest request = new CommandRequest(type, argument, authData);
                return requestSender.send(request);
            }

            @Override
            protected void done() {
                try {
                    CommandResponse response = get();
                    statusLabel.setText("Status: " + response.getMessage());

                    if (response.isSuccess()) {
                        showCommandResponse(type.name().toLowerCase(), response.getMessage());
                    } else {
                        showErrorMessage(type.name().toLowerCase(), response.getMessage());
                    }

                    if (response.isSuccess() && reloadOnSuccess) {
                        loadCollection();
                    }

                    if (response.isSuccess() && closeOnSuccess) {
                        stopAutoRefresh();
                        dispose();
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Command error: " + e.getMessage());
                    showErrorMessage(type.name().toLowerCase(), e.getMessage());
                }
            }
        }.execute();
    }

    private void showCommandResponse(String title, String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        JTextArea textArea = new JTextArea(message, 16, 60);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(textArea),
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showInputError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Input error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showErrorMessage(String title, String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        JTextArea textArea = new JTextArea(message, 10, 50);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(textArea),
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void stopAutoRefresh() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}
