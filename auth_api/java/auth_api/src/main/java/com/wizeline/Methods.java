package com.wizeline;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Methods {
  public static String generateToken(String username, String password) {
    return "test";
  }
  public static String accessData(String authorization){
    return "test";
  }

  public static Boolean validateLogin(String userName, String password){
    try {
      if (userName.length() == 0 && password.length() == 0) {
        return false;
      }
      if (userName.length() == 0) {
        return false;
      }
      if (password.length() == 0) {
        return false;
      }
    }catch (NullPointerException npe){
      System.out.print("NPE caught");
      return false;
    }
    return true;
  }

  public static Connection dataBaseConnection(){

    Connection connection = null;

    try(InputStream input = App.class.getClassLoader().getResourceAsStream("connectionDB.properties")) {

      Properties prop = new Properties();

      if (input == null) {
        System.out.println("Sorry, unable to find connectionDB.properties");
        return connection;
      }

      //load a properties file from class path, inside static method
      prop.load(input);

      //get the property value and print it out, LOGGING Purposes
      System.out.println(prop.getProperty("connection.driver"));
      System.out.println(prop.getProperty("connection.url"));
      System.out.println(prop.getProperty("connection.userName"));
      System.out.println(prop.getProperty("connection.password"));

      //Defining connection values
      String dbDriver = prop.getProperty("connection.driver");
      String dbURL = prop.getProperty("connection.url");
      String dbUserName = prop.getProperty("connection.userName");
      String dbPassword = prop.getProperty("connection.password");

      Class.forName(dbDriver);
      connection = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
      connection.setAutoCommit(false);

    } catch (Exception e) {
      System.out.println("Error connecting to DB\n"
              + e.getMessage().toString());
    }
    return connection;
  }


}

