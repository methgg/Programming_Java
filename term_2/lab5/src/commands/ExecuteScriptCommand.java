package commands;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import manager.CommandManager;
import util.InputProvider;

/**
 * Класс, который реализует команду, которая считывает и исполняет скрипт из указанного файла.
 */

public class ExecuteScriptCommand implements Command {
    private final CollectionManager cm;
    private final CommandManager commandManager;
    private String args;
    private static final Set<String> executingScripts = new HashSet<>();

    public ExecuteScriptCommand(CollectionManager cm, CommandManager commandManager) { 
        this.cm = cm; 
        this.commandManager = commandManager;
    }
    @Override public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        String scriptPath;
        try {
            scriptPath = new File(args).getCanonicalPath();
        } catch (IOException e) {
            System.out.println(ErrorMessages.scriptError(e.getMessage()));
            return;
        }

        if (executingScripts.contains(scriptPath)) {
            System.out.println(ErrorMessages.SCRIPT_RECURSION + scriptPath);
            return;
        }

        executingScripts.add(scriptPath);
        Scanner previousScanner = InputProvider.getCurrentScanner();

        try (Scanner sc = new Scanner(new File(scriptPath))) {
            InputProvider.setScanner(sc);
            try {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.isEmpty()) continue;

                    System.out.println("> " + line);
                    String[] parts = line.split(" ", 2);
                    Command cmd = commandManager.getCommand(parts[0]);

                    if (cmd == null) {
                        System.out.println(ErrorMessages.UNKNOWN_COMMAND);
                        continue;
                    }

                    cmd.setArgs(parts.length > 1 ? parts[1] : "");
                    cmd.execute();
                }
            } finally {
                if (previousScanner != null) {
                    InputProvider.setScanner(previousScanner);
                } else {
                    InputProvider.clear();
                }
            }
        } catch (Exception e) {
            System.out.println(ErrorMessages.scriptError(e.getMessage()));
        } finally {
            executingScripts.remove(scriptPath);
        }
    }
    
    @Override 
    public String getDescription(){
        return "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме";
    }
}
