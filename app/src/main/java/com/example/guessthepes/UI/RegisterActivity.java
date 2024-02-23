/**
 * Třída RegisterActivity - spravuje funkce obrazovky aplikace, kde se uživatel registruje
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guessthepes.R;
import com.example.guessthepes.UI.LoginActivity;
import com.example.guessthepes.UI.MainActivity;

import com.example.guessthepes.R;
import com.example.guessthepes.logic.DatabaseHelper;
import com.example.guessthepes.logic.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {
    private Button buttonRegister;
    private ImageView imageViewBack;
    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private TextView textViewWrongRegisterCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);

        inputUsername = (EditText) findViewById(R.id.editTextUsernameRegister);
        inputPassword = (EditText) findViewById(R.id.editTextPasswordRegister);
        inputConfirmPassword = (EditText) findViewById(R.id.editTextPasswordConfirmRegister);
        textViewWrongRegisterCredentials = (TextView) findViewById(R.id.textViewWrongRegisterCredentials);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            /**
             * Metoda registrace
             */
            @Override
            // Button pro registraci
            public void onClick(View v) {
                if (inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                    User newUser = new User(inputUsername.getText().toString(), inputPassword.getText().toString());
                    boolean isRegistered = DatabaseHelper.addUser(newUser);
                    if (isRegistered){
                        MessageDigest messageDigest = null;
                        try {
                            messageDigest = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                        messageDigest.update(inputPassword.getText().toString().getBytes());
                        String password = new String(messageDigest.digest());
                        LoginActivity.loggedUser = new User(inputUsername.getText().toString(), password);
                        openMainActivity();
                    }else{
                        inputPassword.setText("");
                        inputPassword.setHint("Heslo");
                        inputConfirmPassword.setText("");
                        inputConfirmPassword.setHint("Potvrďte heslo");
                        textViewWrongRegisterCredentials.setText("Zadané uživatelské jméno je již obsazeno");
                        textViewWrongRegisterCredentials.setVisibility(View.VISIBLE);
                    }

                }else{
                    inputPassword.setText("");
                    inputPassword.setHint("Heslo");
                    inputConfirmPassword.setText("");
                    inputConfirmPassword.setHint("Potvrďte heslo");
                    textViewWrongRegisterCredentials.setText("Hesla se neshodují");
                    textViewWrongRegisterCredentials.setVisibility(View.VISIBLE);

                }


            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}