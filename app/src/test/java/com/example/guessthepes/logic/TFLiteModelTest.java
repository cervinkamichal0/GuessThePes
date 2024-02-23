package com.example.guessthepes.logic;

import static org.junit.Assert.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.guessthepes.UI.MainActivity;

import org.junit.Test;

public class TFLiteModelTest {

    @Test
    public void classifyImage() {
        MainActivity mainActivity = new MainActivity();
        TFLiteModel model = new TFLiteModel(mainActivity.getApplicationContext());
        Bitmap bMap = BitmapFactory.decodeFile("image.jpg");
        Bitmap image = Bitmap.createScaledBitmap(bMap, 331, 331, false);

        String result = model.classifyImage(image);

        assertEquals("japanese spaniel", result);

    }
}