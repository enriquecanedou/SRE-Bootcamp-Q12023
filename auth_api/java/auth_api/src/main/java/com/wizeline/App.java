package com.wizeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;

import static com.wizeline.JsonUtil.json;
import static spark.Spark.*;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws SQLException {

        //DataBase connection
        //Connection connectionDB = Methods.dataBaseConnection();

        //if(connectionDB != null){
        //    System.out.println("Connected to DB");
        //}else{
        //    System.out.println("Error: Not connected to DB");
        //}

        //TODO: Remove it later, used to verify data inside DB, from line 29 to 35
        //Statement statement = connectionDB.createStatement();
        //ResultSet rSet = statement.executeQuery("select * from bootcamp_tht.users");

        //while(rSet.next()){
        //     System.out.println("User: " + rSet.getString(1) + " Password: " + rSet.getString(2) + " Salt: "  +  rSet.getString(3) + " Role: " + rSet.getString(4));
        //}

        log.info("Listening on: http://localhost:8000/");

        port(8000);
        get("/", App::routeRoot);
        get("/_health", App::routeRoot);
        post("/login", App::urlLogin, json());
        get("/protected", App::protect, json());
    }

    public static Object routeRoot(spark.Request req, spark.Response res) throws Exception {
        return "OK";
    }

    public static Object urlLogin(spark.Request req, spark.Response res) throws Exception {
        String userName = req.queryParams("userName");
        String password = req.queryParams("password");
        Response r = null;
        if ((Methods.validateLogin(userName, password)) != false) {
            r = new Response(Methods.generateToken(userName, password));
            res.status(200);
            res.type("application/json");
            return r;
        }
        res.status(403);
        return "HTTP status code: " + res.status();
    }

    public static Object protect(spark.Request req, spark.Response res) throws Exception {
        String authorization = req.headers("Authorization");
        Response r = new Response(Methods.accessData(authorization));
        res.type("application/json");
        return r;
    }
}
