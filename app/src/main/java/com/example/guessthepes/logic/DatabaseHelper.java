/**
 * Třída DatabaseHelper - spravuje funkce databáze
 *
 * Tato třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Michal Červinka, Filip Dvořák
 *@created    leden 2024
 */
package com.example.guessthepes.logic;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {



    /**
     * Metoda pro připojení k databízi
     *
     *param
     *@return vrací připojení connection
     */
    private static Connection getConnection() {
        String ip="guessthepes.cra4keku2tom.eu-north-1.rds.amazonaws.com",port="1433",db="gtp",username="admin",password="adminadmin";
        Connection con = null;

        StrictMode.ThreadPolicy a = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(a);
        String ConnectURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectURL = "jdbc:jtds:sqlserver://"+ip+":"+port+";databaseName="+db+";user="+username+";password="+password+";";
            con = DriverManager.getConnection(ConnectURL);
        }
        catch (Exception ex) {
            Log.e("Error: ",ex.getMessage());
        }
        return con;
    }

    /**
     * Metoda autentifikace uživatele
     *
     *param objekt uživatel a jestli jsou jehu údaje hashovane
     *@return Vrací true pokud je uživatel autentifikovan
     */
    public static boolean authenticateUser(User user, boolean isHashed) {

        try {
            String password = user.password;
            String hashedPassword;
            if (!isHashed) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(password.getBytes());
                 hashedPassword = new String(messageDigest.digest());
            }
            else {
                 hashedPassword = user.password;
            }
            String command = "SELECT * FROM users WHERE username = '"+user.username+"' AND password = '"+hashedPassword+"'";
            try {
                Connection con = getConnection();
                Statement smt = con.createStatement();
                ResultSet set = smt.executeQuery(command);
                if (set.next()) {
                    return true;
                }
            } catch (Exception ex) {
                Log.e("Error: ", ex.getMessage());
                return false;
            }
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
    /**
     * Metoda pro uložení nových data do databáze
     *
     *param objekt record
     *@return vrací true pokud se podarilo uložit
     */
    public static boolean addRecord(Record record)
    {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            record.image.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            byte[] bytesImage = byteArrayOutputStream.toByteArray();

            String command = "INSERT INTO records (breed, username, image) VALUES (?,?,?)";

            try {
                Connection con = getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(command);
                preparedStatement.setString(1, record.breed);
                preparedStatement.setString(2, record.user.username);
                preparedStatement.setBytes(3, bytesImage);
                preparedStatement.execute();
                return true;
            } catch (Exception ex) {
                Log.e("Error: ", ex.getMessage());
                return false;
            }
    }
    /**
     * Metoda pro načtení dat z databáze
     *
     *param Objekt uživatel pro kterého se mají vypsat data
     *@return List s daty
     */
    public static List<Record> getRecords(User user)
    {

        if(authenticateUser(user,true))
        {
            String command = "SELECT * FROM records WHERE username = '"+user.username+"'";
            List<Record> recordsList = new ArrayList<>();
            try {
                Connection con = getConnection();
                Statement smt = con.createStatement();
                ResultSet set = smt.executeQuery(command);
                while (set.next())
                {
                    Record record = new Record(set.getString("breed"),user,BitmapFactory.decodeByteArray(set.getBytes("image"), 0, set.getBytes("image").length));
                    recordsList.add(record);
                }
                return recordsList;
            }
            catch (Exception ex) {
                Log.e("Error: ",ex.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * Metoda pro změnu hesla
     *
     *param Objekt uživatele pro kterého heslo měnime a nové heslo
     *@return Vrací true pokud se podařilo heslo změnit
     */
    public static boolean changePassword(User user , String newPassword)
    {
        String oldPassword=user.password;
        if (authenticateUser(user,true)) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(newPassword.getBytes());
                newPassword = new String(messageDigest.digest());
            }
            catch (NoSuchAlgorithmException e){
                e.printStackTrace();
                return false;
            }

            String command = "UPDATE users SET password = '" + newPassword + "' WHERE username = '" + user.username + "' AND password = '" + oldPassword + "'";
            try {
                Connection con = getConnection();
                Statement smt = con.createStatement();
                smt.execute(command);
                return true;
            }catch (Exception ex) {
                Log.e("Error: ",ex.getMessage());
                return false;
            }
        }
        return false;
    }
    /**
     * Metoda pro přidání nového uživatele
     *
     *param Objekt uživatele
     *@return Vrací true pokud se podařilo uživatele přidat
     */
    public static boolean addUser(User user)
    {
        String command = "INSERT INTO users (username, password) VALUES (?,?)";
        String password = user.password;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String hashedPassword = new String(messageDigest.digest());
            try {
                Connection con = getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(command);
                preparedStatement.setString(1, user.username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.execute();
                return true;
            } catch (Exception ex) {
                Log.e("Error: ", ex.getMessage());
                return false;
            }
        }catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Metoda pro vytvoření hash
     *
     *param String pro hashování
     *@return Zahashovaný string
     */
    public static String getHash(String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            return new String(messageDigest.digest());
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return "";
        }
    }

}
