package edu.eci.arep;

import static spark.Spark.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

public class LogService {
    public static void main(String[] args) {
        port(getPort());
        staticFiles.location("/public");
        get("/logservice", (req, res) -> {
            String val = req.queryParams("message");
            return logMessage(val);
        });

    }
    private static String logMessage(String val) {
        try (MongoClient client = new MongoClient("mongo", 27017)) {
            MongoDatabase database = client.getDatabase("mydb");
            MongoCollection<Document> collection = database.getCollection("log");
            // Insertar la cadena en la base de datos con una marca de tiempo
            Document doc = new Document("message", val)
                    .append("timestamp", new Date());
            collection.insertOne(doc);
            // Recuperar las 10 últimas cadenas con sus fechas
            FindIterable<Document> cursor = collection.find()
                    .sort(Sorts.descending("timestamp"))
                    .limit(10);
            List<Map<String, Object>> logList = new ArrayList<>();
            for (Document document : cursor) {
                Map<String, Object> logEntry = new HashMap<>();
                logEntry.put("message", document.getString("message"));
                logEntry.put("timestamp", document.getDate("timestamp"));
                logList.add(logEntry);
            }
            // Crear un objeto JSON con las últimas 10 cadenas y sus fechas
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(logList);

            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar el log en MongoDB";
        }
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4568;
    }
}
