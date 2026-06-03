package gui;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import models.MusicBand;

public class MusicBandTableModel extends AbstractTableModel {
    public static final String[] COLUMNS = {
            "Key",
            "ID",
            "Name",
            "X",
            "Y",
            "Creation Date",
            "Participants",
            "Genre",
            "Front Man",
            "Birthday",
            "Height",
            "Passport",
            "Eye Color"
    };

    private final List<Map.Entry<Long, MusicBand>> rows = new ArrayList<>();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void setRows(List<Map.Entry<Long, MusicBand>> newRows) {
        rows.clear();
        rows.addAll(newRows);
        fireTableDataChanged();
    }

    public Map.Entry<Long, MusicBand> getEntryAt(int rowIndex) {
        return rows.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Map.Entry<Long, MusicBand> entry = rows.get(rowIndex);
        Long key = entry.getKey();
        MusicBand band = entry.getValue();

        return switch (columnIndex) {
            case 0 -> key;
            case 1 -> band.getId();
            case 2 -> band.getName();
            case 3 -> band.getCoordinates().getX();
            case 4 -> band.getCoordinates().getY();
            case 5 -> band.getCreationDate().format(dateTimeFormatter);
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
}