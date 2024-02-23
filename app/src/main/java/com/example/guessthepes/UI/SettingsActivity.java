/**
 * Třída SettingsActivity - spravuje funkce obrazovky aplikace, kde má uživatel výběr možností odhlášení nebo změna hesla
 *
 * Tato třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Dominik Šímon
 *@created    leden 2024
 */
package com.example.guessthepes.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guessthepes.R;
import com.example.guessthepes.UI.LoginActivity;
import com.example.guessthepes.UI.MainActivity;
import com.example.guessthepes.UI.PasswordChangeActivity;

public class SettingsActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private TextView textViewPasswordChange;
    private TextView textViewLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewPasswordChange = (TextView) findViewById(R.id.textViewPasswordChange);
        textViewLogout = (TextView) findViewById(R.id.textViewLogout);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        textViewPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPasswordChangeActivity();
            }
        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            /**
             * Metoda pro odhlášení
             */
            @Override
            public void onClick(View v) {
                LoginActivity.loggedUser = null;
                openLoginActivity();
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openPasswordChangeActivity() {
        Intent intent = new Intent(this, PasswordChangeActivity.class);
        startActivity(intent);
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}