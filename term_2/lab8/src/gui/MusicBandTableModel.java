package gui;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import models.MusicBand;

public class MusicBandTableModel extends AbstractTableModel {
    public static final String[] COLUMN_KEYS = {
            "column.key",
            "column.id",
            "column.name",
            "column.x",
            "column.y",
            "column.creationDate",
            "column.participants",
            "column.genre",
            "column.frontMan",
            "column.birthday",
            "column.height",
            "column.passport",
            "column.eyeColor"
    };

    private final List<Map.Entry<Long, MusicBand>> rows = new ArrayList<>();
    private Locale locale = GuiResources.EN_IN;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(locale);
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(locale);
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
        this.dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale);
        this.numberFormat = NumberFormat.getNumberInstance(locale);
        fireTableStructureChanged();
    }

    public String[] getColumnNames() {
        String[] names = new String[COLUMN_KEYS.length];
        for (int i = 0; i < COLUMN_KEYS.length; i++) {
            names[i] = GuiResources.get(locale, COLUMN_KEYS[i]);
        }
        return names;
    }

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
        return COLUMN_KEYS.length;
    }

    @Override
    public String getColumnName(int column) {
        return GuiResources.get(locale, COLUMN_KEYS[column]);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Map.Entry<Long, MusicBand> entry = rows.get(rowIndex);
        Long key = entry.getKey();
        MusicBand band = entry.getValue();

        return switch (columnIndex) {
            case 0 -> numberFormat.format(key);
            case 1 -> numberFormat.format(band.getId());
            case 2 -> band.getName();
            case 3 -> numberFormat.format(band.getCoordinates().getX());
            case 4 -> numberFormat.format(band.getCoordinates().getY());
            case 5 -> band.getCreationDate().format(dateTimeFormatter);
            case 6 -> numberFormat.format(band.getNumberOfParticipants());
            case 7 -> band.getGenre();
            case 8 -> band.getFrontMan().getName();
            case 9 -> band.getFrontMan().getBirthday() == null
                    ? ""
                    : band.getFrontMan().getBirthday().format(dateFormatter);
            case 10 -> band.getFrontMan().getHeight() == null
                    ? ""
                    : numberFormat.format(band.getFrontMan().getHeight());
            case 11 -> band.getFrontMan().getPassportID();
            case 12 -> band.getFrontMan().getEyeColor();
            default -> "";
        };
    }
}
