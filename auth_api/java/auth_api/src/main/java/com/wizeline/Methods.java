package com.wizeline;

import io.jsonwebtoken.Jwts;

import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import java.util.Properties;

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
    //Adding numb characters because secret key is too short to use 256 encryption
    String signingKey = "my2w7wjd7yXF64FIADfJxNs1oupTGAuWasdfasdfasdfasdfasdf";
    // decode the base64 encoded string
    byte[] decodedKey = Base64.getDecoder().decode(signingKey);
    // rebuild key using SecretKeySpec
    Key originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

    if (pwdEncrypted.toString().equals(dbPassword)){
      String jwtToken = Jwts.builder()
              .setHeaderParam("alg", "HS256")
              .setHeaderParam("typ", "JWT")
              .signWith(originalKey)
              .claim("role", dbRole)
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

