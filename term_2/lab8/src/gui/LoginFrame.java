package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import client.RequestSender;
import network.AuthData;
import network.CommandRequest;
import network.CommandResponse;
import network.CommandType;

public class LoginFrame extends JFrame {
    private final RequestSender requestSender;

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel titleLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel loginLabel = new JLabel();
    private final JLabel passwordLabel = new JLabel();
    private final JLabel languageLabel = new JLabel();
    private final JButton loginButton = new JButton();
    private final JButton registerButton = new JButton();
    private final JComboBox<String> languageBox = new JComboBox<>(new String[] {"RU", "NO", "DA", "EN_IN"});

    private Locale currentLocale = GuiResources.EN_IN;

    public LoginFrame(RequestSender requestSender) {
        this.requestSender = requestSender;

        setSize(520, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUi();
        applyLocale();
    }

    private void initUi() {
        JPanel root = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel languagePanel = new JPanel();
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

        root.add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        formPanel.add(loginLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        root.add(formPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> authenticate(CommandType.LOGIN));
        registerButton.addActionListener(e -> authenticate(CommandType.REGISTER));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonsPanel, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        root.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void authenticate(CommandType commandType) {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();

        if (username.isBlank()) {
            statusLabel.setText(t("login.empty"));
            return;
        }

        if (passwordChars.length == 0) {
            statusLabel.setText(t("password.empty"));
            return;
        }

        String password = new String(passwordChars);
        Arrays.fill(passwordChars, '\0');

        statusLabel.setText(t("connecting"));

        new SwingWorker<CommandResponse, Void>() {
            @Override
            protected CommandResponse doInBackground() {
                AuthData authData = new AuthData(username, password);
                CommandRequest request = new CommandRequest(commandType, null, authData);
                return requestSender.send(request);
            }

            @Override
            protected void done() {
                try {
                    CommandResponse response = get();
                    statusLabel.setText(response.getMessage());

                    if (response.isSuccess()) {
                        AuthData authData = new AuthData(username, password);
                        openMainWindow(authData);
                    }
                } catch (Exception e) {
                    statusLabel.setText(MessageFormat.format(t("connection.error"), e.getMessage()));
                }
            }
        }.execute();
    }

    private void openMainWindow(AuthData authData) {
        MainFrame mainFrame = new MainFrame(requestSender, authData, currentLocale);
        mainFrame.setVisible(true);
        dispose();
    }

    private void applyLocale() {
        setTitle(GuiResources.get(currentLocale, "app.title") + " - " + t("auth.title"));
        titleLabel.setText(t("auth.title"));
        loginLabel.setText(t("login") + ":");
        passwordLabel.setText(t("password") + ":");
        languageLabel.setText(t("language") + ":");
        loginButton.setText(t("login"));
        registerButton.setText(t("register"));
    }

    private String t(String key) {
        return GuiResources.get(currentLocale, key);
    }
}
