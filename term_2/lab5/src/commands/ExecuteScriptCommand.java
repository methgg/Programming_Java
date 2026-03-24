package commands;

import java.io.File;
import java.util.Scanner;

import manager.CollectionManager;
import manager.CommandManager;
import util.InputProvider;

public class ExecuteScriptCommand implements Command {
    private final CollectionManager cm;
    private final CommandManager commandManager;
    private String args;

    public ExecuteScriptCommand(CollectionManager cm, CommandManager commandManager) { 
        this.cm = cm; 
        this.commandManager = commandManager;
    }
    @Override public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        try (Scanner sc = new Scanner(new File(args))) {
            InputProvider.setScanner(sc);
            try {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                System.out.println("> " + line);
                String[] parts = line.split(" ", 2);
                Command cmd = commandManager.getCommand(parts[0]);

                if (cmd == null) {
                    System.out.println("Команда не найдена. Введите help");
                    continue;
                }

                cmd.setArgs(parts.length > 1 ? parts[1] : "");
                cmd.execute();
            }
            } finally {
                InputProvider.clear();
            }
        } catch (Exception e) {
            System.out.println("Ошибка скрипта: " + e.getMessage());
        }
    }

}