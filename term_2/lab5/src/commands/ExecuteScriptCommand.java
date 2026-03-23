package commands;

import manager.CollectionManager;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Читает команды из файла сценария и выполняет их в консольном режиме.
 */
public class ExecuteScriptCommand implements Command {
    private final CollectionManager cm;
    private String args;

    public ExecuteScriptCommand(CollectionManager cm) { this.cm = cm; }
    @Override public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        try (Scanner sc = new Scanner(new File(args))) {
            InputProvider.setScanner(sc);
            try {
            Map<String, Command> commands = createCommands();
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                System.out.println("> " + line);
                String[] parts = line.split(" ", 2);
                Command cmd = commands.get(parts[0]);

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

    private Map<String, Command> createCommands() {
        Map<String, Command> commands = new HashMap<>();
        commands.put("help", new HelpCommand());
        commands.put("info", new InfoCommand(cm));
        commands.put("show", new ShowCommand(cm));
        commands.put("insert", new InsertCommand(cm));
        commands.put("update", new UpdateCommand(cm));
        commands.put("remove_key", new RemoveKeyCommand(cm));
        commands.put("clear", new ClearCommand(cm));
        commands.put("save", new SaveCommand(cm));
        commands.put("execute_script", new ExecuteScriptCommand(cm));
        commands.put("exit", new ExitCommand());
        commands.put("remove_lower", new RemoveLowerCommand(cm));
        commands.put("replace_if_greater", new ReplaceIfGreaterCommand(cm));
        commands.put("remove_greater_key", new RemoveGreaterKeyCommand(cm));
        commands.put("count_greater_than_number_of_participants", new CountGreaterThanNumberOfParticipantsCommand(cm));
        commands.put("filter_greater_than_genre", new FilterGreaterThanGenreCommand(cm));
        commands.put("print_field_descending_front_man", new PrintFieldDescendingFrontManCommand(cm));
        return commands;
    }
}