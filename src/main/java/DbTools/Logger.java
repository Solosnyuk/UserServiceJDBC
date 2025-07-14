package DbTools;

import com.mongodb.client.*;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Logger {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "test_db";
    private static final String COLLECTION_NAME = "test_logs";
    private static final MongoCollection<Document> collection;

    static {
        MongoClient mongoClient = MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    public static void log(String testName, String status, String message) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("timestamp", LocalDateTime.now().toString());
        logEntry.put("testName", testName);
        logEntry.put("status", status);
        logEntry.put("message", message);

        collection.insertOne(new Document(logEntry));
        System.out.println("Лог записан: " + logEntry);
    }

    public static void clearLogs() {
        collection.deleteMany(new Document());
        System.out.println("Все логи удалены");
    }

    public static void logAction(String action, String details) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("timestamp", LocalDateTime.now().toString());
        logEntry.put("action", action);
        logEntry.put("details", details);

        collection.insertOne(new Document(logEntry));
        System.out.println("Действие сохранено: " + logEntry);
    }
}