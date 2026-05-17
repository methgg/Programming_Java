package server.commands;

import java.util.concurrent.locks.Lock;

import manager.CollectionManager;
import network.CommandRequest;
import network.CommandResponse;

public class InfoServerCommand implements ServerCommand {
    private final CollectionManager collectionManager;

    public InfoServerCommand(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override 
    public CommandResponse execute(CommandRequest request){
        Lock readLock = collectionManager.getLock().readLock();
        readLock.lock();
        try {
            String message = "=== Информация о коллекции ===\n"
                + "Тип коллекции: " + collectionManager.getCollection().getClass().getSimpleName() + "\n"
                + "Дата инициализации коллекции: " + collectionManager.getInitializationDate() + "\n"
                + "Количество элементов: " + collectionManager.size();

            return new CommandResponse(true, message, null);
        } finally {
            readLock.unlock();
        }
        
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

}