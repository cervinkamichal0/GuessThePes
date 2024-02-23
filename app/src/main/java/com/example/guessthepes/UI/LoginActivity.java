/**
 * Třída LoginActivity - spravuje funkce obrazovky aplikace, kde se uživatel přihlašuje
 *
 * Tato třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Michal Červinka, Filip Dvořák, Dominik Šímon
 *@created    leden 2024
 */
package com.example.guessthepes.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.guessthepes.R;
import com.example.guessthepes.logic.DatabaseHelper;
import com.example.guessthepes.logic.User;

public class LoginActivity extends AppCompatActivity {
    private EditText inputUsername;
    private EditText inputPassword;
    private Button buttonLogin;
    private TextView textViewWrongPassword;
    private TextView textViewRegister;
    public static User loggedUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewRegister = (TextView) findViewById(R.id.textViewRegister);
        inputPassword = (EditText) findViewById(R.id.editTextPassword);
        inputUsername = (EditText) findViewById(R.id.editTextUsername);
        textViewWrongPassword = (TextView) findViewById(R.id.textViewWrongPassword);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Metoda přihlašování
             *
             */
            public void onClick(View v) {
                String hashedPassword = inputPassword.getText().toString();
                hashedPassword = DatabaseHelper.getHash(hashedPassword);

                User user = new User(inputUsername.getText().toString(), hashedPassword);


                boolean isLogged = DatabaseHelper.authenticateUser(user,true);
                if (isLogged) {
                    loggedUser = user;
                    openMainActivity();
                }else{
                    inputUsername.setHint("Jméno");
                    inputPassword.setHint("Heslo");
                    textViewWrongPassword.setVisibility(View.VISIBLE);

                }
            }
        });


        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            //button pro registraci
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    //Otecření obrazovky MainActivity
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Otevření obrazovky RegisterActivity
    public void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}