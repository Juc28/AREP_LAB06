package co.edu.escuelaing.sparkdockerdemolive;

import static spark.Spark.staticFiles;
import static spark.Spark.get;
public class LogServerFacade {
    private static final String LOG_SERVICE_URL = "http://localhost:5000/logservice";
    public static void main(String[] args) {
        RemoteLogService invoker = new RemoteLogService(LOG_SERVICE_URL);
        staticFiles.location("/public");
        get("/logserverfacade",(req,res) -> {
            res.type("Aplication/json");
             return invoker.invoke(args); });
    }

}