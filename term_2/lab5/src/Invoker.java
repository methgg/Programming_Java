import commands.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import manager.CollectionManager;

/**
 * Консольный “интерпретатор” команд: читает строку из stdin, находит нужную реализацию
 * в {@code commands} и вызывает её.
 */
public class Invoker {
    private Map<String, Command> commands = new HashMap<>();

    public Invoker(CollectionManager cm) {
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

    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        InputProvider.setScanner(sc);
        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line.isEmpty()) continue;

            String[] parts = line.split(" ", 2);
            Command cmd = commands.get(parts[0]);
            if (cmd != null) {
                cmd.setArgs(parts.length > 1 ? parts[1] : "");
                cmd.execute();
            } else System.out.println("Команда не найдена. Введите help");
        }
    }

    public static void main(String[] args) {
        CollectionManager cm = new CollectionManager();
        // загружаем коллекцию из файла, если есть
        String filename = System.getenv("COLLECTIONFILE");
        if (filename != null) {
            cm.getCollection().putAll(util.JsonUtil.readFromFile(filename));
        }
        Invoker invoker = new Invoker(cm);
        invoker.start();
    }
}