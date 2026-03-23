package commands;

import manager.CollectionManager;
import models.MusicBand;

import java.util.Iterator;
import java.util.Map;

/**
 * Удаляет из коллекции все элементы, чьи ключи больше заданного.
 */
public class RemoveGreaterKeyCommand implements Command {
    private CollectionManager cm;
    private String args;

    public RemoveGreaterKeyCommand(CollectionManager cm) { this.cm = cm; }
    @Override
    public void setArgs(String args) { this.args = args; }

    @Override
    public void execute() {
        try {
            long key = Long.parseLong(args.trim());
            Iterator<Map.Entry<Long, MusicBand>> it = cm.getCollection().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, MusicBand> entry = it.next();
                if (entry.getKey() > key) it.remove();
            }
            System.out.println("Элементы с ключами больше " + key + " удалены.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}