/**
 * Třída ProgramViewHolder - objekt pro listview zobrazující historii
 *
 * Tato třída je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Dominik Šimon
 *@created    leden 2024
 */
package com.example.guessthepes.logic;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guessthepes.R;

public class ProgramViewHolder {

    ImageView image;
    TextView breed;
    ProgramViewHolder(View v)
    {
        image = v.findViewById(R.id.imageViewHistory);
        breed = v.findViewById(R.id.textViewBreed);
    }
}
