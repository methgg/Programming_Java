package server;

import database.DatabaseConfig;
import database.DatabaseManager;
import database.MusicBandRepository;
import database.SchemaInitializer;
import database.UserRepository;
import exceptions.ErrorMessages;
import manager.CollectionManager;

public class ServerMain {
    public static void main(String[] args) {
        try {
            DatabaseConfig databaseConfig = DatabaseConfig.fromEnvironment();
            DatabaseManager databaseManager = new DatabaseManager(databaseConfig);
            databaseManager.testConnection();
            System.out.println(ErrorMessages.DATABASE_CONNECTED);

            SchemaInitializer schemaInitializer = new SchemaInitializer(databaseManager);
            schemaInitializer.initialize();
            System.out.println(ErrorMessages.DATABASE_SCHEMA_INITIALIZED);

            CollectionManager cm = new CollectionManager();
            MusicBandRepository musicBandRepository = new MusicBandRepository(databaseManager);
            cm.getCollection().putAll(musicBandRepository.loadAll());
            System.out.println(ErrorMessages.COLLECTION_LOADED_FROM_DATABASE);

            UserRepository userRepository = new UserRepository(databaseManager);
            AuthService authService = new AuthService(userRepository);

            ServerCommandManager serverCommandManager = new ServerCommandManager(cm);
            ServerCommandProcessor serverCommandProcessor = new ServerCommandProcessor(serverCommandManager, authService);
            ServerTcpApp serverTcpApp = new ServerTcpApp(serverCommandProcessor, 25345);
            serverTcpApp.start();
        } catch (Exception e) {
            System.out.println(ErrorMessages.serverStartupError(e.getMessage()));
        }
    }
}