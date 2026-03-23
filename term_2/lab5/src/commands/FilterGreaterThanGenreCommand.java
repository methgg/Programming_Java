package commands;

import manager.CollectionManager;
import models.MusicBand;
import models.MusicGenre;

/**
 * Печатает элементы, у которых genre больше заданного значения.
 */
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
        } catch (Exception e) { System.out.println("Ошибка: " + e.getMessage()); }
    }
}