/**
 * Třída User - objekt uživatele
 *
 * Tato třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Michal Červinka
 *@created    leden 2024
 */
package com.example.guessthepes.logic;

public class User {
    public String username;
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
