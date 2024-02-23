/**
 *  Třída mainActivity - spravuje funkce hlavní obrazovky aplikace
 *
 *  Tato třída je aplikace na rozpoznání plemen psů
 *
 *@author     Michal Červinka, Filip Dvořák, Dominik Šímon
 *@created    leden 2024
 */
package com.example.guessthepes.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guessthepes.R;
import com.example.guessthepes.logic.DatabaseHelper;
import com.example.guessthepes.logic.Record;
import com.example.guessthepes.logic.TFLiteModel;
import com.example.guessthepes.logic.User;

import com.example.guessthepes.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 2;
    private ImageView imageView;
    public Bitmap cameraPhoto;
    private Button buttonHistory;
    final int imageSize=224;
    private ImageView imageViewSettings;
    private TextView textViewLoggedUser;
    private TextView textViewDogBreed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageViewDog);
        buttonHistory = (Button) findViewById(R.id.buttonHistory);
        imageViewSettings = findViewById(R.id.imageViewSettings);
        textViewLoggedUser =  (TextView) findViewById(R.id.textViewLoggedUser);
        textViewDogBreed = (TextView) findViewById(R.id.textViewDogBreed);
        textViewLoggedUser.setText("Uživatel: " + LoginActivity.loggedUser.username);


        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistoryActivity();
            }
        });

        imageViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });
    }
    public void openHistoryActivity() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void openGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    public void openCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,2);

    }

    /**
     * Metoda pro zpracování vstupů z galerie nebo fotoaparátu
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap imageBeforeResize = image;

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                TFLiteModel model = new TFLiteModel(getApplicationContext());
                String breed = model.classifyImage(image);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(imageBeforeResize);
                textViewDogBreed.setText("Plemeno psa je\n" + breed);
                textViewDogBreed.setVisibility(View.VISIBLE);
                Record record = new Record(breed, LoginActivity.loggedUser,image);
                DatabaseHelper.addRecord(record);

            }
            else {
                cameraPhoto = (Bitmap) data.getExtras().get("data");
                //Record record = new Record("dawg",new User("zalio","heslo"),cameraPhoto);
                //DatabaseHelper.addRecord(record);
                imageView.setVisibility(View.VISIBLE);
                //imageView.setImageBitmap(cameraPhoto);

                //předání vyfoceneho obrazku modelu
                int dimension = Math.min(cameraPhoto.getWidth(), cameraPhoto.getHeight());
                cameraPhoto = ThumbnailUtils.extractThumbnail(cameraPhoto, dimension, dimension);

                Bitmap image = cameraPhoto;
                Bitmap imageBeforeResize = cameraPhoto;
                image = Bitmap.createScaledBitmap(cameraPhoto, imageSize, imageSize, false);
                TFLiteModel model = new TFLiteModel(this);
                String breed = model.classifyImage(image);

                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(imageBeforeResize);
                textViewDogBreed.setText("Plemeno psa je\n" + breed);
                textViewDogBreed.setVisibility(View.VISIBLE);
                Record record = new Record(breed, LoginActivity.loggedUser,image);
                DatabaseHelper.addRecord(record);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //Metoda pro testování funkcí z DatabaseHelperu
    public void test (View view){
        //List<Record> recordsList = DatabaseHelper.getRecords(new User("zalio","spekulant"));
       // imageView.setVisibility(View.VISIBLE);
       // imageView.setImageBitmap(recordsList.get(0).image);

        DatabaseHelper.addUser(new User("fidar","yapper"));
        boolean result = DatabaseHelper.authenticateUser(new User("fidar","yapper"),false);
        System.out.println(result);
    }


}