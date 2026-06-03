package gui;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import client.RequestSender;
import models.MusicBand;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.CommandType;
import network.arguments.InsertArgument;
import network.arguments.KeyArgument;
import network.arguments.UpdateArgument;


public class MainFrame extends JFrame {
    private final RequestSender requestSender;
    private final AuthData authData;
    private LinkedHashMap<Long, MusicBand> collection = new LinkedHashMap<>();
    private JLabel statusLabel;
    private MusicBandTableModel tableModel;
    private JTable table;
    private JTextField filterField;
    private JComboBox<String> filterColumnBox;
    private JComboBox<String> sortColumnBox;
    private JComboBox<String> sortOrderBox;

    public MainFrame(RequestSender requestSender, AuthData authData) {
        this.requestSender = requestSender;
        this.authData = authData;

        setTitle("MusicBand Gallery");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUi();
        loadCollection();
    }

    private void initUi() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("User: " + authData.getUsername()), BorderLayout.WEST);

        JPanel controlsPanel = new JPanel();

        JButton addButton = new JButton("insert");
        JButton editButton = new JButton("update");
        JButton deleteButton = new JButton("remove_key");

        addButton.addActionListener(e -> addBand());
        editButton.addActionListener(e -> editSelectedBand());
        deleteButton.addActionListener(e -> deleteSelectedBand());

        controlsPanel.add(addButton);
        controlsPanel.add(editButton);
        controlsPanel.add(deleteButton);

        topPanel.add(controlsPanel, BorderLayout.CENTER);

        topPanel.add(new JLabel("Language: RU | NO | DA | EN_IN"), BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(createFilterPanel(), BorderLayout.SOUTH);
        root.add(northPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("TABLE", SwingConstants.CENTER), BorderLayout.NORTH);

        tableModel = new MusicBandTableModel();
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(false);

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel visualizationPanel = new JPanel(new BorderLayout());
        visualizationPanel.add(new JLabel("VISUALIZATION", SwingConstants.CENTER), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, visualizationPanel);
        splitPane.setResizeWeight(0.5);

        root.add(splitPane, BorderLayout.CENTER);

        statusLabel = new JLabel("Status: ");
        root.add(statusLabel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void loadCollection() {
        statusLabel.setText("Status: Loading collection...");

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

                            for (var entry : map.entrySet()) {
                                if (entry.getKey() instanceof Long key
                                        && entry.getValue() instanceof MusicBand band) {
                                    loaded.put(key, band);
                                }
                            }

                            collection = loaded;
                            statusLabel.setText("Status: Loaded " + collection.size() + " objects.");
                            refreshTable();
                        }
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Load error: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void refreshTable() {
        String filterText = filterField == null ? "" : filterField.getText().trim().toLowerCase();
        int filterColumn = filterColumnBox == null ? 2 : filterColumnBox.getSelectedIndex();
        int sortColumn = sortColumnBox == null ? 2 : sortColumnBox.getSelectedIndex();
        boolean descending = sortOrderBox != null && "Desc".equals(sortOrderBox.getSelectedItem());

        ArrayList<Map.Entry<Long, MusicBand>> rows = collection.entrySet().stream()
                .filter(entry -> filterText.isEmpty()
                        || String.valueOf(getColumnValue(entry, filterColumn)).toLowerCase().contains(filterText))
                .sorted(Comparator.comparing(entry -> String.valueOf(getColumnValue(entry, sortColumn)),
                        String.CASE_INSENSITIVE_ORDER))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

        if (descending) {
            java.util.Collections.reverse(rows);
        }

        tableModel.setRows(rows);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();

        filterPanel.add(new JLabel("Filter:"));

        filterField = new JTextField(14);
        filterPanel.add(filterField);

        filterPanel.add(new JLabel("Column:"));
        filterColumnBox = new JComboBox<>(MusicBandTableModel.COLUMNS);
        filterPanel.add(filterColumnBox);

        filterPanel.add(new JLabel("Sort:"));
        sortColumnBox = new JComboBox<>(MusicBandTableModel.COLUMNS);
        sortColumnBox.setSelectedItem("Name");
        filterPanel.add(sortColumnBox);

        filterPanel.add(new JLabel("Order:"));
        sortOrderBox = new JComboBox<>(new String[] {"Asc", "Desc"});
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

        MusicBandDialog dialog = new MusicBandDialog(this, "Add music band", null);
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
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Add error: " + e.getMessage());
                }
            }
        }.execute();
    }

    private Map.Entry<Long, MusicBand> getSelectedEntry() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select an object first.",
                    "Selection error",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        return tableModel.getEntryAt(modelRow);
    }

    private void editSelectedBand() {
        Map.Entry<Long, MusicBand> selectedEntry = getSelectedEntry();
        if (selectedEntry == null) {
            return;
        }

        MusicBand oldBand = selectedEntry.getValue();

        MusicBandDialog dialog = new MusicBandDialog(this, "update music band", oldBand);
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
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Update error: " + e.getMessage());
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
                    }
                } catch (Exception e) {
                    statusLabel.setText("Status: Delete error: " + e.getMessage());
                }
            }
        }.execute();
    }
}
