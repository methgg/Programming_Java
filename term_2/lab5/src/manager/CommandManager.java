package manager;

import java.util.HashMap;

import commands.ClearCommand;
import commands.Command;
import commands.CountGreaterThanNumberOfParticipantsCommand;
import commands.ExitCommand;
import commands.FilterGreaterThanGenreCommand;
import commands.HelpCommand;
import commands.InfoCommand;
import commands.InsertCommand;
import commands.PrintFieldDescendingFrontManCommand;
import commands.RemoveGreaterKeyCommand;
import commands.RemoveKeyCommand;
import commands.RemoveLowerCommand;
import commands.ReplaceIfGreaterCommand;
import commands.SaveCommand;
import commands.ShowCommand;
import commands.UpdateCommand;

/**
 * Класс менеджер комманд, содержащий все команды и их описания
 */
public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();

     public CommandManager(CollectionManager cm) {
        commands.put("info", new InfoCommand(cm));
        commands.put("show", new ShowCommand(cm));
        commands.put("insert", new InsertCommand(cm));
        commands.put("update", new UpdateCommand(cm));
        commands.put("remove_key", new RemoveKeyCommand(cm));
        commands.put("clear", new ClearCommand(cm));
        commands.put("save", new SaveCommand(cm));
        commands.put("exit", new ExitCommand());
        commands.put("remove_lower", new RemoveLowerCommand(cm));
        commands.put("replace_if_greater", new ReplaceIfGreaterCommand(cm));
        commands.put("remove_greater_key", new RemoveGreaterKeyCommand(cm));
        commands.put("count_greater_than_number_of_participants", new CountGreaterThanNumberOfParticipantsCommand(cm));
        commands.put("filter_greater_than_genre", new FilterGreaterThanGenreCommand(cm));
        commands.put("print_field_descending_front_man", new PrintFieldDescendingFrontManCommand(cm));
        commands.put("help", new HelpCommand(this));

    }

    public Command getCommand(String name){
        return commands.get(name);
    }

    public HashMap<String, Command> getCommands(){
        return commands;
    }

    public void register(String name, Command command) {
        commands.put(name, command);
    }
}