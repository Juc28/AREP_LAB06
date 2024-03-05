package co.edu.escuelaing.sparkdockerdemolive;

import static spark.Spark.*;

public class LogService {
    public static void main(String[] args) {
        port(5000);
        get("/logserver",(req,res) -> {
            System.out.println("logservice");
            res.type("Aplication/json");
            return "Hola mundo"; });
    }
}
