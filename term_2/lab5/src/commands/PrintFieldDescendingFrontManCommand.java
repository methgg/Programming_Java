package commands;

import manager.CollectionManager;
import models.Person;
import models.MusicBand;

import java.util.Comparator;

/**
 * Печатает поле {@code frontMan} всех элементов в порядке убывания.
 */
public class PrintFieldDescendingFrontManCommand implements Command {
    private CollectionManager cm;
    private String args;

    public PrintFieldDescendingFrontManCommand(CollectionManager cm) { this.cm = cm; }

    @Override
    public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        cm.getCollection().values().stream()
                .map(MusicBand::getFrontMan)
                // Требование: значения поля frontMan в порядке убывания.
                // В качестве ключа используем имя фронтмена.
                .sorted(Comparator.comparing((Person p) -> p.getName(), Comparator.nullsLast(String::compareTo)).reversed())
                .forEach(System.out::println);
    }
}