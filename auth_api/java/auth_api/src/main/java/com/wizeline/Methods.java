package com.wizeline;

import io.jsonwebtoken.Jwts;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.UUID;
import java.time.Instant;
import java.util.Date;

public class Methods {
  public static String generateToken(String username, String password) throws SQLException {

    Connection connectionDB = Methods.dataBaseConnection();
    PreparedStatement prepStatement = connectionDB.prepareStatement("SELECT * FROM bootcamp_tht.users WHERE username=?");
    prepStatement.setString(1, username.toString());
    ResultSet rSet = prepStatement.executeQuery();
    String saltValue = null;
    String dbPassword = null;
    String dbRole = null;

    while(rSet.next()){
      System.out.println("UserName: "+ rSet.getString(1) + " Password: " + rSet.getString(2) + " Salt: " + rSet.getString(3) + " Role: " + rSet.getString(4));
      dbPassword = rSet.getString(2);
      saltValue = rSet.getString(3);
      dbRole = rSet.getString(4);
    }

    prepStatement.close();

    String pwdToEncrypt = password + saltValue;
    String pwdEncrypted = encryptThisString(pwdToEncrypt);
    if (pwdEncrypted.toString().equals(dbPassword)){
      String jwtToken = Jwts.builder()
              .claim("userName", username)
              .claim("role", dbRole)
              .setSubject(username)
              .setId(UUID.randomUUID().toString())
              .setIssuedAt(Date.from(Instant.now()))
              .setExpiration(Date.from(Instant.now().plus(5l, ChronoUnit.MINUTES)))
              .compact();

      return jwtToken;

    }else{
      return "Error, invalid username/password";
    }

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

  public static String encryptThisString(String input)
  {
    try {
      // getInstance() method is called with algorithm SHA-512
      MessageDigest md = MessageDigest.getInstance("SHA-512");

      // digest() method is called
      // to calculate message digest of the input string
      // returned as array of byte
      byte[] messageDigest = md.digest(input.getBytes());

      // Convert byte array into signum representation
      BigInteger no = new BigInteger(1, messageDigest);

      // Convert message digest into hex value
      String hashtext = no.toString(16);

      // Add preceding 0s to make it 32 bit
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }

      // return the HashText
      return hashtext;
    }

    // For specifying wrong message digest algorithms
    catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

}

