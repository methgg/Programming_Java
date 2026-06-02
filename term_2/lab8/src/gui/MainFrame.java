package gui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import client.RequestSender;


public class MainFrame extends JFrame {
    private final RequestSender requestSender;
    private final String username;

    public MainFrame(RequestSender requestSender, String username) {
        this.requestSender = requestSender;
        this.username = username;

        setTitle("MusicBand Gallery");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUi();
    }

    private void initUi() {
        JPanel root = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("User: " + username), BorderLayout.WEST);

        JPanel controlsPanel = new JPanel();
        controlsPanel.add(new JButton("Add"));
        controlsPanel.add(new JButton("Edit"));
        controlsPanel.add(new JButton("Delete"));
        topPanel.add(controlsPanel, BorderLayout.CENTER);

        topPanel.add(new JLabel("Language: RU | NO | DA | EN_IN"), BorderLayout.EAST);

        root.add(topPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("TABLE", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel visualizationPanel = new JPanel(new BorderLayout());
        visualizationPanel.add(new JLabel("VISUALIZATION", SwingConstants.CENTER), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, visualizationPanel);
        splitPane.setResizeWeight(0.5);

        root.add(splitPane, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("Status: ");
        root.add(statusLabel, BorderLayout.SOUTH);

        setContentPane(root);
    }
}