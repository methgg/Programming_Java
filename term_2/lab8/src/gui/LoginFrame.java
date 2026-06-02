package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.RequestSender;

public class LoginFrame extends JFrame{
    private final RequestSender requestSender;

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);

    public LoginFrame(RequestSender requestSender) {
        this.requestSender = requestSender;

        setTitle("MusicBand Gallery - Login");
        setSize(420, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUi();
    }

    private void initUi() {
        JPanel root = new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Authorization", SwingConstants.CENTER);
        root.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        formPanel.add(new JLabel("Login:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        root.add(formPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(e -> openMainWindow());
        registerButton.addActionListener(e -> openMainWindow());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonsPanel, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        root.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void openMainWindow() {
        String username = usernameField.getText().trim();

        MainFrame mainFrame = new MainFrame(requestSender, username);
        mainFrame.setVisible(true);
        dispose();
    }
}