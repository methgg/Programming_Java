import client.ClientCommandManager;
import client.ClientCommandParser;
import exceptions.ErrorMessages;
import manager.CollectionManager;
import network.CommandRequest;
import network.CommandResponse;
import server.ServerCommandManager;
import server.ServerCommandProcessor;
import util.ConsoleReader;
import util.IdGenerator;

/**
 * Класс, который запускает программу
 */
public class Invoker {
    private final ClientCommandManager clientCommandManager;
    private final ClientCommandParser clientCommandParser;
    private final ServerCommandProcessor serverCommandProcessor;


    public Invoker(CollectionManager cm) {
        this.clientCommandManager = new ClientCommandManager();
        this.clientCommandParser = new ClientCommandParser(clientCommandManager);

        ServerCommandManager serverCommandManager = new ServerCommandManager(cm);
        this.serverCommandProcessor = new ServerCommandProcessor(serverCommandManager);
    }
    
    public void start() {
        while (true) {
            String line;

            try {
                line = ConsoleReader.readLineWithTabCompletion("> ", clientCommandManager.getCommandNames());
            } catch (Exception e) {
                System.out.println(ErrorMessages.inputError(e.getMessage()));
                continue;
            }
            if (line == null) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            try {
                CommandRequest request = clientCommandParser.parse(line);
                CommandResponse response = serverCommandProcessor.process(request);
                if (response.getMessage() != null && !response.getMessage().isEmpty()) {
                    System.out.println(response.getMessage());
                }

                if (request.getCommandType() == network.CommandType.EXIT) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
                
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
