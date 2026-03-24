package commands;

import java.util.Comparator;

import manager.CollectionManager;
import models.MusicBand;
import models.Person;

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
                .sorted(Comparator.comparing((Person p) -> p.getName(), Comparator.nullsLast(String::compareTo)).reversed())
                .forEach(System.out::println);
    }
}