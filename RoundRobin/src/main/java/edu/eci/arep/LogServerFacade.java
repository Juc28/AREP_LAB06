package edu.eci.arep;
import static spark.Spark.*;

import java.io.IOException;
public class LogServerFacade {
    public static void main( String[] args )
    {
        port(getPort());
        staticFiles.location("/public");
        get("/log",(req,res) -> {
            String val = req.queryParams("value");
            return RemoteLogService.getLogs(val);
        });
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}
