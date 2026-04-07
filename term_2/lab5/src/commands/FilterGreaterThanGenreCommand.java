package commands;

import exceptions.ErrorMessages;
import manager.CollectionManager;
import models.MusicGenre;

public class FilterGreaterThanGenreCommand implements Command {
    private CollectionManager cm;
    private String args;

    public FilterGreaterThanGenreCommand(CollectionManager cm) { this.cm = cm; }

    @Override
    public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        try {
            MusicGenre genre = MusicGenre.valueOf(args.trim().toUpperCase());
            cm.getCollection().values().stream()
                    .filter(b -> b.getGenre().ordinal() > genre.ordinal())
                    .forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println(ErrorMessages.commandError("filter_greater_than_genre", ErrorMessages.INVALID_GENRE));
        }
    }
    @Override 
    public String getDescription(){
        return "вывести элементы, значение поля genre которых больше заданного";
    }
}
