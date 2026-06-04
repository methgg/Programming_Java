package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Color;
import models.Coordinates;
import models.MusicBand;
import models.MusicGenre;
import models.Person;

public class MusicBandDialog extends JDialog {
    private final Locale locale;

    private final JTextField nameField = new JTextField();
    private final JTextField xField = new JTextField();
    private final JTextField yField = new JTextField();
    private final JTextField participantsField = new JTextField();
    private final JComboBox<MusicGenre> genreBox = new JComboBox<>(MusicGenre.values());

    private final JTextField frontManNameField = new JTextField();
    private final JTextField birthdayField = new JTextField();
    private final JTextField heightField = new JTextField();
    private final JTextField passportField = new JTextField();
    private final JComboBox<Color> eyeColorBox = new JComboBox<>();

    private MusicBand result;

    public MusicBandDialog(java.awt.Frame owner, String title, MusicBand initialBand) {
        this(owner, title, initialBand, GuiResources.EN_IN);
    }

    public MusicBandDialog(java.awt.Frame owner, String title, MusicBand initialBand, Locale locale) {
        super(owner, title, true);
        this.locale = locale;

        eyeColorBox.addItem(null);
        for (Color color : Color.values()) {
            eyeColorBox.addItem(color);
        }

        setSize(500, 430);
        setLocationRelativeTo(owner);

        initUi();

        if (initialBand != null) {
            fillFields(initialBand);
        }
    }

    private void initUi() {
        JPanel root = new JPanel(new BorderLayout(8, 8));

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 6, 6));

        formPanel.add(new JLabel(t("field.name") + ":"));
        formPanel.add(nameField);

        formPanel.add(new JLabel(t("field.x") + ":"));
        formPanel.add(xField);

        formPanel.add(new JLabel(t("field.y") + ":"));
        formPanel.add(yField);

        formPanel.add(new JLabel(t("field.participants") + ":"));
        formPanel.add(participantsField);

        formPanel.add(new JLabel(t("field.genre") + ":"));
        formPanel.add(genreBox);

        formPanel.add(new JLabel(t("field.frontManName") + ":"));
        formPanel.add(frontManNameField);

        formPanel.add(new JLabel(t("field.birthday") + ":"));
        formPanel.add(birthdayField);

        formPanel.add(new JLabel(t("field.height") + ":"));
        formPanel.add(heightField);

        formPanel.add(new JLabel(t("field.passport") + ":"));
        formPanel.add(passportField);

        formPanel.add(new JLabel(t("field.eyeColor") + ":"));
        formPanel.add(eyeColorBox);

        root.add(formPanel, BorderLayout.CENTER);

        JButton okButton = new JButton(t("ok"));
        JButton cancelButton = new JButton(t("cancel"));

        okButton.addActionListener(e -> submit());
        cancelButton.addActionListener(e -> {
            result = null;
            dispose();
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        root.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void fillFields(MusicBand band) {
        nameField.setText(band.getName());
        xField.setText(String.valueOf(band.getCoordinates().getX()));
        yField.setText(String.valueOf(band.getCoordinates().getY()));
        participantsField.setText(String.valueOf(band.getNumberOfParticipants()));
        genreBox.setSelectedItem(band.getGenre());

        frontManNameField.setText(band.getFrontMan().getName());

        if (band.getFrontMan().getBirthday() != null) {
            birthdayField.setText(String.valueOf(band.getFrontMan().getBirthday()));
        }

        if (band.getFrontMan().getHeight() != null) {
            heightField.setText(String.valueOf(band.getFrontMan().getHeight()));
        }

        passportField.setText(band.getFrontMan().getPassportID());
        eyeColorBox.setSelectedItem(band.getFrontMan().getEyeColor());
    }

    private void submit() {
        try {
            String name = nameField.getText().trim();
            Integer x = Integer.valueOf(xField.getText().trim());
            Double y = Double.valueOf(yField.getText().trim());
            Integer participants = Integer.valueOf(participantsField.getText().trim());
            MusicGenre genre = (MusicGenre) genreBox.getSelectedItem();

            String frontManName = frontManNameField.getText().trim();

            LocalDate birthday = null;
            String birthdayText = birthdayField.getText().trim();
            if (!birthdayText.isEmpty()) {
                birthday = LocalDate.parse(birthdayText);
            }

            Long height = null;
            String heightText = heightField.getText().trim();
            if (!heightText.isEmpty()) {
                height = Long.valueOf(heightText);
            }

            String passport = passportField.getText().trim();
            Color eyeColor = (Color) eyeColorBox.getSelectedItem();

            Coordinates coordinates = new Coordinates(x, y);
            Person frontMan = new Person(frontManName, birthday, height, passport, eyeColor);
            result = new MusicBand(name, coordinates, participants, genre, frontMan);

            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    t("input.error"),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public MusicBand getResult() {
        return result;
    }

    private String t(String key) {
        return GuiResources.get(locale, key);
    }
}
