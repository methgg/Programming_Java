import java.util.Scanner;

import commands.Command;
import commands.ExecuteScriptCommand;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import manager.CommandManager;
import util.IdGenerator;
import util.InputProvider;

/**
 * Класс, который запускает программу
 */
public class Invoker {
    private CommandManager commandManager;

    public Invoker(CollectionManager cm) {
        this.commandManager = new CommandManager(cm);
        commandManager.register("execute_script", new ExecuteScriptCommand(cm, commandManager));
    }
    
    public void start() {
        Scanner sc = new Scanner(System.in);
        InputProvider.setScanner(sc);
        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line.isEmpty()) continue;

            String[] parts = line.split(" ", 2);
            Command cmd = commandManager.getCommand(parts[0]);
            if (cmd != null) {
                cmd.setArgs(parts.length > 1 ? parts[1] : "");
                cmd.execute();
            } else System.out.println(ErrorMessages.UNKNOWN_COMMAND);
        }
    }

    public static void main(String[] args) {
        CollectionManager cm = new CollectionManager();
        String filename = System.getenv("COLLECTIONFILE");
        if (filename != null) {
            cm.getCollection().putAll(util.JsonUtil.readFromFile(filename));
            cm.getCollection().forEach((key, band) -> band.setId(key));
            long maxId = cm.getCollection().keySet().stream().mapToLong(Long::longValue).max().orElse(0);
            IdGenerator.updateCurrentId(maxId);
        }
        Invoker invoker = new Invoker(cm);
        invoker.start();
    }
}
