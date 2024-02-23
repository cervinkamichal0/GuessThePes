/**
 * Třída PasswordChangeActivity - spravuje funkce obrazovky aplikace, kde si uživatel může zmenit heslo
 *
 * Tato třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Michal Červinka, Filip Dvořák, Dominik Šímon
 *@created    leden 2024
 */
package com.example.guessthepes.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guessthepes.R;

import com.example.guessthepes.logic.DatabaseHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordChangeActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private Button buttonChangePassword;
    private EditText inputPassword;
    private EditText inputNewPassword;
    private TextView textViewWrongNewPasswordCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        imageViewBack =  findViewById(R.id.imageViewBack);
        buttonChangePassword =  findViewById(R.id.buttonChangePassword);

        inputPassword =  findViewById(R.id.editTextOldPassword);
        inputNewPassword =  findViewById(R.id.editTextNewPassword);
        textViewWrongNewPasswordCredentials =  findViewById(R.id.textViewWrongNewPasswordCredentials);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            /**
             * Metoda pro změnu hesla
             *
             */
            @Override
            public void onClick(View v) {
                MessageDigest messageDigest;
                try {
                    messageDigest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                messageDigest.update(inputPassword.getText().toString().getBytes());
                String hashedOldPassword = new String(messageDigest.digest());
                if (inputNewPassword.getText().toString().length() > 0) {
                    if (hashedOldPassword.equals(LoginActivity.loggedUser.password))
                    {
                        String stareHeslo = inputPassword.getText().toString();
                        String noveHeslo = inputNewPassword.getText().toString();
                        if(!stareHeslo.equals(noveHeslo)) {
                            boolean succes = DatabaseHelper.changePassword(LoginActivity.loggedUser ,inputNewPassword.getText().toString());
                            if (succes) {
                                LoginActivity.loggedUser.password = DatabaseHelper.getHash(inputNewPassword.getText().toString());
                                AlertDialog successDialog = createSuccessDialog();
                                successDialog.show();

                            } else {
                                AlertDialog failureDialog = createFailureDialog();
                                failureDialog.show();
                            }
                        }
                        else {
                            textViewWrongNewPasswordCredentials.setText("Nové heslo je stejné jako staré heslo");
                            textViewWrongNewPasswordCredentials.setVisibility(View.VISIBLE);
                        }
                    }else{
                        textViewWrongNewPasswordCredentials.setText("Staré heslo není správné");
                        textViewWrongNewPasswordCredentials.setVisibility(View.VISIBLE);
                    }

                }else{
                    textViewWrongNewPasswordCredentials.setText("Kolonka nové heslo nesmí být prázdná");
                    textViewWrongNewPasswordCredentials.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Metoda inicializace AlertDialog pro success
     *
     */
    AlertDialog createSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vaše heslo bylo úspěšně\nzměněno.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openMainActivity();
            }
        });

        return builder.create();
    }

    /**
     * Metoda inicializace AlertDialog pro failure
     *
     */
    AlertDialog createFailureDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vyskytl se problém s připojením.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openMainActivity();
            }
        });

        return builder.create();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}