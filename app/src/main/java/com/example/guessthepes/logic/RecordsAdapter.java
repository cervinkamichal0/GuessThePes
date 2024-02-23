/**
 *Třídan RecordsAdapter - podpůrná třída pro zobrazení listview zobrazující historii uživatele
 *
 * Tato třίda je součástí apklikace na rozpoznání plemen psů
 *
 *@author     Dominik Šímon
 *@created    leden 2024
 */
package com.example.guessthepes.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.guessthepes.R;

import java.util.List;

public class RecordsAdapter extends ArrayAdapter<String> {
    Context context;
    String[] breedArray;
    Bitmap[] imageArray;


    public RecordsAdapter(Context context, String[] breedArray, Bitmap[] imageArray) {
        super(context, R.layout.single_item, R.id.textViewBreed, breedArray);
        this.context = context;
        this.imageArray = imageArray;
        this.breedArray = breedArray;
    }

    //vytvoření jednotlivých views pro každý prvek v adapteru
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View singleItem = convertView;
        ProgramViewHolder holder = null;
        if(singleItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.single_item, parent, false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        }
        else{
            holder = (ProgramViewHolder) singleItem.getTag();
        }
        holder.image.setImageBitmap(imageArray[position]);
        holder.breed.setText(breedArray[position]);

        return singleItem;
    }
}
