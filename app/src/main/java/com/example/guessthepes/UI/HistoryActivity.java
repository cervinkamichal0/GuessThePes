/**
 *  Třída HistoryActivity - spravuje funkce obrazovky aplikace, kde se zobrazuje historie
 *  uživatelem nahraných obrázků
 *
 *  Tato třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Michal Červinka, Filip Dvořák, Dominik Šímon
 *@created    leden 2024
 */
package com.example.guessthepes.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.guessthepes.R;
import com.example.guessthepes.logic.DatabaseHelper;
import com.example.guessthepes.logic.Record;
import com.example.guessthepes.logic.RecordsAdapter;

import android.graphics.Bitmap;

import java.util.List;
import java.util.stream.Collectors;

public class HistoryActivity extends AppCompatActivity {
    private ImageView imageViewBack;

    ListView listview;

    /**
     * Metoda pro vytvoření listview zobrazující historii a načtení záznamů
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        listview = (ListView) findViewById(R.id.listView);

        //získání záznamů aktuálně přihlášeného uživatele
        List<Record> listOfRecords = DatabaseHelper.getRecords(LoginActivity.loggedUser);

        //vytvoření pole plemen ze záznamů
        List<String> breedList = listOfRecords.stream()
                .map(Record::getBreed)
                .collect(Collectors.toList());
        String[] breedArray = breedList.toArray(new String[0]);

        //vytvoření pole obrázků ze záznamů
        List<Bitmap> imageList = listOfRecords.stream()
                .map(Record::getImage)
                .collect(Collectors.toList());
        Bitmap[] imageArray = imageList.toArray(new Bitmap[0]);

        //přidání záznamů do listview
        reverseArrays(breedArray, imageArray);
        RecordsAdapter recordsAdapter = new RecordsAdapter(this,breedArray, imageArray);
        listview.setAdapter(recordsAdapter);

        // button pro krok zpět
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    //přesměrování na main activity
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Metoda pro převrácení polí
     *
     *param pole pro převrácení
     *
     */
    private void reverseArrays(String[] breedArray, Bitmap[] imageArray) {
        int start = 0;
        int end = breedArray.length - 1;

        while (start < end) {
            // Swap elements in breedArray
            String tempBreed = breedArray[start];
            breedArray[start] = breedArray[end];
            breedArray[end] = tempBreed;

            // Swap elements in imageArray
            Bitmap tempImage = imageArray[start];
            imageArray[start] = imageArray[end];
            imageArray[end] = tempImage;

            // Move to the next pair of elements
            start++;
            end--;
        }
    }
}