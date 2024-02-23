/**
 * Třída Record - objekt záznamu obsahující informace o uživateli, fotce a rozpoznaném plemeni
 *
 * Třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Michal Červinka
 *@created    leden 2024
 */
package com.example.guessthepes.logic;

import android.graphics.Bitmap;


public class Record {
    public String breed;
    public User user;
    public Bitmap image;
    public Record(String breed, User user, Bitmap image) {
        this.breed = breed;
        this.user = user;
        this.image = image;
    }
    public Bitmap getImage(){
        return image;
    }
    public String getBreed(){
        return breed;
    }




}
