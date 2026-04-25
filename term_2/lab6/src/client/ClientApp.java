package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import exceptions.ErrorMessages;
import network.CommandRequest;
import network.CommandResponse;
import util.ConsoleReader;


/**
 * Класс, который запускает программу
 */
public class ClientApp {
    private final ClientCommandManager clientCommandManager;
    private final ClientCommandParser clientCommandParser;
    private final RequestSender requestSender;

    private final Set<String> activeScripts = new HashSet<>();

    public ClientApp(RequestSender requestSender) {
        this.clientCommandManager = new ClientCommandManager();
        this.clientCommandParser = new ClientCommandParser(clientCommandManager);
        this.requestSender = requestSender;
    }

    
    public void start() {
        while (true) {
            String line;

            try {
                line = ConsoleReader.readLineWithTabCompletion("> ", clientCommandManager.getCommandNames());
            } catch (Exception e) {
                System.out.println(ErrorMessages.inputError(e.getMessage()));
                continue;
            }
            if (line == null) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            if (executeCommandLine(line)) {
                break;
            }
                
        }
    }
    private boolean executeCommandLine(String line) {
        String[] parts = line.split(" ", 2);
        String commandName = parts[0];
        String commandArgs = parts.length > 1 ? parts[1].trim() : "";

        if (commandName.equals("execute_script")) {
            if (commandArgs.isEmpty()) {
                System.out.println(ErrorMessages.missingArgument(commandName));
                return false;
            }
            if (activeScripts.contains(commandArgs)) {
                System.out.println(ErrorMessages.scriptError(ErrorMessages.SCRIPT_RECURSION + commandArgs));
                return false;
            }
            activeScripts.add(commandArgs);
            try (Scanner scriptScanner = new Scanner(new File(commandArgs))) {
                while (scriptScanner.hasNextLine()) {
                    String scriptLine = scriptScanner.nextLine().trim();

                    if (scriptLine.isEmpty()) {
                        continue;
                    }
                      
                    if (executeCommandLine(scriptLine)) {
                        return true;
                    }
                }
                    
            } catch (FileNotFoundException e) {
                System.out.println(ErrorMessages.fileNotFound(commandArgs));
            } finally {
                activeScripts.remove(commandArgs);
            }
                
            return false;
        }
        try {
            CommandRequest request = clientCommandParser.parse(line);
            CommandResponse response = requestSender.send(request);


            if (response.getMessage() != null && !response.getMessage().isEmpty()) {
                System.out.println(response.getMessage());
            }

            return request.getCommandType() == network.CommandType.EXIT;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
